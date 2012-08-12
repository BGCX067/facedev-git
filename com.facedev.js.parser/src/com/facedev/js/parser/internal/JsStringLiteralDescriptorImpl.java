package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsStringLiteralDescriptor;
import com.facedev.js.parser.Token;

/**
 * Implementation of {@link JsStringLiteralDescriptor}.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsStringLiteralDescriptorImpl implements JsStringLiteralDescriptor {
	
	private Token string;
	private String value;
	
	JsStringLiteralDescriptorImpl(Token string) {
		this.string = string;
		this.value = (String) string.subSequence(1, string.length() - 1);
	}

	public List<Token> getTokens() {
		return Collections.singletonList(string);
	}

	public void validate(JsParseLogger logger) {
	}

	public String getValue() {
		return value;
	}

}
