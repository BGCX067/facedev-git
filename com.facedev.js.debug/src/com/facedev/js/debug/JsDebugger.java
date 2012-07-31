package com.facedev.js.debug;

import java.util.List;

/**
 * Common interface for all debuggers that may be registered as extensions of this bundle.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDebugger {
	
	/**
	 * Provides unmodifiable list of debugger instances assigned to this debugger.
	 * Each debugger should handle its own instances in implementation-specific way.
	 * Usually this are browser application instances launched on the current platform.
	 * 
	 * @return unmodifiable list of debugger instances assigned to this debugger.
	 * @throws JsDebuggerException in case of troubles
	 */
	List<JsDebuggerInstance> getRegisteredInstances() throws JsDebuggerException;
	
	/**
	 * @return <code>true</code> if this debugger is supported on current platform.
	 */
	boolean isSupported();
}
