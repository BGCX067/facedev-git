package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * Filters out comment tokens as thay are not intended to be parsed.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class CommentsTokenFilter implements TokenSource {
	
	private TokenSource source;
	
	/**
	 * Creates token filter based on other token source.
	 * @param source
	 */
	CommentsTokenFilter(TokenSource source) {
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#current()
	 */
	public Token current() {
		return source.current();
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#next()
	 */
	public Token next() throws JsParseException {
		while (source.next() != null && 
			source.current().isComment());
		
		return source.current();
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#previous()
	 */
	public Token previous() throws JsParseException {
		while (source.previous() != null && 
				source.current().isComment());
		
		return source.current();
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#commit()
	 */
	public void commit() {
		source.commit();
	}

	public void rollback(Token to) {
		source.rollback(to);
	}
}
