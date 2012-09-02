package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * Parses primary expression descriptors. Primary expression definition provided in
 * ECMA-262 specification chapter 11.1.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class PrimaryExpressionDescriptorParser implements JsDescriptorParser {

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.JsDescriptorParser#isApplicable(com.facedev.js.parser.internal.TokenSource)
	 */
	public boolean isApplicable(TokenSource source) {
		Token token = source.current();
		return token.isIdentifier() ||
				(token.isKeyword() && (
					token.isKeyword(JsKeywords.KEYWORD_THIS) ||
					token.isKeyword(JsKeywords.KEYWORD_TRUE) ||
					token.isKeyword(JsKeywords.KEYWORD_FALSE) ||
					token.isKeyword(JsKeywords.KEYWORD_NULL) ||
					token.isKeyword(JsKeywords.KEYWORD_UNDEFINED)
				)) ||
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
		Token tok = source.current();
		if (tok.isIdentifier()) {
			return new JsIdentifierdescriptorImpl(tok);
		}
		if (tok.isKeyword()) {
			return parseKeyword(tok);
		}
		if (tok.equalsTo('{')) {
			// parse JSON
		}
		if (tok.equalsTo('[')) {
			// parse array literal
		}
		if (tok.isStringLiteral()) {
			return new JsStringLiteralDescriptorImpl(tok);
		}
		if (tok.isDigitLiteral()) {
			return new JsNumberLiteralDescriptorImpl(tok);
		}
		if (tok.isRegex()) {
			return new JsRegexpLiteralDescriptorImpl(tok);
		}
		if (tok.equalsTo('(')) {	
			// parse expression
		}
		throw new JsParseException("Wrong token has been passed: " + tok);
	}

	private JsDescriptor parseKeyword(Token tok) throws JsParseException {
		if (tok.isKeyword(JsKeywords.KEYWORD_THIS)) {
			return new JsIdentifierdescriptorImpl(tok);
		}
		if (tok.isKeyword(JsKeywords.KEYWORD_TRUE)) {
			return new JsBooleanLiteralDescriptorImpl(tok, true);
		} 
		if (tok.isKeyword(JsKeywords.KEYWORD_FALSE)) {
			return new JsBooleanLiteralDescriptorImpl(tok, false);
		} 
		if (tok.isKeyword(JsKeywords.KEYWORD_NULL)) {
			return new JsNullLiteralDescriptorImpl(tok);
		}
		if (tok.isKeyword(JsKeywords.KEYWORD_UNDEFINED)) {
			return new JsUndefinedlLiteralDescriptorImpl(tok);
		}
		throw new JsParseException("Wrong token has been passed: " + tok);
	}

}
