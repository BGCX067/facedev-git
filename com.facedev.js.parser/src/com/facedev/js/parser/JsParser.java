package com.facedev.js.parser;

import java.net.URI;

import com.facedev.js.parser.internal.JsAnalyser;

/**
 * Abstract entry point for javascript parser.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public abstract class JsParser {
	
	protected JsParser() {
	}
	
	/**
	 * Creates parser based on reader.
	 * 
	 * @param resource
	 * @param charsetName
	 * @return JsParser instance
	 * @throws JsParseException in case of serious trouble.
	 */
	public static JsParser create(URI resource, String charsetName) throws JsParseException {
		return new JsAnalyser(resource, charsetName);
	}

	/**
	 * Parses file and logs all necessary messages in the logger passed.
	 * @param logger
	 * @return {@link JsCompilationUnit} read from source.
	 * @throws JsParseException in case of serious trouble.
	 */
	public abstract JsCompilationUnit parse(JsParseLogger logger) throws JsParseException;
}
