package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;
/**
 * Provides parsers for all kinds of javascript descriptors.
 * Enumerated types can be used in 
 * {@link JsAstParser#expect(TokenSource, JsDescriptorType...)} method
 * for syntax parsing.
 *
 * @author alex.bereznevatiy@gmail.com
 *
 */
enum JsDescriptorType {
	
	/**
	 * Represents whole compilation unit.
	 * @see CompilationUnitDescriptorParser
	 */
	COMPILATION_UNIT(new CompilationUnitDescriptorParser()), 
	
	/**
	 * Represents single javascript expression.
	 */
	EXPRESSION(null),
	
	/**
	 * Represents primary expression as defined in ECMA-262 specification (chapter 11.1)
	 */
	PRIMARY_EXPRESSION(new PrimaryExpressionDescriptorParser()),
	
	/**
	 * Represents member expression (see ECMA-262 specification chapter 11.2)
	 */
	MEMBER_EXPRESSION(new MemberExpressionDescriptorParser()),
	
	/**
	 * TODO
	 */
	FUNCTION_EXPRESSION(null);
	
	private JsDescriptorParser parser;
	
	private JsDescriptorType(JsDescriptorParser parser) {
		this.parser = parser;
	}

	/**
	 * @param source
	 * @return <code>true</code> if current token is a start token of this descriptor type.
	 */
	public boolean isApplicable(TokenSource source) {
		return parser.isApplicable(source);
	}

	/**
	 * @param jsAstParser
	 * @param source
	 * @return new descriptor parsed from {@link TokenSource}
	 * @throws JsParseException
	 */
	public JsDescriptor parse(JsAstParser jsAstParser, TokenSource source) throws JsParseException {
		return parser.parse(jsAstParser, source);
	}

}
