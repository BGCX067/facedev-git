package com.facedev.js.parser;

import java.util.Map;

/**
 * Represents object literal (JSON format).
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsObjectLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return unmodifiable map of keys and associated values.
	 */
	Map<String, JsDescriptor> getValues();
}
