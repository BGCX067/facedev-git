package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsRegexpLiteralDescriptor;
import com.facedev.js.parser.Token;

/**
 * Implementation for regular expression literal descriptor.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsRegexpLiteralDescriptorImpl implements JsRegexpLiteralDescriptor {
	
	private Token token;

	JsRegexpLiteralDescriptorImpl(Token token) {
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#getTokens()
	 */
	public List<Token> getTokens() {
		return Collections.singletonList(token);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#validate(com.facedev.js.parser.JsParseLogger)
	 */
	public void validate(JsParseLogger logger) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsRegexpLiteralDescriptor#getValue()
	 */
	public String getValue() {
		return token.toString();
	}

}
