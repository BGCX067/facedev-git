/*
 * JNI interface functions.
 *
 *  Created on: Mar 27, 2013
 *      Author: alex.bereznevatiy@gmail.com
 */

#ifndef FD_JAVA_H_
#define FD_JAVA_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_facedev_js_debug_safari_SafariJsDebugger
 * Method:    initSafariDriver
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_initSafariDriver
  (JNIEnv *, jclass);

/*
 * Class:     com_facedev_js_debug_safari_SafariJsDebugger
 * Method:    disposeSafariDriver
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_disposeSafariDriver
  (JNIEnv *, jclass);

/*
 * Class:     com_facedev_js_debug_safari_SafariJsDebugger
 * Method:    fillSafariInstance
 * Signature: (Lcom/facedev/js/debug/safari/SafariJsDebuggerInstance;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_fillSafariInstance
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     com_facedev_js_debug_safari_SafariJsDebugger
 * Method:    getDriverName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_facedev_js_debug_safari_SafariJsDebugger_getDriverName
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif

#endif /* FD_JAVA_H_ */
