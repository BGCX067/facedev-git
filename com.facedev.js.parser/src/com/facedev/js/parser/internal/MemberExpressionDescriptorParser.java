package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Provides parser for member expressions. Member expressions are defined in ECMA-262
 * specification chapter 11.2.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class MemberExpressionDescriptorParser implements JsDescriptorParser {

	public boolean isApplicable(TokenSource source) {
		return source.current().isKeyword(JsKeywords.KEYWORD_NEW) ||
			JsDescriptorType.PRIMARY_EXPRESSION.isApplicable(source) ||
			JsDescriptorType.FUNCTION_EXPRESSION.isApplicable(source);
	}

	public JsDescriptor parse(JsAstParser jsAstParser, TokenSource source)
			throws JsParseException {
		JsDescriptor descriptor = parseMemberExpression(jsAstParser, source);
		Token token = source.current();
		if (token.equalsTo('[')) {
			// TODO
		}
		if (token.equalsTo('.')) {
			token = source.next();
			if (!token.isIdentifier()) {
				jsAstParser.getLogger().log(JsParseLogger.Level.ERROR, 
						"Syntax error: identifier expected", token);
				return null;
			}
			
		}
		return descriptor;
	}

	JsDescriptor parseMemberExpression(JsAstParser jsAstParser,
			TokenSource source) throws JsParseException {
		Token token = source.current();
		if (token.isKeyword(JsKeywords.KEYWORD_NEW)) {
			source.next();
			return jsAstParser.expect(source, JsDescriptorType.MEMBER_EXPRESSION);
		}
		return jsAstParser.expect(source, JsDescriptorType.PRIMARY_EXPRESSION, 
				JsDescriptorType.FUNCTION_EXPRESSION);
	}

}
