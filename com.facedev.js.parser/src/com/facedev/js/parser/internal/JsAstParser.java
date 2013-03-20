package com.facedev.js.parser.internal;

import java.io.Reader;
import java.net.URI;

import com.facedev.js.parser.JsCompilationUnit;
import com.facedev.js.parser.JsSyntaxNode;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsParser;

/**
 * Abstract syntax tree (AST) based implementation of javascript parser.
 * Instances of this class are not thread safe, so should be used only in 
 * single thread. 
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsAstParser extends JsParser {
	
	private URI uri;
	private JsParseLogger logger;

	public JsAstParser(URI uri) {
		this.uri = uri;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsParser#parse(com.facedev.js.parser.JsParseLogger)
	 */
	@Override
	public JsCompilationUnit parse(JsParseLogger logger) throws JsParseException {
		TokenSource source = createTokenSource();
		setLogger(logger);
		return null;
	}

	/**
	 * @return new token source for the reader associated
	 */
	TokenSource createTokenSource() {
		return null;//new CommentsTokenFilter(new Tokenizer(uri.toURL().openStream()));
	}

	/**
	 * @return logger associated with this parser.
	 */
	JsParseLogger getLogger() {
		return logger;
	}
	
	void setLogger(JsParseLogger logger) {
		this.logger = logger;
	}
}
