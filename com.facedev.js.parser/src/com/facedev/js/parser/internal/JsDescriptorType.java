package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;
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
	COMPILATION_UNIT(null);
	
	private JsDescriptorParser parser;
	
	private JsDescriptorType(JsDescriptorParser parser) {
		this.parser = parser;
	}

	public boolean isApplicable(TokenSource source) {
		return parser.isApplicable(source);
	}

	public JsDescriptor parse(JsAstParser jsAstParser, TokenSource source) {
		return parser.parse(jsAstParser, source);
	}

}
