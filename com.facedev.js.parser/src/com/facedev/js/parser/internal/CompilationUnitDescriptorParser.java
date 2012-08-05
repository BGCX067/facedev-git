package com.facedev.js.parser.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.facedev.js.parser.JsCompilationUnitDescriptor;
import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * This implementation provides parsing engine for whole compilation unit.
 * Compilation unit is represented by either javascript file our other source
 * of javascript tokens (e.g. java.net.URL).
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class CompilationUnitDescriptorParser implements JsDescriptorParser {

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
	public JsDescriptor parse(JsAstParser parser, TokenSource source) throws JsParseException {
		source.next();
		List<JsDescriptor> children = new ArrayList<JsDescriptor>();
		while (source.current() != null) {
			children.add(parser.expect(source, JsDescriptorType.EXPRESSION));
		}
		return new CompilationUnitDescriptorImpl(children);
	}
	
	/**
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	private static class CompilationUnitDescriptorImpl implements JsCompilationUnitDescriptor {
		
		private List<JsDescriptor> children;

		CompilationUnitDescriptorImpl(List<JsDescriptor> children) {
			this.children = children;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.JsDescriptor#getTokens()
		 */
		public List<Token> getTokens() {
			List<Token> result = new LinkedList<Token>();
			for (JsDescriptor descriptor : children) {
				result.addAll(descriptor.getTokens());
			}
			return Collections.unmodifiableList(result);
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.JsDescriptor#validate(com.facedev.js.parser.JsParseLogger)
		 */
		public void validate(JsParseLogger logger) {
			throw new UnsupportedOperationException("Not implemented");
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.JsCompilationUnitDescriptor#getName()
		 */
		public String getName() {
			throw new UnsupportedOperationException("Not implemented");
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.JsCompilationUnitDescriptor#getPath()
		 */
		public File getPath() {
			throw new UnsupportedOperationException("Not implemented");
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.JsCompilationUnitDescriptor#getDescriptors()
		 */
		public List<JsDescriptor> getDescriptors() {
			return Collections.unmodifiableList(children);
		}
	}
}
