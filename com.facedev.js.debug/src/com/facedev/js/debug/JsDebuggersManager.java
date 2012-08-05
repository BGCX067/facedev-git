package com.facedev.js.debug;

import java.util.List;

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

}
