package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * This scanner main goal is to partition document on different parts according to content type of each part.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsPartitionsScanner extends RuleBasedPartitionScanner {

	/**
	 * Token ID for javascript comment.
	 */
	public static final String COMMENT_PART = "__js__comment";
	
	/**
	 * Token ID for javascript documentation (jsdoc).
	 */
	public static final String JSDOC_PART = "__js__doc";
	
	public static final String LITERAL_PART = "__js__literal";

	/**
	 * @return array of legal content types for this scanner.
	 */
	public static String[] getLegalContentTypes() {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, COMMENT_PART, JSDOC_PART, LITERAL_PART } ;
	}

	/**
	 * Initializes scanner with all necessary rules.
	 */
	public JsPartitionsScanner() {
		IToken comment = new Token(COMMENT_PART);
		IToken jsDoc = new Token(JSDOC_PART);
		IToken literal = new Token(LITERAL_PART);
		
		IPredicateRule[] rules = new IPredicateRule[8];
		rules[1] = new MultiLineRule("/*", "*/", comment, (char)0, true);
		rules[0] = new MultiLineRule("/**", "*/", jsDoc, (char)0, true);
		rules[2] = new SingleLineRule("//", "\n", comment, (char)0, true);
		rules[3] = new SingleLineRule("'", "'", literal);
		rules[4] = new SingleLineRule("\"", "\"", literal);
		rules[5] = new SingleLineRule("'", "\n", literal, '\\', true);
		rules[6] = new SingleLineRule("\"", "\n", literal, '\\', true);
		rules[7] = new RegexDetectionRule(literal);
				
		setPredicateRules(rules);
	}
	
	boolean isBegin() {
		return fOffset == 0;
	}
}