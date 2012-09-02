package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsNullLiteralDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Implementation for <code>null</code> literal descriptor of javascript language.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsNullLiteralDescriptorImpl implements JsNullLiteralDescriptor {
	
	private Token token;

	JsNullLiteralDescriptorImpl(Token token) {
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
	}
}
