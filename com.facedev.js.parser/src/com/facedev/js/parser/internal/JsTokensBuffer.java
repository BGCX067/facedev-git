package com.facedev.js.parser.internal;

import java.io.IOException;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * This buffer serves as temporary storage for lookahead tokens.
 * Such functionality is crucial for top-down AST parser.
 * 
 * Buffer may produce {@link SavePoint}s that hold some points where
 * lookahead is used and parser is not sure which rule to use.
 * 
 * It is safe for buffer to delete tokens before first checkpoint.
 *  
 * @author alex.bereznevatiy@gmail.com
 *
 */
class JsTokensBuffer {
	
	private JsTokenizer tokenizer;
	
	private Node next;
	private Node lastReturned;
	
	JsTokensBuffer(JsTokenizer tokenizer) throws IOException, JsParseException {
		this.tokenizer = tokenizer;
		next = new Node(nextTerminal());
	}
	
	/**
	 * @return {@link SavePoint} for the current position of the pointer in buffer.
	 */
	SavePoint createSavePoint() {
		return new SavePoint() {
			
			private Node node = next;

			public boolean rollback() {
				next = node;
				return true;
			}

			public void forward() {
				node = node.next;
			}
			
		};
	}
	
	/**
	 * @return next token from this buffer (or tokenizer if yet not read).
	 * @throws IOException
	 * @throws JsParseException
	 */
	JsFlexToken next() throws IOException, JsParseException {
		lastReturned = next;
		if (next == null) {
			return null;
		}
		JsFlexToken result = next.token;
		if (result == null) {
			return result;
		}
		if (next.next == null) {
			next.next = new Node(nextTerminal());
		}
		next = next.next;
		return result;
	}

	JsFlexToken getLastReturned() {
		return lastReturned == null ? null : lastReturned.token;
	}

	private JsFlexToken nextTerminal() throws IOException, JsParseException {
		JsFlexToken next = tokenizer.next();
		while (next != null && next.isIgnored()) {
			if (next.isLineTerminator() && this.next != null) {
				this.next.terminated = true;
			}
			next = tokenizer.next();
		}
		return next;
	}

	/**
	 * @param keyword
	 * @return <code>true</code> if next token matches keyword passed.
	 * @throws IOException
	 * @throws JsParseException
	 */
	boolean isKeyword(String keyword) throws IOException, JsParseException {
		JsFlexToken tok = next();
		return tok != null && tok.isKeyword() && tok.isSame(keyword);
	}
	
	/**
	 * @return <code>true</code> if this buffer has more tokens.
	 */
	boolean hasNext() {
		return next!= null && next.token != null;
	}

	/**
	 * 
	 * @return true if next token is whitespace
	 */
	boolean isWhiteSpace() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isWhiteSpace();
	}

	/**
	 * @return <code>true</code> if next token is identifier.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isIdentifier() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isIdentifier();
	}
	
	/**
	 * @param identifier
	 * @return <code>true</code> if next toke matches specific identifier.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isIdentifier(String identifier) throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isIdentifier() && tok.equalsTo(identifier);
	}

	/**
	 * @return <code>true</code> if next token is literal.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isLiteral() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isLiteral();
	}

	/**
	 * @return <code>true</code> if next token is string literal.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isStringLiteral() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isStringLiteral();
	}

	/**
	 * @return <code>true</code> if next token is numeric literal.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isNumericLiteral() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isNumberLiteral();
	}

	/**
	 * @param punctuator
	 * @return <code>true</code> if next token matches specific punktuator.
	 * @throws IOException
	 * @throws JsParseException
	 */
	boolean isPunktuator(String punctuator) throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isSame(punctuator);
	}
	
	/**
	 * @return <code>true</code> if last token was followed by line termination sequence.
	 */
	boolean isTerminated() {
		return lastReturned != null && lastReturned.terminated;
	}
	
	private static class Node {
		private Node next;
		private JsFlexToken token;
		public boolean terminated;
		
		Node(JsFlexToken token) {
			this.token = token;
		}
	}
}

/**
 * Starting form this point lookahead will be used.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
interface SavePoint {
	/**
	 * Rolls back pointer in the buffer to this {@link SavePoint}.
	 * @return always <code>true</code> for convenience
	 */
	boolean rollback();

	/**
	 * Moves savepoint forward on the tokens chain
	 */
	void forward();
}