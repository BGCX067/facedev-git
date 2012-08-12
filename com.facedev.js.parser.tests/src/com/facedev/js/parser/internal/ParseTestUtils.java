package com.facedev.js.parser.internal;

import java.io.StringReader;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;

public class ParseTestUtils {
	
	public static JsDescriptor parse(JsDescriptorType type, String input) throws JsParseException {
		StringReader reader = new StringReader(input);
		JsAstParser parser = new JsAstParser(reader);
		TokenSource source = parser.createTokenSource();
		source.next();
		return parser.expect(source, type);
	}
}
