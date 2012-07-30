package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * Common interface for all token sources available in this implementation.
 * Token source is some object that may produce and iterate over produced tokens.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
interface TokenSource {
	
	/**
	 * @return current token returned by last iteration operation (either {@link #next()} or {@link #previous()}).
	 * Or returns <code>null</code> in case of tokenizer positioned at the either beginning or the end of the file.
	 */
	Token current();
	
	/**
	 * @return next token read from underline reader or <code>null</code> in case end of file is reached.
	 * @throws JsParseException in case of read error
	 */
	Token next() throws JsParseException;
	
	/**
	 * @return previous token stored in the internal cache or <code>null</code> in case of 
	 * beginning of the file is reached.
	 * @throws JsParseException in case of {@link #commit()} was invoked on current token.
	 */
	Token previous() throws JsParseException;
	
	/**
	 * Commits current token as the beginning of the internal cache. This provides additional 
	 * internal validation for the parser to avoid the same token be interpreted twice.
	 */
	void commit();
}
