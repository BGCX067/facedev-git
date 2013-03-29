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
	
	/**
	 * @return unique identifier of this instance. This object is guaranteed to be unique only within debugger.
	 */
	Object getID();
	
	/**
	 * @return parent debugger for this instance
	 */
	JsDebugger getDebugger();
}
