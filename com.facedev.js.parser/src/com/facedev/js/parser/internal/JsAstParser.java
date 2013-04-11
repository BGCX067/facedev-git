package com.facedev.js.parser.internal;

import java.io.IOException;
import java.util.List;

import com.facedev.js.parser.JsKeywords;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.JsPunctuators;

/**
 * Top-down implementation of javascript parser. Builds abstract syntax tree (AST) from stream of tokens. 
 * 
 * Note that this class violates java naming convention (method should start with lowercase) bacause it uses
 * own convention to follow naming from the ECMA-262 lexical specification
 * 
 * @author alex.bereznevatiy@gmail.com
 */
class JsAstParser implements JsKeywords, JsPunctuators {
	
	private JsTokensBuffer buffer;
	private JsParseLogger logger;

	JsAstParser(JsTokenizer tokenizer, JsParseLogger logger) throws IOException, JsParseException {
		this.buffer = new JsTokensBuffer(tokenizer);
		this.logger = logger;
	}

	/**
	 * @return list of global nodes of AST.
	 * @throws JsParseException 
	 * @throws IOException 
	 */
	List<JsSyntaxNode> parse() throws IOException, JsParseException {
		// TODO Auto-generated method stub
		PrimaryExpression();
		return null;
	}
	
	private boolean PrimaryExpression() throws IOException, JsParseException {
		SavePoint save = buffer.createSavePoint();
		return (
			save.rollback() && buffer.keyword(KEYWORD_THIS)                                                                )||(
			save.rollback() && buffer.isIdentifier()                                                                       )||(
			save.rollback() && buffer.isLiteral()                                                                          )||(
			save.rollback() && ArrayLiteral()                                                                              )||(
			save.rollback() && ObjectLiteral()                                                                             )||(
			save.rollback() && buffer.isPunktuator(OPEN_BRACKET) && Expression() && buffer.isPunktuator(CLOSE_BRACKET)                       
		);
				
	}

	private boolean Expression() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean ObjectLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean ArrayLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

}
