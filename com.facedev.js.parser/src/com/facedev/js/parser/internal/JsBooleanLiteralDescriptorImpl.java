package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsBooleanLiteralDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Implementation for javascript boolean literal descriptor.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsBooleanLiteralDescriptorImpl implements
		JsBooleanLiteralDescriptor {
	
	private Token token;
	private boolean value;
	
	JsBooleanLiteralDescriptorImpl(Token token, boolean value) {
		this.token = token;
		this.value = value;
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
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsBooleanLiteralDescriptor#getValue()
	 */
	public boolean getValue() {
		return value;
	}

}
