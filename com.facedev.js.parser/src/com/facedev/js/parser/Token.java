package com.facedev.js.parser;

/**
 * Represents single token of javascript language. Hides one or several characters within.
 * Tokens are read from input file and passed to parsing engine.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface Token {

	/**
	 * @param str - {@link CharSequence} to test for equality
	 * @return <code>true</code> if this token equals to
	 * some characters sequence passed. Otherwise returns <code>false</code>.
	 */
	boolean equalsTo(CharSequence str);

	/**
	 * @param character to test for equality
	 * @return <code>true</code> if this token equals to single character.
	 * Otherwise returns <code>false</code>.
	 */
	boolean equalsTo(char c);

	/**
	 * @return <code>true</code> if this token is javascript identifier 
	 * (or keyword - since no validation is performed on this step).
	 */
	boolean isIdentifier();
	
	/**
	 * @param keyword (should be interned with {@link String#intern()}).
	 * @return <code>true</code> if this token is equal to keyword passed.
	 * Otherwise returns <code>false</code>.
	 */
	boolean isKeyword(String keyword);

	/**
	 * @return <code>true</code> if this token is digit.
	 */
	boolean isDigitLiteral();

	/**
	 * @return <code>true</code> if this token is string literal
	 */
	boolean isStringLiteral();
	
	/**
	 * @return <code>true</code> if this token is regular expression literal
	 */
	boolean isRegex();

	/**
	 * @return <code>true</code> if this token is single- or multi-line comment
	 */
	boolean isComment();
	
	/**
	 * @return <code>true</code> if this token is end of javascript expression
	 * (either line termination symbol or ';')
	 */
	boolean isExpressionEnd();

	/**
	 * @return line first character of this token belong to.
	 */
	int getLine();

	/**
	 * @return offset of first character of this token relative to the beginning of the line.
	 */
	int getOffset();

	/**
	 * Writes characters of this token to the result.
	 * @param result
	 */

	void writeTo(StringBuilder result);
	
	/**
	 * @return representation of this token as string.
	 */
	String toString();
}
