package com.facedev.js.parser;

import java.io.Reader;

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
	public JsParser create(Reader reader) throws JsParseException {
		return new JsAstParser(reader);
	}

	/**
	 * Parses file and logs all necessary messages in the logger passed.
	 * @param logger
	 * @return {@link JsCompilationUnitDescriptor} read from source.
	 * @throws JsParseException in case of serious trouble.
	 */
	public abstract JsCompilationUnitDescriptor parse(JsParseLogger logger) throws JsParseException;
}
