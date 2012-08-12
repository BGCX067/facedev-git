package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsNumberLiteralDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Implementation of {@link JsNumberLiteralDescriptor}.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsNumberLiteralDescriptorImpl implements JsNumberLiteralDescriptor {
	
	private Token number;

	JsNumberLiteralDescriptorImpl(Token number) {
		this.number = number;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#getTokens()
	 */
	public List<Token> getTokens() {
		return Collections.singletonList(number);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#validate(com.facedev.js.parser.JsParseLogger)
	 */
	public void validate(JsParseLogger logger) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsNumberLiteralDescriptor#getValue()
	 */
	public double getValue() {
		// TODO Improve logic to match specification.
		return Double.parseDouble(number.toString());
	}

}
