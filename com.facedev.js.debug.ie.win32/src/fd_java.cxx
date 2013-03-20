/*
 * Bridge class for communications between java and debug plugin.
 *
 *  Created on: 31.07.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#include "../../com.facedev.native.common/include/fd_common.h"
#include "../include/fd_bridge.h"
#include "../include/fd_java.h"
#include <string>

static JavaVM* javaVM = fd_null;

static void listenChanges(fd_ieinstance* instance, size_t index, fd_uint state) {
	if (javaVM == fd_null) {
		return;
	}
	JNIEnv* env;
	jint status = javaVM->GetEnv((void**)&env, JNI_VERSION_1_4);

	if (status == JNI_EDETACHED) {

	} else if (status == JNI_OK) {
		if (javaVM->AttachCurrentThread((void **) &env, NULL) != 0) {
			fd_log("Failed to attach current thread to JVM");
			return;
		}
	} else if (status == JNI_EVERSION) {
		fd_log("Error while notifying listeners: bad java version");
		return;// Not supported version
	}

	jclass clazz = env->FindClass("com/facedev/js/debug/ie/IEJsDebugger");
	if (clazz != fd_null) {
		fd_log("Unable to find the class com.facedev.js.debug.ie.IEJsDebugger");
		javaVM->DetachCurrentThread();
		return;
	}
	jmethodID method = env->GetStaticMethodID(clazz, "notifyListeners", "(IZ)V");
	if (method == fd_null) {
		fd_log("Unable to find the method com.facedev.js.debug.ie.IEJsDebugger.notifyListeners(int, boolean)");
		javaVM->DetachCurrentThread();
		return;
	}

	// com.facedev.js.debug.ie.IEJsDebugger.notifyListeners(int, boolean)
	env->CallStaticVoidMethod(clazz, method, (jint)index, state == FD_BHO_STATE_ACTIVATED ? JNI_FALSE : JNI_TRUE);

	if (env->ExceptionCheck()) {
		env->ExceptionDescribe();
	}

	javaVM->DetachCurrentThread();
}

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_initIEDriver
  (JNIEnv* env, jclass clazz) {

	if (fd_bridge::get()->install() == FD_BHO_STATE_ERROR) {
		return JNI_FALSE;
	}

	fd_bridge::get()->listen(listenChanges);
	env->GetJavaVM(&javaVM);

	return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_disposeIEDriver
  (JNIEnv* env, jclass clazz) {
	javaVM = fd_null;
	fd_bridge::get()->unlisten(listenChanges);
	fd_bridge::get()->uninstall();
}

JNIEXPORT jstring JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_getDebuggerName
  (JNIEnv* env, jclass clazz) {
	return env->NewStringUTF(fd_bridge::get()->name().c_str());
}

JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_fillIEInstance
  (JNIEnv* env, jclass clazz, jobject instance, jint index) {
	std::vector<fd_ieinstance*> all = fd_bridge::get()->list();
	if (index < 0 || ((size_t)index) >= all.size()) {
		return JNI_FALSE;
	}
	fd_ieinstance* instancePointer = all[index];
	std::string name = instancePointer->name();

	jclass instanceClazz = env->GetObjectClass(instance);
	jfieldID nameField = env->GetFieldID(instanceClazz, "name", "Ljava/lang/String;");
	if (nameField == fd_null) {
		fd_log("Unable to find class field: com.facedev.js.debug.ie.IEJsDebuggerInstance::name");
		return JNI_FALSE;
	}
	env->SetObjectField(instance, nameField, env->NewStringUTF(name.c_str()));

	return JNI_TRUE;
}
