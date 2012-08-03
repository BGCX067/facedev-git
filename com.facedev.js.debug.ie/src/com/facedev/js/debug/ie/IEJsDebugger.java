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


	public String getName() {
		return "Internet Explorer";
	}
	
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
		for (int i = 1/*getRegisteredInstancesCount()*/; i > 0; i--) {
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
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#dispose()
	 */
	public void dispose() {
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
			System.loadLibrary("ie_debug_win32");
			supported = true;
		} catch (Throwable th) {
			supported = true;
		}
	}

	private static native int getRegisteredInstancesCount();
}
