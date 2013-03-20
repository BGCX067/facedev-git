package com.facedev.js.parser;

import java.io.Reader;
import java.net.URI;

import com.facedev.js.parser.internal.JsAstParser;

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
	 * @param reader
	 * @return new parser instance.
	 * @throws JsParseException in case of serious trouble.
	 */
	public static JsParser create(URI resource) throws JsParseException {
		return new JsAstParser(resource);
	}

	/**
	 * Parses file and logs all necessary messages in the logger passed.
	 * @param logger
	 * @return {@link JsCompilationUnit} read from source.
	 * @throws JsParseException in case of serious trouble.
	 */
	public abstract JsCompilationUnit parse(JsParseLogger logger) throws JsParseException;
}
