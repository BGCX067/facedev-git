package com.facedev.js.debug.ie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
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
	private static final Object ID = new Object();
	
	private static final String LIBRARY_NAME = "ie_debug_win32";
	private static Boolean supported;
	private static final Set<JsDebuggerInstanceListener> listeners = new CopyOnWriteArraySet<JsDebuggerInstanceListener>();
	
	private static volatile String name = "Internet Explorer";
	private static final AtomicInteger instancesCount = new AtomicInteger(0);
	
	private static volatile FutureTask<List<JsDebuggerInstance>> asynch;
	
	private static volatile DispatcherThread dispatcher;

	private static volatile IEJsDebugger singleton;

	/**
	 * This constructor is provided for extension.
	 * Clients should not call this constructor directly.
	 */
	public IEJsDebugger() {
		if (singleton != null) {
			throw new IllegalStateException("Multiple instances of IE debugger are not allowed");
		}
		singleton = this;
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
			if (supported) {
				resetAynch().run();
				dispatcher = new DispatcherThread();
				IEJsDebugger.supported = supported;
				dispatcher.start();
			} else {
				IEJsDebugger.supported = supported;
				singleton = null;
			}
			
		} catch (Throwable th) {
			supported = false;
			singleton = null;
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
		try {
			return asynch.get(15, TimeUnit.SECONDS);
		} catch(Exception ex) {
			throw new JsDebuggerException(ex);
		}
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
				dispatcher = null;
				singleton = null;
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
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#getID()
	 */
	public Object getID() {
		return ID;
	}

	private static FutureTask<List<JsDebuggerInstance>> resetAynch() {
		return asynch = new FutureTask<List<JsDebuggerInstance>>(
			new Callable<List<JsDebuggerInstance>>() {
	
				public List<JsDebuggerInstance> call() throws Exception {
					List<JsDebuggerInstance> result = new LinkedList<JsDebuggerInstance>();
					
					IEJsDebuggerInstance instance;
					int index = 0;
					while (fillIEInstance(instance = new IEJsDebuggerInstance(IEJsDebugger.singleton), index++)) {
						result.add(instance);
					}
					
					return result;
				}
			});
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
	
	private static native void resetIEDriver();
	
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
	
	private static enum NotifyOption {
		ADD {
			@Override
			void notify(JsDebuggerInstance instance,
					JsDebuggerInstanceListener listener) {
				listener.onInstanceAdd(instance);
				
			}
		}, 
		CHANGE {
			@Override
			void notify(JsDebuggerInstance instance,
					JsDebuggerInstanceListener listener) {
				listener.onInstanceChange(instance);
			}
		}, 
		REMOVE {
			@Override
			void notify(JsDebuggerInstance instance,
					JsDebuggerInstanceListener listener) {
				listener.onInstanceRemove(instance);
			}
		}, 
		UNKNOWN {
			@Override
			void notify(JsDebuggerInstance instance,
					JsDebuggerInstanceListener listener) {
				listener.onInstancesChanged();
			}
		};
		
		abstract void notify(JsDebuggerInstance instance, JsDebuggerInstanceListener listener);
		
		private void notifyListeners(JsDebuggerInstance instance) {
			for (JsDebuggerInstanceListener listener : listeners) {
				notify(instance, listener);
			}
			
		}
	}
	
	private static class DispatcherThread extends Thread {
		
		DispatcherThread() {
			setDaemon(true);
		}

		@Override
		public synchronized void run() {
			try {
				while(dispatcher == this) {
					wait(500);
					try {
						processLoop();
					} catch (ExecutionException ex) {
						ex.printStackTrace();
						// ignore so far
					}
				}
			} catch(InterruptedException ex) {
				// ignore & exit
			}
		}

		private void processLoop() throws InterruptedException, ExecutionException {
			if (!asynch.isDone()) {
				asynch.run();
			}
			List<JsDebuggerInstance> previous = asynch.get();
			resetIEDriver();
			resetAynch().run();
			checkEvents(previous, asynch.get());
		}

		private void checkEvents(List<JsDebuggerInstance> previous, List<JsDebuggerInstance> current) {
			if (previous == null || current == null) {
				return;
			}
			boolean changed = false;
			Map<Long, JsDebuggerInstance> idToInstance = new HashMap<Long, JsDebuggerInstance>(previous.size()*2);
			for (JsDebuggerInstance instance : previous) {
				IEJsDebuggerInstance ie = (IEJsDebuggerInstance)instance;
				idToInstance.put(ie.getID(), ie);
			}
			
			for (JsDebuggerInstance instance : current) {
				IEJsDebuggerInstance ie = (IEJsDebuggerInstance)instance;
				JsDebuggerInstance old = idToInstance.remove(ie.getID());
				if (old == null) {
					NotifyOption.ADD.notifyListeners(ie);
					changed = true;
				} else if (!old.getName().equals(instance.getName())) {
					NotifyOption.CHANGE.notifyListeners(ie);
					changed = true;
				}
			}
			for (JsDebuggerInstance instance : idToInstance.values()) {
				NotifyOption.REMOVE.notifyListeners(instance);
				changed = true;
			}
			if (changed) {
				NotifyOption.UNKNOWN.notifyListeners(null);
			}
		}
	}
}
