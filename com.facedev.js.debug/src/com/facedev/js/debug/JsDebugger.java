package com.facedev.js.debug;

import java.util.List;

/**
 * Common interface for all debuggers that may be registered as extensions of this bundle.
 * This class should not redefine equals and hash code since this may lead to unpredictable behavior.
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
	 * @return name of this debugger (usually corresponds to the name of browser with version).
	 */
	String getName();
	
	/**
	 * @return <code>true</code> if this debugger is supported on current platform.
	 */
	boolean isSupported();
	
	/**
	 * Disposes resources associated with this debugger if any.
	 * Used for cleanup when this plugin is going to shutdown.
	 */
	void dispose();
	
	/**
	 * Adds {@link JsDebuggerInstanceListener} to this debugger.
	 * @param listener
	 */
	void addInstanceListener(JsDebuggerInstanceListener listener);
	
	/**
	 * Removes previously registered instance listener from this debugger.
	 * @param listener
	 */
	void removeInstanceListener(JsDebuggerInstanceListener listener);
}
