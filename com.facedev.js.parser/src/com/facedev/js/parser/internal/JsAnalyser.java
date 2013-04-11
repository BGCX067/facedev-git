package com.facedev.js.parser.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import com.facedev.js.parser.JsCompilationUnit;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsParser;

/**
 * Entry point for {@link JsParser} implementation. Uses several modules for javascript code analysis:
 *  - {@link JsTokenizer} splits input stream on javascript tokens;
 *  - {@link JsAstParser} builds javascript abstract syntax tree;
 * 
 * Instances of this class are not thread safe, so should be used only in 
 * single thread. 
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsAnalyser extends JsParser {
	
	private URI uri;
	private String charsetName;
	private JsParseLogger logger;

	public JsAnalyser(URI uri, String charsetName) {
		this.uri = uri;
		this.charsetName = charsetName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsParser#parse(com.facedev.js.parser.JsParseLogger)
	 */
	@Override
	public JsCompilationUnit parse(JsParseLogger logger) throws JsParseException {
		this.logger = logger;
		try {
			return parse();
		} catch (IOException ex) {
			throw new JsParseException(ex);
		}
		
	}

	private JsCompilationUnit parse() throws 
	        IOException, JsParseException {
		InputStream stream = null;
		try {
			stream = uri.toURL().openStream();
			JsTokenizer tokenizer = new JsTokenizer(new InputStreamReader(stream, charsetName));
			return parse(tokenizer);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private JsCompilationUnit parse(JsTokenizer tokenizer) throws IOException, JsParseException {
		JsAstParser parser = new JsAstParser(tokenizer, logger);
		parser.parse();
		return null;
	}
}
