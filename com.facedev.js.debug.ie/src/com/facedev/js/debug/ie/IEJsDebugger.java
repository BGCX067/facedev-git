package com.facedev.js.debug.ie;

import java.util.Collection;
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

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggerInstanceListener;
import com.facedev.utils.PollingService;
import com.facedev.utils.PollingService.PollingTask;

/**
 * Internet explorer debugger entry point class.
 * Provides interface to native debugger implementation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class IEJsDebugger implements JsDebugger {
	private static final Object ID = new Object();
	private static final Object LOCK = new Object();
	
	private static final String LIBRARY_NAME = "ie_debug_win32";
	private static Boolean supported;
	private static final Set<JsDebuggerInstanceListener> listeners = new CopyOnWriteArraySet<JsDebuggerInstanceListener>();
	
	private static volatile String name = "Internet Explorer";

	private static volatile FutureTask<List<JsDebuggerInstance>> asynch;
	
	private static volatile PollingTask dispatcher;

	private static volatile IEJsDebugger singleton;

	/**
	 * This constructor is provided for extension.
	 * Clients should not call this constructor directly.
	 */
	public IEJsDebugger() {
		synchronized(LOCK) {
			if (singleton != null) {
				throw new IllegalStateException("Multiple instances of IE debugger are not allowed");
			}
			singleton = this;
			registerNatives();
		}
	}
	
	private static void registerNatives() {
		if (supported != null) {
			return; // already registered
		}
		try {
			System.loadLibrary(LIBRARY_NAME);
			boolean supported = initIEDriver();
			name = getDebuggerName();
			if (supported) {
				resetAynch().run();
				dispatcher = new Dispatcher();
				PollingService.getInstance().schedule(dispatcher, 500L);
				IEJsDebugger.supported = supported;
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
		synchronized (LOCK) {
			return supported != null && supported.booleanValue();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebugger#dispose()
	 */
	public void dispose() {
		synchronized (LOCK) {
			listeners.clear();
			if (isSupported()) {
				disposeIEDriver();
			}
			supported = null;
			dispatcher = null;
			singleton = null;
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
	
	/**
	 * Resets IE driver and reloads all the instances.
	 */
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
	
	private static class Dispatcher implements PollingTask {
		
		public Boolean call() throws Exception {
			if (dispatcher != this) {
				// disposed:
				return false;
			}
			synchronized(LOCK) {
				processLoop();
			}
			return null;
		}

		private void processLoop() throws InterruptedException, ExecutionException {
			asynch.run();
			List<JsDebuggerInstance> previous = asynch.get();
			resetIEDriver();
			resetAynch().run();
			checkEvents(previous, asynch.get());
		}

		private void checkEvents(List<JsDebuggerInstance> previous, List<JsDebuggerInstance> current) {
			if (previous == null || current == null) {
				return;
			}
			Map<Long, JsDebuggerInstance> idToInstance = getIdToInstanceMap(previous);
			
			boolean isAddedOrChanged = checkAddedOrChanged(current, idToInstance);
			if (checkRemoved(idToInstance.values()) || isAddedOrChanged) {
				NotifyOption.UNKNOWN.notifyListeners(null);
			}
		}

		private boolean checkRemoved(Collection<JsDebuggerInstance> values) {
			for (JsDebuggerInstance instance : values) {
				NotifyOption.REMOVE.notifyListeners(instance);
			}
			return !values.isEmpty();
		}

		private boolean checkAddedOrChanged(List<JsDebuggerInstance> current, Map<Long, JsDebuggerInstance> idToInstance) {
			boolean isAddedOrChanged = false;
			for (JsDebuggerInstance instance : current) {
				if (checkAddedOrChanged(instance, idToInstance.remove(instance.getID()))) {
					isAddedOrChanged = true;
				}
			}
			return isAddedOrChanged;
		}

		private boolean checkAddedOrChanged(JsDebuggerInstance current, JsDebuggerInstance old) {
			if (old == null) {
				NotifyOption.ADD.notifyListeners(current);
				return true;
			} else if (!old.getName().equals(current.getName())) {
				NotifyOption.CHANGE.notifyListeners(current);
				return true;
			}
			return false;
		}

		private Map<Long, JsDebuggerInstance> getIdToInstanceMap(List<JsDebuggerInstance> previous) {
			Map<Long, JsDebuggerInstance> result = new HashMap<Long, JsDebuggerInstance>(previous.size() * 2);
			for (JsDebuggerInstance instance : previous) {
				IEJsDebuggerInstance ie = (IEJsDebuggerInstance)instance;
				result.put(ie.getID(), ie);
			}
			return result;
		}
	}
}
