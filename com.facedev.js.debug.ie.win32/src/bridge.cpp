/*
 * Bridge class for communications between java and debug plugin.
 *
 *  Created on: 31.07.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/com_facedev_js_debug_ie_IEJsDebugger.h"


JNIEXPORT jint JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_getRegisteredInstancesCount
  (JNIEnv *, jclass) {
	return 1;
}
