package com.facedev.js.parser;

/**
 * Interface for loggers for non-critical parsing errors.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public interface JsParseLogger {
		
	public enum Message {
		SYNTAX_ERROR, 
		STATEMENT_HAS_NO_EFFECT, 
		WITH_STATEMENT;
	}
	
	/**
	 * Logs message of level specified.
	 * @param level
	 * @param message
	 * @param token
	 */
	void log(Message message, Token...tokens);
}
