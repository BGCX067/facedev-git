package com.facedev.js.debug.safari;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggerInstanceListener;

public class SafariJsDebugger implements JsDebugger {
	
	private static final Object LOCK = new Object();
	private static final String LIBRARY_NAME = "safari_debug_mac";
	
	private static final List<JsDebuggerInstanceListener> listeners = 
			new CopyOnWriteArrayList<JsDebuggerInstanceListener>();
	
	private static volatile SafariJsDebugger singletonInstance;
	
	private static volatile FutureTask<List<JsDebuggerInstance>> instances;
	
	private static volatile String name = "Safari";
	
	private static volatile Boolean supported;
	
	private static volatile DispatcherThread dispatcher;

	public SafariJsDebugger() {
		synchronized(LOCK) {
			if (singletonInstance != null) {
				throw new IllegalStateException();
			}
			singletonInstance = this;
			
			registerNatives();
		}
	}
	
	private static void registerNatives() {
		if (supported != null) {
			// already registered
			return;
		}
		try {
			System.loadLibrary(LIBRARY_NAME);
			boolean supported = initSafariDriver();
			name = getDriverName();
			refreshInstances();
			dispatcher = new DispatcherThread();
			dispatcher.start();
			SafariJsDebugger.supported = supported;
		} catch (UnsatisfiedLinkError ex) {
			supported = Boolean.FALSE;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#getRegisteredInstances()
	 */
	public List<JsDebuggerInstance> getRegisteredInstances()
			throws JsDebuggerException {
		if (!isSupported() || instances == null) {
			throw new JsDebuggerException("Safari Debugger is not loaded or not supported on this platform");
		}
		try {
			return instances.get();
		} catch (ExecutionException ex) {
			throw new JsDebuggerException("Unable to get instances", ex);
		} catch (InterruptedException ex) {
			throw new JsDebuggerException("Unable to get instances", ex);
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
	 * @see com.facedev.js.debug.JsDebugger#isSupported()
	 */
	public boolean isSupported() {
		return supported != null && supported;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#dispose()
	 */
	public void dispose() {
		synchronized(LOCK) {
			listeners.clear();
			supported = null;
			singletonInstance = null;
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
	
	private static void refreshInstances() {
		instances = new FutureTask<List<JsDebuggerInstance>>(
				new Callable<List<JsDebuggerInstance>>() {

			public List<JsDebuggerInstance> call() throws Exception {
				List<JsDebuggerInstance> result = new ArrayList<JsDebuggerInstance>();
				
				int index = 0;
				SafariJsDebuggerInstance instance;
				while (fillSafariInstance(instance = new SafariJsDebuggerInstance(), index++)) {
					result.add(instance);
				}
					
				return Collections.unmodifiableList(result);
			}
			
		});
	}

	private static native boolean initSafariDriver();
	
	private static native void disposeSafariDriver();
	
	private static native boolean fillSafariInstance(SafariJsDebuggerInstance instance, int index);
	
	private static native String getDriverName();
	
	private static class DispatcherThread extends Thread {
		
		DispatcherThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (this == dispatcher) {
				try {
					refresh();
				} catch (InterruptedException ex) {
					// ignore & return
					return;
				} catch (ExecutionException ex) {
					ex.printStackTrace();
					return;
				}
			}
		}

		private synchronized void refresh() throws InterruptedException, ExecutionException {
			wait(500);
			instances.run();
			List<JsDebuggerInstance> previous = instances.get();
			refreshInstances();
			instances.run();
			List<JsDebuggerInstance> current = instances.get();
			notifyListeners(previous, current);
		}

		private void notifyListeners(List<JsDebuggerInstance> previous,
				List<JsDebuggerInstance> current) {
			// TODO Auto-generated method stub
			
		}
	}
}
