/*
 * Bridge class for communications between java and debug plugin.
 *
 *  Created on: 31.07.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifdef _WIN32

#include "../../com.facedev.native.common/include/fd_common.h"
#include "../include/fd_bridge.h"
#include "../include/fd_java.h"
#include <string>

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_initIEDriver
  (JNIEnv* env, jclass clazz) {

	if (fd::bridge::get()->install() == FD_BRIDGE_STATE_ERROR) {
		return JNI_FALSE;
	}
	if (fd::bridge::get()->reset() == FD_BRIDGE_STATE_ERROR) {
		return JNI_FALSE;
	}

	return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_disposeIEDriver
  (JNIEnv* env, jclass clazz) {
	fd::bridge::get()->uninstall();
}

JNIEXPORT jstring JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_getDebuggerName
  (JNIEnv* env, jclass clazz) {
	return env->NewStringUTF(fd::bridge::get()->name().c_str());
}

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_fillIEInstance
  (JNIEnv* env, jclass clazz, jobject instance, jint index) {
	std::vector<fd::ieinstance*> all = fd::bridge::get()->list();
	if (index < 0 || ((size_t)index) >= all.size()) {
		return JNI_FALSE;
	}
	fd::ieinstance* instancePointer = all[index];

	jclass instanceClazz = env->GetObjectClass(instance);
	jmethodID initMethod = env->GetMethodID(instanceClazz, "init", "(JLjava/lang/String;)V");
	if (initMethod == fd_null) {
		fd_error("Unable to find class method: com.facedev.js.debug.ie.IEJsDebuggerInstance::init");
		return JNI_FALSE;
	}
	std::wstring name = instancePointer->name();

	env->CallVoidMethod(instance, initMethod,
			(jlong)instancePointer->id(),
			env->NewString((const jchar*)name.c_str(), name.size()));

	return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_resetIEDriver
  (JNIEnv* env, jclass clazz) {
	fd::bridge::get()->reset();
}

#endif // #ifdef _WIN32
