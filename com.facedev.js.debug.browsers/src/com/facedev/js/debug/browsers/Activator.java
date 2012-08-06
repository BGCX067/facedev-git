package com.facedev.js.debug.browsers;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.facedev.js.debug.JsDebuggersManager;

/**
 * Activator class for this plugin.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class Activator implements BundleActivator {
	
	private static BundleContext context;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}

	/**
	 * @return {@link BundleContext} associated with this bundle.
	 */
	public static BundleContext getBundleContext() {
		return context;
	}
	
	/**
	 * @return {@link JsDebuggersManager} service instance.
	 */
	public static JsDebuggersManager getDebuggerManager() {
		if (context == null) {
			return null;
		}
		ServiceReference<JsDebuggersManager> ref = context.getServiceReference(JsDebuggersManager.class);
		
		if (ref == null) {
			return null;
		}
		return context.getService(ref);
	}
}
