package com.facedev.js.debug.ie;

import com.facedev.js.debug.JsDebuggerInstance;

final class IEJsDebuggerInstance implements JsDebuggerInstance {
	
	private String name = "Unknown";
	
	IEJsDebuggerInstance() {}
	
	void init(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
