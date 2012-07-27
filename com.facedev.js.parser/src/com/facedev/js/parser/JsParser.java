package com.facedev.js.parser;

import java.io.Reader;
import java.util.List;

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
		return null;
	}

	/**
	 * Parses file and logs all necessary messages in the logger passed.
	 * @param logger
	 * @return list of descriptors read from source.
	 * @throws JsParseException in case of serious trouble.
	 */
	public abstract List<JsDescriptor> parse(JsParseLogger logger);
}
