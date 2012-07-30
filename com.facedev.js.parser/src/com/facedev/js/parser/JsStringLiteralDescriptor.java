package com.facedev.js.parser;

/**
 * Represents string literals of javascript language.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public interface JsStringLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return value of this literal (without quotes).
	 */
	String getValue();
}
