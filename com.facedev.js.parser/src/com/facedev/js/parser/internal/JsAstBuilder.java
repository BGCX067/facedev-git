package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

/**
 * Helper class to help to build abstract syntax tree. JsAstParser uses it to paste nodes.
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsAstBuilder {
	
	/**
	 * Accepts meaning terminal.
	 * @param token
	 */
	void accept(JsFlexToken token) {
		
	}
	
	/**
	 * Creates binary operator from two last accepted branches.
	 * @param operator
	 */
	void binaryOperator(String operator) {
		
	}
	
	/**
	 * Commits changes to the current point. This action means that all lookaheads before current point are done
	 * and parser will never return further this point.
	 */
	void commit() {
		
	}

	List<JsSyntaxNode> create() {
		return Collections.emptyList();
	}
}
