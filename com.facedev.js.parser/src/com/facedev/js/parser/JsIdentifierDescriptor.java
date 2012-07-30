package com.facedev.js.parser;

/**
 * Represents javascript identifier (name of variable or property).
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public interface JsIdentifierDescriptor extends JsDescriptor {
	
	/**
	 * @return identifier name of this descriptor
	 */
	String getIdentifier();
}
