package com.facedev.js.parser.internal;

import java.io.Reader;

import com.facedev.js.parser.JsCompilationUnitDescriptor;
import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsParser;

/**
 * Abstract syntax tree (AST) based implementation of javascript parser.
 * Instances of this class are not thread safe, so should be used only in 
 * single thread. 
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsAstParser extends JsParser {
	
	private Reader reader;
	private JsParseLogger logger;

	public JsAstParser(Reader reader) {
		this.reader = reader;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsParser#parse(com.facedev.js.parser.JsParseLogger)
	 */
	@Override
	public JsCompilationUnitDescriptor parse(JsParseLogger logger) throws JsParseException {
		TokenSource source = createTokenSource();
		this.logger = logger;
		return (JsCompilationUnitDescriptor) expect(source, JsDescriptorType.COMPILATION_UNIT);
	}

	/**
	 * @return new token source for the reader associated
	 */
	TokenSource createTokenSource() {
		return new CommentsTokenFilter(new Tokenizer(reader));
	}

	/**
	 * Expect some descriptor to be parsed next.
	 * Allowed descriptors are enumerated in the last parameter.
	 * If next token is not expected one this method moves tokenizer 
	 * to the end of expression and returns <code>null</code>.
	 * 
	 * Note that no logging is done by this method and clients should log errors 
	 * and warning by themselves. 
	 * 
	 * @param source to read tokens from
	 * @param types of descriptors that are allowed in this position
	 * @return parsed type or <code>null</code> in case not expected type
	 * is parsed.
	 * @throws JsParseException in case of troubles
	 */
	JsDescriptor expect(TokenSource source, JsDescriptorType...types) throws JsParseException {
		for (JsDescriptorType type : types) {
			if (type.isApplicable(source)) {
				return type.parse(this, source);
			}
		}
		while (source.current() != null && !source.current().isExpressionEnd()) {
			source.next();
		}
		if (source.current() != null) {
			source.next();
		}
		return null;
	}

	/**
	 * @return logger associated with this parser.
	 */
	JsParseLogger getLogger() {
		return logger;
	}
}
