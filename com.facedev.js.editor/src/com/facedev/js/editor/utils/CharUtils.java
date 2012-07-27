package com.facedev.js.editor.utils;
/**
 * Utility class provides common operations on characters.
 * 
 * @see also {@link Character} class.
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class CharUtils {

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
	
}
