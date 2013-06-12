/*
 * Provides JNI methods interfaces (generated by javah command).
 *
 *  Created on: 07.08.2012
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_JAVA_H_
#define FD_JAVA_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_facedev_js_debug_ie_IEJsDebugger
 * Method:    initIEDriver
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_initIEDriver
  (JNIEnv *, jclass);

/*
 * Class:     com_facedev_js_debug_ie_IEJsDebugger
 * Method:    disposeIEDriver
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_disposeIEDriver
  (JNIEnv *, jclass);

/*
 * Class:     com_facedev_js_debug_ie_IEJsDebugger
 * Method:    getDebuggerName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_getDebuggerName
  (JNIEnv *, jclass);

/*
 * Class:     com_facedev_js_debug_ie_IEJsDebugger
 * Method:    fillIEInstance
 * Signature: (Lcom/facedev/js/debug/ie/IEJsDebuggerInstance;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_fillIEInstance
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_facedev_js_debug_ie_IEJsDebugger
 * Method:    resetIEDriver
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_facedev_js_debug_ie_IEJsDebugger_resetIEDriver
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif

#endif /* FD_JAVA_H_ */
