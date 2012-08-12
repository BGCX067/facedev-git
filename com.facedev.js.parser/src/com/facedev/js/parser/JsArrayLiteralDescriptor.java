package com.facedev.js.parser;

import java.util.List;

/**
 * Descriptor for array literal in JSON notation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsArrayLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return unmodifiable list of array members.
	 */
	List<JsDescriptor> getValues();
}
