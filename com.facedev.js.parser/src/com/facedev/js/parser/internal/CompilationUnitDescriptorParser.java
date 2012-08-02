package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;

/**
 * This implementation provides parsing engine for whole compilation unit.
 * Compilation unit is represented by either javascript file our other source
 * of javascript tokens (e.g. java.net.URL).
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class CompilationUnitDescriptorParser implements JsDescriptorParser {

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.JsDescriptorParser#isApplicable(com.facedev.js.parser.internal.TokenSource)
	 */
	public boolean isApplicable(TokenSource source) {
		// check if we are in the beginning of the stream
		return source.current() == null; 
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.JsDescriptorParser#parse(com.facedev.js.parser.internal.JsAstParser, com.facedev.js.parser.internal.TokenSource)
	 */
	public JsDescriptor parse(JsAstParser jsAstParser, TokenSource source) {
		// TODO Auto-generated method stub
		return null;
	}

}
