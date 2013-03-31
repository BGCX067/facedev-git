/*
 * Bridge implementation.
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include <string.h>
#include <ole2.h>
#include <mshtml.h>
#include <oleacc.h>
#include <vector>
#include <tchar.h>
#include <shlwapi.h>
#include <string>

#include "../include/fd_win_common.h"
#include "../include/fd_bridge.h"
#include "../include/fd_ie.h"

fd::bridge* fd::bridge::instance = fd_null;

#define FD_IE_CLASS_SIZE 128

#define FD_IE_SERVER_CLASS "Internet Explorer_Server"

#define FD_IE_FRAME_CLASS "IEFrame"

#define FD_IE_NAME "Microsoft Internet Explorer"

static IHTMLDocument2* request_doc(HWND hWnd) {
	HINSTANCE hInst = ::LoadLibrary(_T("OLEACC.DLL"));
	if ( hInst == NULL ) {
		return fd_false;
	}

	LRESULT lRes;

	UINT nMsg = ::RegisterWindowMessage(_T("WM_HTML_GETOBJECT"));

	::SendMessageTimeout(hWnd, nMsg, 0L, 0L, SMTO_ABORTIFHUNG, 1000, (PDWORD_PTR)&lRes);

	LPFNOBJECTFROMLRESULT pfObjectFromLresult = (LPFNOBJECTFROMLRESULT)::GetProcAddress(hInst, _T("ObjectFromLresult"));
	if (pfObjectFromLresult == NULL) {
		return fd_null;
	}
	IHTMLDocument2* document = fd_null;
	HRESULT hr = (*pfObjectFromLresult)( lRes, IID_IHTMLDocument2, 0, (void**)&document );
	if (!fd_win_check_n_log(hr)) {
		fd_error("Unable to handle IE tab");
		return fd_null;
	}
	return document;
};

BOOL fd::bridge::fd_enum_ie_win(HWND hWnd, LPARAM lParam) {
	char winCls[FD_IE_CLASS_SIZE];

	if (GetClassName(hWnd, winCls, FD_IE_CLASS_SIZE) == 0) {
		return TRUE;
	}
	if (strcmp(FD_IE_SERVER_CLASS, winCls) == 0) {
		fd::bridge::register_handle(hWnd);
		return TRUE;
	}

	if (strcmp(FD_IE_FRAME_CLASS, winCls) == 0) {
		EnumChildWindows(hWnd, fd_enum_ie_win, lParam);
		return TRUE;
	}

	return TRUE;
}

void fd::bridge::register_handle(HWND hWnd) {
	IHTMLDocument2* document = request_doc(hWnd);
	if (document == fd_null) {
		return;
	}

	BSTR bTitle;
	if (!fd_win_check_n_log(document->get_title(&bTitle))) {
		return;
	}
	std::wstring name(bTitle, SysStringLen(bTitle));

	document->Release();

	fd_static_assert(sizeof(HWND)<=8);

	get()->_registered.push_back(
		new fd::ieinstance((fd_ulong)hWnd, name));
}

fd::bridge::~bridge() {
	for (std::vector<ieinstance*>::iterator it = _registered.begin();
			it != _registered.end(); ++it) {
		delete (*it);
	}
}

fd_uint fd::bridge::install() {

	fd_info("Installing IE debugger.");

	return FD_BRIDGE_STATE_INSTALLED;
}


fd_uint fd::bridge::uninstall() {
	_registered.clear();
	return FD_BRIDGE_STATE_UNINSTALLED;
}

fd_uint fd::bridge::reset() {
	if (!fd_win_check_n_log(CoInitialize(NULL))) {
		fd_error("Unable to initialize COM");
		return FD_BRIDGE_STATE_ERROR;
	}
	_registered.clear();

	if (!EnumWindows(fd_enum_ie_win, 0)) {
		HRESULT result = HRESULT_FROM_WIN32(GetLastError());
		fd_win_check_n_log(result);
		fd_error("Unable to load debugger because it is unable to enumerate windows.");
		return FD_BRIDGE_STATE_ERROR;
	}

	return FD_BRIDGE_STATE_NOT_CHANGED;
}

const std::string fd::bridge::name() {
	/*
	 * Getting version from "HKLM\Software\\Microsoft\\Internet Explorer"
	 */
	static const char* KEY_PATH = "Software\\Microsoft\\Internet Explorer";
	static const char* KEY_NAME = "Version";
	static const size_t BUFFER_SIZE = 1024;
	static const size_t BUFFER_LENGTH = BUFFER_SIZE * sizeof(TCHAR);
	HKEY key;
	TCHAR value[BUFFER_SIZE];
	DWORD bufLen = BUFFER_LENGTH;

	if( RegOpenKeyExA(HKEY_LOCAL_MACHINE, KEY_PATH, 0, KEY_QUERY_VALUE, &key) != ERROR_SUCCESS ){
		fd_error("Unable determine IE version: cannot open registry.");
		return FD_IE_NAME" (unknown version)";
	}
	if (RegQueryValueExA(key, KEY_NAME, 0, 0, (LPBYTE) value, &bufLen) != ERROR_SUCCESS || (bufLen > BUFFER_LENGTH)) {
		fd_error("Unable determine IE version: cannot read registry.");
		RegCloseKey(key);
		return FD_IE_NAME" (unknown version)";
	}
	RegCloseKey(key);
	value[bufLen - 1] = 0;

	std::string result(FD_IE_NAME" (version ");
	result += value;
	result += ')';
	return result;
}

