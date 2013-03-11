package com.facedev.js.debug;

/**
 * Listens to the states of debuggers. THe only method of this interface is called when debugger is either added or removed.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDebuggerChangeListener {
	
	/**
	 * Provides information about what is happened with debugger at the moment this listener has been invoked.
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	public static enum State {
		ADDED, REMOVED;
	}

	void onJsDebuggerChange(JsDebugger debugger, State state);
}
