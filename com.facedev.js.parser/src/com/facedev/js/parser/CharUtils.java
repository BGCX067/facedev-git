package com.facedev.js.parser;
/**
 * Utility class provides common operations on characters.
 * 
 * @see also {@link Character} class.
 * @author alex.bereznevatiy@gmail.com
 *
 */
public final class CharUtils {
	
	private CharUtils() {}

	/**
	 * @param char to test
	 * @return <code>true</code> if character is valid identifier part for javascript identifier.
	 * Otherwise returns <code>false</code>.
	 */
	public static boolean isIdentifierPart(char ch) {
		return isIdentifierPart((int)ch);
	}

	/**
	 * @param codePoint to test
	 * @return <code>true</code> if code point is valid identifier part for javascript identifier.
	 * Otherwise returns <code>false</code>.
	 */
	public static boolean isIdentifierPart(int codePoint) {
		return Character.isLetterOrDigit(codePoint) || codePoint == '_' || codePoint == '$';
	}
	
	/**
	 * @param char to test
	 * @return <code>true</code> if character is valid identifier start for javascript identifier.
	 * Otherwise returns <code>false</code>.
	 */
	public static boolean isIdentifierStart(char ch) {
		return isIdentifierStart((int)ch);
	}

	/**
	 * @param codePoint to test
	 * @return <code>true</code> if code point is valid identifier start for javascript identifier.
	 * Otherwise returns <code>false</code>.
	 */
	public static boolean isIdentifierStart(int codePoint) {
		return Character.isLetter(codePoint) || codePoint == '_' || codePoint == '$';
	}

	/**
	 * @param character to test
	 * @return <code>true</code> if character is valid javascript letter.
	 */
	public static boolean isLetter(char ch) {
		return isLetter((int) ch);
	}

	/**
	 * @param codePoint to test
	 * @return <code>true</code> if code point is valid javascript letter.
	 */
	public static boolean isLetter(int codePoint) {
		return Character.isLetter(codePoint);
	}
	
}
