/*
 * Java JNI functions implementation.
 *
 *  Created on: Mar 31, 2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../include/fd_java.h"
#include "../include/fd_safari.h"

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_initSafariDriver
  (JNIEnv* env, jclass clazz) {
	if (fd::safari_bridge::get()->install() != FD_SAFARI_ERROR) {
		return JNI_TRUE;
	}
	return JNI_FALSE;
}

JNIEXPORT void JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_disposeSafariDriver
  (JNIEnv* env, jclass clazz) {
	fd::safari_bridge::get()->uninstall();
}

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_fillSafariInstance
  (JNIEnv* env, jclass clazz, jobject instance, jint index) {
	std::vector<fd::safari_instance*> instances = fd::safari_bridge::get()->list();

	if (instances.size() <= (size_t)index || index <0) {
		return JNI_FALSE;
	}
	fd::safari_instance* data = instances[index];
	jclass instanceClazz = env->GetObjectClass(instance);
	jmethodID initMethod = env->GetMethodID(instanceClazz, "init", "(Ljava/lang/String;)V");
	if (initMethod == fd_null) {
		fd_error("Unable to find class methd: com.facedev.js.debug.safari."
				"SafariJsDebuggerInstance::init");
		return JNI_FALSE;
	}
	env->CallVoidMethod(instance, initMethod,
			env->NewStringUTF(data->name().c_str()));

	return JNI_TRUE;
}

JNIEXPORT jstring JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_getDriverName
  (JNIEnv * env, jclass clazz) {
	return env->NewStringUTF("Safari (test)");
}


