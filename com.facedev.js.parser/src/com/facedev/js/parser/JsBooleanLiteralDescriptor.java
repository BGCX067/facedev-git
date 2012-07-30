package com.facedev.js.parser;

/**
 * Represents boolean value literal of javascript language.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsBooleanLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return value of this boolean literal.
	 */
	boolean getValue();
}
