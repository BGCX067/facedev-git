package com.facedev.js.debug;

/**
 * General exception for any troubles with debuggers.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsDebuggerException extends Exception {
	private static final long serialVersionUID = 3907466578208665329L;

	public JsDebuggerException() {
		super();
	}

	public JsDebuggerException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsDebuggerException(String message) {
		super(message);
	}

	public JsDebuggerException(Throwable cause) {
		super(cause);
	}
}
