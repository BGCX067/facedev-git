package com.facedev.js.parser;

/**
 * Represents variable declaration (started from 'var' keyword).
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsVariableDescriptor extends JsDescriptor {
	
	/**
	 * @return name of this variable as {@link JsIdentifierDescriptor}.
	 */
	JsIdentifierDescriptor getIdentifier();
}
