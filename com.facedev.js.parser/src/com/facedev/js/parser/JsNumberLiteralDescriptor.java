package com.facedev.js.parser;

/**
 * Represents javascript number literals.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsNumberLiteralDescriptor extends JsDescriptor {
	
	/**
	 * @return value of this literal as double precision floating point.
	 */
	double getValue();
}
