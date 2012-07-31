package com.facedev.js.debug.ie;

import java.util.LinkedList;
import java.util.List;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;

/**
 * Internet explorer debugger entry point class.
 * Provides interface to native debugger implementation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class IEJsDebugger implements JsDebugger {
	
	private static Boolean supported;
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#getRegisteredInstances()
	 */
	public List<JsDebuggerInstance> getRegisteredInstances() throws JsDebuggerException {
		registerNatives();
		if (!isSupported()) {
			throw new JsDebuggerException("IE Debugger is not loaded or not supported on this platform");
		}
		List<JsDebuggerInstance> result = new LinkedList<JsDebuggerInstance>();
		for (int i = getRegisteredInstancesCount(); i > 0; i--) {
			result.add(new IEJsDebuggerInstance());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#isSupported()
	 */
	public boolean isSupported() {
		registerNatives();
		return supported != null && supported.booleanValue();
	}
	
	private static synchronized void registerNatives() {
		if (Activator.getContext() == null) {
			supported = null;
			return; // not started yet
		}
		if (supported != null) {
			return; // already registered
		}
		try {
			System.load("./native/ie_debug_win32.dll");
			supported = true;
		} catch (Throwable th) {
			supported = false;
		}
	}

	private static native int getRegisteredInstancesCount();
}
