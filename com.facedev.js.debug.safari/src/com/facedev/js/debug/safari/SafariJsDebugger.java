package com.facedev.js.debug.safari;

import java.util.Collections;
import java.util.List;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggerInstanceListener;

public class SafariJsDebugger implements JsDebugger {

	public SafariJsDebugger() {
		// TODO Auto-generated constructor stub
	}

	public List<JsDebuggerInstance> getRegisteredInstances()
			throws JsDebuggerException {
		return Collections.emptyList();
	}

	public String getName() {
		return "Safari";
	}

	public boolean isSupported() {
		return true;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void addInstanceListener(JsDebuggerInstanceListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeInstanceListener(JsDebuggerInstanceListener listener) {
		// TODO Auto-generated method stub

	}

}
