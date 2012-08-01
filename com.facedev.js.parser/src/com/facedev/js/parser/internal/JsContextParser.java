package com.facedev.js.parser.internal;

import java.io.Reader;

import com.facedev.js.parser.JsCompilationUnitDescriptor;
import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsParser;

/**
 * Abstract syntax tree (AST) based implementation of javascript parser.
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsContextParser extends JsParser {
	
	private Reader reader;

	public JsContextParser(Reader reader) {
		this.reader = reader;
	}

	@Override
	public JsCompilationUnitDescriptor parse(JsParseLogger logger) {
		CommentsTokenFilter filter = new CommentsTokenFilter(new Tokenizer(reader));
		return (JsCompilationUnitDescriptor) expect(filter, JsDescriptorType.COMPILATION_UNIT);
	}

	JsDescriptor expect(TokenSource source, JsDescriptorType...types) {
		return null;
	}
}
