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

#include "../include/fd_win_common.h"
#include "../include/fd_bridge.h"
#include "../include/fd_ie.h"

fd::bridge* fd::bridge::instance = fd_null;

#define FD_IE_CLASS_SIZE 128

#define FD_IE_SERVER_CLASS "Internet Explorer_Server"

#define FD_IE_FRAME_CLASS "IEFrame"

struct fd_ie_win_result {
	std::vector<std::wstring> registered;
};

static void fd_create_ie_instance(fd_ie_win_result* result, HWND hwnd) {
	HINSTANCE hInst = ::LoadLibrary(_T("OLEACC.DLL"));
	if ( hInst == NULL ) {
		return;
	}

	IHTMLDocument2* spDoc;
	LRESULT lRes;

	UINT nMsg = ::RegisterWindowMessage(_T("WM_HTML_GETOBJECT"));

	::SendMessageTimeout( hwnd, nMsg, 0L, 0L, SMTO_ABORTIFHUNG, 1000, (PDWORD_PTR)&lRes );

	LPFNOBJECTFROMLRESULT pfObjectFromLresult = (LPFNOBJECTFROMLRESULT)::GetProcAddress(hInst, _T("ObjectFromLresult"));
	if (pfObjectFromLresult != NULL) {
		HRESULT hr = (*pfObjectFromLresult)( lRes, IID_IHTMLDocument2, 0, (void**)&spDoc );
		if (!fd_win_check_n_log(hr)) {
			fd_error("Unable to handle IE tab");
			return;
		}
		fd_debug("Loading information for IE tab.");
		BSTR rez;
		if (fd_win_check_n_log(spDoc->get_title(&rez))) {
			std::wstring str(rez, SysStringLen(rez));
			result->registered.push_back(str);
			fd_debug("Loading IE tab was successful: %ls", str.c_str());
		}
		spDoc->Release();
	}
};

static BOOL fd_enum_ie_win(HWND hwnd, LPARAM lParam) {
	char winCls[FD_IE_CLASS_SIZE];

	if (GetClassName(hwnd, winCls, FD_IE_CLASS_SIZE) == 0) {
	    return TRUE;
	}

	if (strcmp(FD_IE_SERVER_CLASS, winCls) == 0) {
		fd_create_ie_instance((fd_ie_win_result*)lParam, hwnd);
		return TRUE;
	}

	if (strcmp(FD_IE_FRAME_CLASS, winCls) == 0) {
		EnumChildWindows(hwnd, fd_enum_ie_win, lParam);
		return TRUE;
	}

	return TRUE;
}

fd_uint fd::bridge::install() {

	fd_info("Installing IE debugger.");

	if (!fd_win_check_n_log(CoInitialize(NULL))) {
		fd_error("Unable to initialize COM");
		return FD_BRIDGE_STATE_ERROR;
	}

	fd_info("Searching for IE windows.");
	fd_ie_win_result res;

	if (!EnumWindows(fd_enum_ie_win, (LPARAM)&res)) {
		HRESULT result = HRESULT_FROM_WIN32(GetLastError());
		fd_win_check_n_log(result);
		fd_error("Unable to load debugger because it is unable to enumerate windows.");
		return FD_BRIDGE_STATE_ERROR;
	}

	fd_debug("Windows count is: %d", (int)res.registered.size());

	for (std::vector<std::wstring>::iterator itr = res.registered.begin(); itr != res.registered.end(); ++itr) {
		_registered.push_back(new fd::ieinstance(*itr));
	}

	return FD_BRIDGE_STATE_INSTALLED;
}


fd_uint fd::bridge::uninstall() {
	_registered.clear();
	return FD_BRIDGE_STATE_UNINSTALLED;
}

std::string fd::bridge::name() {
	return "Internet Explorer (test)";
}
