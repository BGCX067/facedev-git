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
	
	private static volatile Boolean supported;

	/**
	 * This constructor is provided for extension.
	 * Clients should not call this constructor as they will get instantiation error.
	 */
	public IEJsDebugger() {
		if (supported != null) {
			throw new IllegalAccessError("You are not allowed to instantiate this class");
		}
		registerNatives();
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
			supported = initIEDriver();
		} catch (Throwable th) {
			supported = false;
		}
	}

	public String getName() {
		return "Internet Explorer";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#getRegisteredInstances()
	 */
	public List<JsDebuggerInstance> getRegisteredInstances() throws JsDebuggerException {
		if (!isSupported()) {
			throw new JsDebuggerException("IE Debugger is not loaded or not supported on this platform");
		}
		List<JsDebuggerInstance> result = new LinkedList<JsDebuggerInstance>();
		
		IEJsDebuggerInstance instance;
		while (fillIEInstance(instance = new IEJsDebuggerInstance())) {
			result.add(instance);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#isSupported()
	 */
	public boolean isSupported() {
		return supported != null && supported.booleanValue();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#dispose()
	 */
	public void dispose() {
		disposeIEDriver();
		supported = null;
	}

	private static native boolean initIEDriver();
	
	private static native void disposeIEDriver();

	private static native boolean fillIEInstance(IEJsDebuggerInstance ieJsDebuggerInstance);
}
