package com.facedev.js.debug;

import java.util.List;

import org.eclipse.core.runtime.IExtension;

/**
 * Interface provides access to registered debuggers.
 * Clients can access instances of this interface with OSGi service.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDebuggersManager {

	/**
	 * @return unmodifiable list of {@link JsDebugger}s registered on current time.
	 */
	List<JsDebugger> getDebuggers();

	/**
	 * @param debugger
	 * @return extension associated with passed debugger or <code>null</code> if there is no extension.
	 */
	IExtension getExtension(JsDebugger debugger);
	
	/**
	 * Adds listener to this manager. Listener will be notified when debuggers are changed.
	 * @param listener
	 */
	void addJsDebuggerChangeListener(JsDebuggerChangeListener listener);
	
	/**
	 * Removes previously added listener.
	 * @param listener
	 */
	void removeJsDebuggerChangeListener(JsDebuggerChangeListener listener);
}
