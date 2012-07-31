package com.facedev.js.debug.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DebuggerActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		DebuggersManager.getInstance().initialize();
	}

	public void stop(BundleContext context) throws Exception {
		DebuggersManager.getInstance().destroy();
	}

}
