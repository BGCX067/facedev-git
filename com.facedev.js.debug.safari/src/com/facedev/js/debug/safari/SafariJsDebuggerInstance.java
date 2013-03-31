package com.facedev.js.debug.safari;

import com.facedev.js.debug.JsDebuggerInstance;

public class SafariJsDebuggerInstance implements JsDebuggerInstance {
	
	private String name = "(unknown)";
	
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

}
