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
	
	/**
	 * @param character to test
	 * @return <code>true</code> if character is javascript expression delimiter.
	 */
	public static boolean isExpressionDelimiter(char ch) {
		return isExpressionDelimiter((int)ch);
	}

	/**
	 * @param codePoint to test
	 * @return <code>true</code> if code point is javascript expression delimiter.
	 */
	public static boolean isExpressionDelimiter(int codePoint) {
		return codePoint == 0x00A ||
			codePoint == 0x00D || 
			codePoint == 0x02028 || 
			codePoint == 0x02029 ||
			codePoint == ';';
	}

	/**
	 * @param character to test
	 * @return <code>true</code> if character is javascript white space
	 */
	public static boolean isWhitespace(char ch) {
		return isWhitespace((int)ch);
	}
	
	/**
	 * @param codePoint to test
	 * @return <code>true</code> if code point is javascript white space
	 */
	public static boolean isWhitespace(int codePoint) {
		return Character.getType(codePoint) == Character.SPACE_SEPARATOR;
	}
}
