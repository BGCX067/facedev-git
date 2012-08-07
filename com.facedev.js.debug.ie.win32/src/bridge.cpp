/*
 * Bridge class for communications between java and debug plugin.
 *
 *  Created on: 31.07.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/com_facedev_js_debug_ie_IEJsDebugger.h"
#include <iostream>
#include "windows.h"


JNIEXPORT jint JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_getRegisteredInstancesCount
  (JNIEnv *, jclass) {
	size_t MAX_SIZE = 1024;
	char rez[MAX_SIZE];

	HMODULE module = GetModuleHandleA("ie_debug_win32.dll");
	GetModuleFileName(module, rez, MAX_SIZE);
	std::cout << "File name: " << rez << std::endl;

	return 1;
}
