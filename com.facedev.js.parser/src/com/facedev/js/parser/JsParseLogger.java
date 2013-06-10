package com.facedev.js.parser;

/**
 * Interface for loggers for non-critical parsing errors.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public interface JsParseLogger {
		
	public enum Message {
		SYNTAX_ERROR(true), 
		STATEMENT_HAS_NO_EFFECT(false), 
		WITH_STATEMENT(false);
		
		private final boolean critical;
		
		private Message(boolean critical) {
			this.critical = critical;
		}

		public boolean isCritical() {
			return critical;
		}
	}
	
	/**
	 * Logs message of level specified.
	 * Note: implementations should avoid to throw JsParseException unless something really critical occurs.
	 * @param level
	 * @param message
	 * @param token
	 * @throws JsParseException if critical error occurs.
	 */
	void log(Message message, Token...tokens) throws JsParseException;
}
