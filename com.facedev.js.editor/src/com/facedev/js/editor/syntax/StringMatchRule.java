package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.facedev.js.editor.utils.CharUtils;
/**
 * Rule to match {@link CharSequence}s. Useful for keywords search.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class StringMatchRule implements IRule {

	private char[] value;
	
	private IToken token;
	
	/**
	 * Creates rule based on {@link CharSequence} and token that matches rule.
	 * @param value to test
	 * @param token to return if rule is successfully evaluated.
	 */
	public StringMatchRule(CharSequence value, IToken token) {
		if (value == null || value.length() == 0) {
			throw new IllegalArgumentException("Values should ne non-empty not null string");
		}
		if (token == null) {
			throw new IllegalArgumentException("Token cannot be NULL");
		}
		this.token = token;
		this.value = toCharArray(value);
	}

	private char[] toCharArray(CharSequence value) {
		final int length = value.length();
		
		char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			result[i] = value.charAt(i);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		if (value[0] == scanner.read()) {
			return doEvaluate(scanner);
		} else {
			scanner.unread();
		}
		
		return Token.UNDEFINED;
	}

	private IToken doEvaluate(ICharacterScanner scanner) {
		final int column = scanner.getColumn();
		if (column == 0) {
			scanner.unread();
			return Token.UNDEFINED;
		}

		/*
		 * If word is not in the beginning of the line we should check 
		 * if there is at least one non-identifier character before the word. 
		 */
		if (column > 1) {
			scanner.unread();
			scanner.unread();
			
			if (CharUtils.isIdentifierPart(scanner.read())) {
				return Token.UNDEFINED;
			}
			scanner.read();
		}
		
		int index = 1;
		final int length = value.length;
		
		/*
		 * Check the word itself:
		 */
		for (int i = 1; i < length; i++) {
			index ++;
			if (value[i] != scanner.read()) {
				unread(scanner, index);
				return Token.UNDEFINED;
			}
		}
		
		/*
		 * Last character should be non-identifier also.
		 */
		int last = scanner.read();
		
		if (last != ICharacterScanner.EOF && CharUtils.isIdentifierPart(last)) {
			unread(scanner, ++index);
			return Token.UNDEFINED;
		}
		
		if (last != ICharacterScanner.EOF) {
			scanner.unread();
		}
		
		return token;
	}

	private void unread(ICharacterScanner scanner, int i) {
		for (; i > 0; i--) {
			scanner.unread();
		}
	}

}
