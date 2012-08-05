package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * Parses primary expression descriptors. Primary expression definition provided in
 * ECMA-262 specification chapter 11.1.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
class PrimaryExpressionDescriptorParser implements JsDescriptorParser {

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.JsDescriptorParser#isApplicable(com.facedev.js.parser.internal.TokenSource)
	 */
	public boolean isApplicable(TokenSource source) {
		Token token = source.current();
		return token.isIdentifier() || token.isKeyword("this") ||
				token.isStringLiteral() || token.isDigitLiteral() ||
				token.isRegex() || token.equalsTo('(') 
				|| isArrayLiteral(token) || isObjectLiteral(token);
	}

	private boolean isObjectLiteral(Token token) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isArrayLiteral(Token token) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.JsDescriptorParser#parse(com.facedev.js.parser.internal.JsAstParser, com.facedev.js.parser.internal.TokenSource)
	 */
	public JsDescriptor parse(JsAstParser jsAstParser, TokenSource source)
			throws JsParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
