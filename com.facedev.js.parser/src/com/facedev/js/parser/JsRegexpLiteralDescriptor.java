package com.facedev.js.parser;

/**
 * Represents regular expression literal in javascript language.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsRegexpLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return string representation of this reqular expression literal.
	 */
	String getValue();
}
