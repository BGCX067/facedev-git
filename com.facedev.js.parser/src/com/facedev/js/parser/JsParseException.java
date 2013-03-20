package com.facedev.js.parser;

import java.io.IOException;

/**
 * Core exception that may be thrown when javascript parsing fails critically.
 * Note that this exception represents non-recoverable error in parsing and
 * not equivalent to javascript SyntaxError which is typically handled 
 * by {@link JsParseLogger}.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsParseException extends Exception {
	private static final long serialVersionUID = 4362879734758069891L;

	public JsParseException(String message) {
		super(message);
	}

	public JsParseException(IOException ex) {
		super(ex);
	}
	
}
