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
	public static final String COMMENT_PART = "__bs__comment";
	
	/**
	 * Token ID for javascript documentation (jsdoc).
	 */
	public static final String BSDOC_PART = "__bs__doc";

	/**
	 * @return array of legal content types for this scanner.
	 */
	public static String[] getLegalContentTypes() {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, COMMENT_PART, BSDOC_PART } ;
	}

	/**
	 * Initializes scanner with all necessary rules.
	 */
	public JsPartitionsScanner() {
		IToken comment = new Token(COMMENT_PART);
		IToken bsDoc = new Token(BSDOC_PART);
		
		IPredicateRule[] rules = new IPredicateRule[3];
		rules[1] = new MultiLineRule("/*", "*/", comment);
		rules[0] = new MultiLineRule("/**", "*/", bsDoc);
		rules[2] = new SingleLineRule("//", "\n", comment);
				
		setPredicateRules(rules);
	}
}