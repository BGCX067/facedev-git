package com.facedev.js.debug;

/**
 * Instance of specific debugger extension.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDebuggerInstance {
	/**
	 * @return name of this instance (usually corresponding to the browser window or tab title).
	 */
	String getName();
}
