package com.facedev.js.parser.internal;

import java.io.StringReader;

import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

public class ParseTestUtils {
	
	public static JsDescriptor parse(JsDescriptorType type, String input) throws JsParseException {
		StringReader reader = new StringReader(input);
		JsAstParser parser = new JsAstParser(reader);
		parser.setLogger(new ExceptionThrowingLogger());
		TokenSource source = parser.createTokenSource();
		source.next();
		return parser.expect(source, type);
	}
	
	public static class LogMessageError extends RuntimeException {
		private static final long serialVersionUID = 3506804313839457927L;

		public LogMessageError(String message) {
			super(message);
		}		
	}
	
	private static class ExceptionThrowingLogger implements JsParseLogger {

		public void log(Level level, String message, Token token) {
			throw new LogMessageError(level.name() + " - " + 
					message + " on token: " + token);
		}

		public void log(Level level, String message, JsDescriptor descriptor) {
			throw new LogMessageError(level.name() + " - " + message + " on descriptor: " + 
					descriptor.getClass().getName() + descriptor.getTokens());
		}
		
	}
}
