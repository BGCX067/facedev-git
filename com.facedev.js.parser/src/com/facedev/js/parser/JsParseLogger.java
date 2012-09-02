package com.facedev.js.parser;

/**
 * Interface for loggers for non-critical parsing errors.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public interface JsParseLogger {
	
	/**
	 * Level of the logging message.
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	public enum Level {
		INFO,
		WARN,
		ERROR
	}
	
	/**
	 * Logs message of level specified for single token.
	 * @param level
	 * @param message
	 * @param token
	 */
	void log(Level level, String message, Token token);
	
	/**
	 * Logs message of level for descriptor.
	 * @param level
	 * @param message
	 * @param descriptor
	 */
	void log(Level level, String message, JsDescriptor descriptor);
}
