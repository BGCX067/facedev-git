package com.facedev.js.parser;

/**
 * Represents single token of javascript language. Hides one or several characters within.
 * Tokens are read from input file and passed to parsing engine.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface Token extends CharSequence {

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
	 * @return <code>true</code> if this token is javascript identifier (except keywords and reserved words).
	 */
	boolean isIdentifier();
	
	/**
	 * @return <code>true</code> if this token represents one of the 
	 * javascript language keywords. 
	 */
	boolean isKeyword();
	
	/**
	 * @return <code>true</code> if this token represents one of the javascript language reserved words (including keywords).
	 */
	boolean isReserved();
	
	/**
	 * @return <code>true</code> if this token represents one of the javascript language reserved words for future usage.
	 */
	boolean isReservedForFuture();
	
	/**
	 * @return <code>true</code> if this token represents one of the javascript language reserved words for future usage in strict mode.
	 */
	boolean isReservedForFutureStrictMode();

	/**
	 * @return <code>true</code> if this token is javascript literal.
	 */
	boolean isLiteral();
	
	/**
	 * @return <code>true</code> if this token is javascript null literal.
	 */
	boolean isNullLiteral();
	
	/**
	 * @return <code>true</code> if this token is javascript boolean literal.
	 */
	boolean isBooleanLiteral();
	
	/**
	 * @return <code>true</code> if this token is number literal.
	 */
	boolean isNumberLiteral();

	/**
	 * @return <code>true</code> if this token is string literal
	 */
	boolean isStringLiteral();
	
	/**
	 * @return <code>true</code> if this token is regular expression literal
	 */
	boolean isRegexpLiteral();

	/**
	 * @return <code>true</code> if this token is single- or multi-line comment
	 */
	boolean isComment();
	
	/**
	 * @return <code>true</code> if this token is line termination sequence
	 */
	boolean isLineTerminator();
	
	/**
	 * @return <code>true</code> if this token represents white spaces block
	 */
	boolean isWhiteSpace();
	
	/**
	 * @return <code>true</code> if this token represents punctuator.
	 */
	boolean isPunctuator();
	
	/**
	 * @return <code>true</code> if this token represents division punctuator.
	 */
	boolean isDivPunctuator();
	
	/**
	 * @return <code>true</code> if this token represents any punctuator.
	 */
	boolean isAnyPunctuator();
	
	/**
	 * @return <code>true</code> if this token is error token and cannot be recognized by tokenizer.
	 */
	boolean isError();
	
	/**
	 * Useful fast method for keywords and punctuators.
	 * @param interned with {@link String#intern()}) string.
	 * @return <code>true</code> if this token is equal to value passed.
	 * Otherwise returns <code>false</code>.
	 */
	boolean isSame(String value);

	/**
	 * @return line first character of this token belong to.
	 */
	int getLine();

	/**
	 * @return offset of first character of this token relative to the beginning of the line.
	 */
	int getOffset();
	
	/**
     * Returns a new <code>String</code> that is a subsequence of this token.
     * The subsequence starts with the <code>char</code> value at the specified index and
     * ends with the <code>char</code> value at index <tt>end - 1</tt>.  The length
     * (in <code>char</code>s) of the
     * returned sequence is <tt>end - start</tt>, so if <tt>start == end</tt>
     * then an empty sequence is returned. </p>
     * 
     * @param   start   the start index, inclusive
     * @param   end     the end index, exclusive
     *
     * @return  substring presentation of this token
     *
     * @throws  IndexOutOfBoundsException
     *          if <tt>start</tt> or <tt>end</tt> are negative,
     *          if <tt>end</tt> is greater than <tt>length()</tt>,
     *          or if <tt>start</tt> is greater than <tt>end</tt>
     */
    String subSequence(int start, int end);

    /**
     * Returns a string containing the characters in this token in the same
     * order.
     *
     * @return  a string representation of this token
     */
    public String toString();
}
