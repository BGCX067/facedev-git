package com.facedev.js.debug.ie;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggerInstanceListener;

/**
 * Internet explorer debugger entry point class.
 * Provides interface to native debugger implementation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class IEJsDebugger implements JsDebugger {
	
	private static final String LIBRARY_NAME = "ie_debug_win32";
	private static Boolean supported;
	private static final Set<JsDebuggerInstanceListener> listeners = new CopyOnWriteArraySet<JsDebuggerInstanceListener>();
	
	private static volatile String name = "Internet Explorer";
	private static final AtomicInteger instancesCount = new AtomicInteger(0);

	/**
	 * This constructor is provided for extension.
	 * Clients should not call this constructor directly.
	 */
	public IEJsDebugger() {
		registerNatives();
		instancesCount.incrementAndGet();
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
			System.loadLibrary(LIBRARY_NAME);
			boolean supported = initIEDriver();
			name = getDebuggerName();
			IEJsDebugger.supported = supported;
		} catch (Throwable th) {
			supported = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#getName()
	 */
	public String getName() {
		return name;
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
		int index = 0;
		while (fillIEInstance(instance = new IEJsDebuggerInstance(), index++)) {
			result.add(instance);
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#isSupported()
	 */
	public boolean isSupported() {
		synchronized (IEJsDebugger.class) {
			return supported != null && supported.booleanValue();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#dispose()
	 */
	public void dispose() {
		synchronized (IEJsDebugger.class) {
			if (instancesCount.decrementAndGet() > 0) {
				return;
			}
			listeners.clear();
			if (isSupported()) {
				disposeIEDriver();
				supported = null;
			}
		}
	}
	
	/*
	 * This method is called from driver to notify java part about changes
	 */
	private static void notifyListeners(int index, boolean remove) {
		IEJsDebuggerInstance instance = new IEJsDebuggerInstance();
		if (!fillIEInstance(instance, index)) {
			return;
		}
		for (JsDebuggerInstanceListener listener : listeners) {
			if (remove) {
				listener.onInstanceRemove(instance);
			} else {
				listener.onInstanceAdd(instance);
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#addInstanceListener(com.facedev.js.debug.JsDebuggerInstanceListener)
	 */
	public void addInstanceListener(JsDebuggerInstanceListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#removeInstanceListener(com.facedev.js.debug.JsDebuggerInstanceListener)
	 */
	public void removeInstanceListener(JsDebuggerInstanceListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Initializes Internet Explorer driver.
	 * @return <code>true</code> if initialization was successful and <code>false</code> otherwise.
	 */
	private static native boolean initIEDriver();
	
	/**
	 * Disposes any resources associated with Internet Explorer driver.
	 */
	private static native void disposeIEDriver();
	
	/**
	 * @return name of the browser associated with this driver.
	 */
	private static native String getDebuggerName();

	/**
	 * Fills instance of debugger that matches specific Internet Explorer window or tab.
	 * @param instance to fill
	 * @param index of instance to fill
	 * @return <code>true</code> if instance at index specified exists and <code>false</code> otherwise.
	 */
	private static native boolean fillIEInstance(IEJsDebuggerInstance instance, int index);
}
