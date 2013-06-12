package com.facedev.js.parser;

import java.util.List;

/**
 * Common interface for all nodes in abstract syntax tree of javascript document.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsSyntaxNode {
	
	/**
	 * @return unmodifiable list of tokens associated with this descriptor.
	 */
	List<Token> getTokens();
	
	/**
	 * Validates this descriptor and logs messages using logger passed.
	 * 
	 * @param logger to log the validation messages
	 */
	void validate(JsParseLogger logger);
}
