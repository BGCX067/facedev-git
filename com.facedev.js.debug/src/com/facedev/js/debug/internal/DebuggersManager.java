package com.facedev.js.debug.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerChangeListener;
import com.facedev.js.debug.JsDebuggersManager;
import com.facedev.utils.OSGiUtils;

/**
 * Singleton for managing debuggers instances.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class DebuggersManager implements BundleListener, JsDebuggersManager {
	
	private static final String CLASS_NAME_PROPERTY = "class";

	private static final String JS_DEBUG_EXTENSION_POINT = "com.facedev.js.debug";

	private static DebuggersManager instance;
	
	private Map<String, JsDebugger> debuggers;
	
	private Map<JsDebugger, IExtension> extensions;
	
	private List<JsDebuggerChangeListener> listeners;

	private DebuggersManager(){
		debuggers = new HashMap<String, JsDebugger>();
		extensions = new HashMap<JsDebugger, IExtension>();
	}
	
	/**
	 * @return <i>singleton</i> instance of DebuggersManager.
	 */
	public static DebuggersManager getInstance() {
		if (instance == null) {
			instance = new DebuggersManager();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggersManager#getDebuggers()
	 */
	public List<JsDebugger> getDebuggers() {
		ArrayList<JsDebugger> result = new ArrayList<JsDebugger>(debuggers.values());
		result.trimToSize();
		return Collections.unmodifiableList(result);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.debug.JsDebuggersManager#getExtension(com.facedev.js.debug.JsDebugger)
	 */
	public IExtension getExtension(JsDebugger debugger) {
		return extensions.get(debugger);
	}

	/**
	 * Disposes any resources acquired by debuggers.
	 */
	void destroy() {
		removeDebuggers(null);
	}

	public void bundleChanged(BundleEvent event) {
		if (event.getBundle().getBundleId() == DebuggerActivator.getBundle().getBundleId() &&
				event.getType() == BundleEvent.STARTED) {
			addDebuggers(null);
			return;
		}
		
		if (event.getType() == BundleEvent.STARTED) {
			addDebuggers(event.getBundle());
		} else if (event.getType() == BundleEvent.STOPPING) {
			removeDebuggers(event.getBundle());
		}
	}

	public void addJsDebuggerChangeListener(JsDebuggerChangeListener listener) {
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<JsDebuggerChangeListener>();
		}
		listeners.add(listener);
	}

	public void removeJsDebuggerChangeListener(JsDebuggerChangeListener listener) {
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}

	private void addDebuggers(Bundle bundle) {
		IExtension[] extensions = getExtensionsForBundle(bundle);
		if (extensions == null || extensions.length == 0) {
			return;
		}
		
		for (IExtension extension : extensions) {
			addDebugger(extension);
		}
	}

	private void removeDebuggers(Bundle bundle) {
		IExtension[] extensions = getExtensionsForBundle(bundle);
		if (extensions == null || extensions.length == 0) {
			return;
		}
		
		for (IExtension extension : extensions) {
			removeDebugger(extension);
		}
	}

	private IExtension[] getExtensionsForBundle(Bundle bundle) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(JS_DEBUG_EXTENSION_POINT);
		if (point == null) {
			return null;
		}
		IExtension[] extensions = point.getExtensions();
		if (bundle == null) {
			return extensions;
		}
		
		List<IExtension> result = new LinkedList<IExtension>();
		
		for (IExtension extension : extensions) {
			Bundle contributor = OSGiUtils.getBundle(extension);
			if (contributor != null && contributor.getBundleId() == bundle.getBundleId()) {
				result.add(extension);
			}
		}
		
		return result.toArray(new IExtension[result.size()]);
	}

	private void addDebugger(IExtension extension) {
		String uniqueId = getId(extension);
		if (uniqueId == null || debuggers.get(uniqueId) != null) {
			return; // already registered
		}
		try {
			JsDebugger debugger = (JsDebugger) extension.getConfigurationElements()[0].createExecutableExtension(CLASS_NAME_PROPERTY);
			if (!debugger.isSupported()) {
				return;
			}
			debuggers.put(uniqueId, debugger);
			extensions.put(debugger, extension);
			notifyListeners(debugger, JsDebuggerChangeListener.State.ADDED);
		} catch (CoreException ex) {
			DebuggerActivator.getLog().log(new Status(Status.ERROR, extension.getContributor().getName(), 
					Status.ERROR, "Unable to initialize extension: " + uniqueId, ex));
		}
	}

	private void removeDebugger(IExtension extension) {
		String uniqueId = getId(extension);
		if (uniqueId == null || debuggers.get(uniqueId) == null) {
			return; // already removed
		}
		JsDebugger debugger = debuggers.remove(uniqueId);
		extensions.remove(debugger);
		notifyListeners(debugger, JsDebuggerChangeListener.State.REMOVED);
		debugger.dispose();
	}

	private void notifyListeners(JsDebugger debugger, JsDebuggerChangeListener.State state) {
		if (listeners == null) {
			return;
		}
		for (JsDebuggerChangeListener listener : listeners) {
			listener.onJsDebuggerChange(debugger, state);
		}
	}

	private String getId(IExtension extension) {
		if (extension.getUniqueIdentifier() != null) {
			return extension.getUniqueIdentifier();
		}
		if (extension.getConfigurationElements().length == 0) {
			return null; // not a valid extension
		}
		String clz = extension.getConfigurationElements()[0].getAttribute(CLASS_NAME_PROPERTY);
		if (clz == null || clz.trim().length() == 0) {
			return null;
		}
		return extension.getContributor().getName() + "#" + clz;
	}
}
