package com.facedev.js.debug.internal;

import java.util.Hashtable;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.facedev.js.debug.JsDebuggersManager;

/**
 * Activates all registered debuggers and makes this session active.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class DebuggerActivator implements BundleActivator {
	
	private static ILog log;
	
	private static Bundle bundle;

	public void start(BundleContext context) throws Exception {
		log = Platform.getLog(context.getBundle());
		bundle = context.getBundle();
		
		context.addBundleListener(DebuggersManager.getInstance());
		
		context.registerService(JsDebuggersManager.class, DebuggersManager.getInstance(), new Hashtable<String, Object>());
	}

	public void stop(BundleContext context) throws Exception {
		context.ungetService(context.getServiceReference(JsDebuggersManager.class));
		DebuggersManager.getInstance().destroy();
		log = null;
		
	}

	public static ILog getLog() {
		return log;
	}

	public static Bundle getBundle() {
		return bundle;
	}
}
