package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsDescriptor;

/**
 * Provides parsing engine for single descriptor type.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsDescriptorParser {

	/**
	 * Checks if this parser can parse current position. Contract is that token 
	 * source should point to the same position after calling this method.
	 * @param source to read tokens from
	 * @return <code>true</code> if this parser can parse current position
	 */
	boolean isApplicable(TokenSource source);

	/**
	 * Parses current token and creates descriptor for it or <code>null</code>
	 * if syntax error occurs. Token source should point to the next token after last
	 * token that belongs to the descriptor parsed. 
	 * @param jsAstParser
	 * @param source to read tokens
	 * @return corresponding instance of JsDescriptor or <code>null</code> if
	 * parse error occurs on this descriptor and it cannot be parsed. 
	 */
	JsDescriptor parse(JsAstParser jsAstParser, TokenSource source);

}
