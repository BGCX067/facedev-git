package com.facedev.js.debug;

/**
 * This interface provides a way for clients to listen for a changes of instance count for specific debugger.
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDebuggerInstanceListener {
	
	/**
	 * This method is called when instance is added to the debugger.
	 * @param instance
	 */
	void onInstanceAdd(JsDebuggerInstance instance);
	
	/**
	 * This method is called when instance is removed from the debugger.
	 * @param instance
	 */
	void onInstanceRemove(JsDebuggerInstance instance);
	

	/**
	 * This method is called when instance is changed by third-party process.
	 * Change may include change of title or other information.
	 * @param instance
	 */
	void onInstanceChange(JsDebuggerInstance instance);
	
	/**
	 * Notifies listeners that some instances has been changed (added, removed or changed).
	 * This method is called by debugged after analyzing all the instances with some delay.
	 */
	void onInstancesChanged();
}
