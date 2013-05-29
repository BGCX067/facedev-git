package com.facedev.js.parser.internal;

import java.util.Arrays;

import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

public class ParseTestUtils {
	
//	public static JsSyntaxNode parse(JsDescriptorType type, String input) throws JsParseException {
//		StringReader reader = new StringReader(input);
//		JsAstParser parser = new JsAstParser(reader);
//		parser.setLogger(new ExceptionThrowingLogger());
//		TokenSource source = parser.createTokenSource();
//		source.next();
//		return parser.expect(source, type);
//	}
	
	public static class LogMessageError extends RuntimeException {
		private static final long serialVersionUID = 3506804313839457927L;

		public LogMessageError(String message) {
			super(message);
		}		
	}
	
	@SuppressWarnings("unused")
	private static class ExceptionThrowingLogger implements JsParseLogger {

		public void log(Message message, Token... tokens) {
			throw new LogMessageError(message.name() + " on tokens: " + 
					Arrays.toString(tokens));
		}
		
	}
}
