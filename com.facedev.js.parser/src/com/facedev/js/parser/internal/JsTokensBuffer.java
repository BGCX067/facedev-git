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
	
	private Node last;
	
	JsTokensBuffer(JsTokenizer tokenizer) throws IOException, JsParseException {
		this.tokenizer = tokenizer;
		last = new Node(tokenizer.next());
	}
	
	/**
	 * @return {@link SavePoint} for the current position of the pointer in buffer.
	 */
	SavePoint createSavePoint() {
		return new SavePoint() {
			
			private Node node = last;

			public boolean rollback() {
				last = node;
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
		JsFlexToken result = last.token;
		if (result == null) {
			return result;
		}
		if (last.next == null) {
			last.next = new Node(tokenizer.next());
		}
		last = last.next;
		return result;
	}

	/**
	 * @param keyword
	 * @return <code>true</code> if next token matches keyword passed.
	 * @throws IOException
	 * @throws JsParseException
	 */
	boolean keyword(String keyword) throws IOException, JsParseException {
		JsFlexToken tok = next();
		return tok != null && tok.isKeyword() && tok.isSame(keyword);
	}
	
	/**
	 * @return <code>true</code> if this buffer has more tokens.
	 */
	boolean hasNext() {
		return last.token != null;
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
	 * @return <code>true</code> if next token is literal.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	boolean isLiteral() throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isLiteral();
	}

	boolean isPunktuator(String punctuator) throws IOException, JsParseException {
		Token tok = next();
		return tok != null && tok.isSame(punctuator);
	}
	
	private static class Node {
		private Node next;
		private JsFlexToken token;
		
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