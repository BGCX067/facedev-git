package com.facedev.js.debug.safari;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerInstance;

public class SafariJsDebuggerInstance implements JsDebuggerInstance {
	
	private String name = "(unknown)";
	private JsDebugger debugger;
	
	SafariJsDebuggerInstance(JsDebugger debugger) {
		this.debugger = debugger;
	}
	
	@SuppressWarnings("unused")
	private void init(String name) {
		this.name = name;
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
	 * @see com.facedev.js.debug.JsDebuggerInstance#getID()
	 */
	public Object getID() {
		return "[Unknown]";
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggerInstance#getDebugger()
	 */
	public JsDebugger getDebugger() {
		return debugger;
	}

}
