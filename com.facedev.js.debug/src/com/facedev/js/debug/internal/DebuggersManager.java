package com.facedev.js.debug.internal;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * Singleton for managing debuggers instances.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class DebuggersManager implements BundleListener {
	
	private static DebuggersManager instance;
	
//	private List<JsDebugger> debuggers;

	private DebuggersManager(){
//		debuggers = new ArrayList<JsDebugger>();
	}
	
	public static DebuggersManager getInstance() {
		if (instance == null) {
			instance = new DebuggersManager();
		}
		return instance;
	}

	/**
	 * Initializes all debuggers registered with appropriate extension.
	 */
	void initialize() {
		
	}

	/**
	 * Disposes any resources acquired by debuggers.
	 */
	void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void bundleChanged(BundleEvent event) {
		if (event.getType() == BundleEvent.STARTED) {
			
		} else if (event.getType() == BundleEvent.STOPPING) {
			
		}
	}

}
