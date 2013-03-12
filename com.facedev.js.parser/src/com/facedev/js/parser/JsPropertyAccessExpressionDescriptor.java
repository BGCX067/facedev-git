package com.facedev.js.parser;

/**
 * This interface represents member or property access construction of javascript language.
 * This construction is represented as <code>target.identifier</code> expression.
 * It is described as part of the member expression in chapter 11.2 of ECMA-262 specification.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsPropertyAccessExpressionDescriptor extends JsExpressionDescriptor {
	
	/**
	 * @return expression that creates target object that is the holder of property
	 * denoted by {@link #getIdentifier()} method.  
	 */
	JsDescriptor getTarget();
	
	/**
	 * @return identifier that holds name of the property to access.
	 */
	JsIdentifierDescriptor getIdentifier();
}
