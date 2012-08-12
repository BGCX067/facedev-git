package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsIdentifierDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Implementation of {@link JsIdentifierDescriptor}.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
class JsIdentifierdescriptorImpl implements JsIdentifierDescriptor {
	
	private Token identifier;
	
	JsIdentifierdescriptorImpl(Token identifier) {
		this.identifier = identifier;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#getTokens()
	 */
	public List<Token> getTokens() {
		return Collections.singletonList(this.identifier);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#validate(com.facedev.js.parser.JsParseLogger)
	 */
	public void validate(JsParseLogger logger) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsIdentifierDescriptor#getIdentifier()
	 */
	public String getIdentifier() {
		return this.identifier.toString();
	}

}
