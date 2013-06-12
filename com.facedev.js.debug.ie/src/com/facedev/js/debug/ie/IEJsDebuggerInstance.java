package com.facedev.js.debug.ie;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerInstance;

final class IEJsDebuggerInstance implements JsDebuggerInstance {
	
	private long id;
	private String name = "Unknown";
	private IEJsDebugger debugger;
	
	IEJsDebuggerInstance(IEJsDebugger debugger) {
		this.debugger = debugger;
	}
	
	// actually used from JNI
	@SuppressWarnings("unused")
	private void init(long id, String name) {
		this.id = id;
		if (name == null || (name = name.trim()).length() == 0) {
			this.name = "(blank)";
		} else {
			this.name = name;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggerInstance#getID()
	 */
	public Long getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggerInstance#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggerInstance#getDebugger()
	 */
	public JsDebugger getDebugger() {
		return debugger;
	}
}
