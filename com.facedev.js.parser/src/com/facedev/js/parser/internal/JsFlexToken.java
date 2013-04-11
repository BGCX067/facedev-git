package com.facedev.js.parser.internal;

import com.facedev.js.parser.Token;

final class JsFlexToken implements Token {
	
	static final String TOKEN_LINE_TERMINATOR = "\n";
	
	static final String TOKEN_WHITE_SPACE = " ";
	
	/**
	 * Matches the LineTerminatorSequence in ECMA specification (ECMA 7.2)
	 */
	static final int LINE_TERMINATOR = 0x01;
	
	/**
	 * Matches multiple occurrences of WhiteSpace in ECMA specification (ECMA 7.2)
	 */
	static final int WHITE_SPACE = 0x02;
	
	/**
	 * Matches javascript comment (Comment, ECMA 7.4)
	 */
	static final int COMMENT = 0x04;
	
	/**
	 * Matches javascript reserved word (ReservedWord, ECMA 7.6.1)
	 */
	static final int RESERVED = 0x08;
	
	/**
	 * Matches javascript reserved word (FutureReservedWord, ECMA 7.6.1)
	 */
	static final int FUTURE_RESERVED = 0x010;
	
	/**
	 * Matches javascript reserved word in strict mode (FutureReservedWord, ECMA 7.6.1)
	 */
	static final int FUTURE_RESERVED_STRICT = 0x020;
	
	/**
	 * Matches javascript keyword (Keyword, ECMA 7.6.1)
	 */
	static final int RESERVED_KEYWORD = 0x040;
	
	/**
	 * Matches javascript punctuator (Punctuator, ECMA 7.7)
	 */
	static final int PUNKTUATOR = 0x080;
	
	/**
	 * Matches javascript division punctuator (DivPunctuator, ECMA 7.7)
	 */
	static final int DIV_PUNKTUATOR = 0x0100;
	
	/**
	 * Matches any javascript punctuator (Punctuator or DivPunctuator, ECMA 7.7)
	 */
	static final int ANY_PUNKTUATOR = 0x0200;
	
	/**
	 * Matches any javascript literal (Literal, ECMA 7.8) 
	 */
	static final int LITERAL = 0x0400;
	
	/**
	 * Matches null javascript literal (NullLiteral, ECMA 7.8.1)
	 */
	static final int NULL_LITERAL = 0x0800;
	
	/**
	 * Matches boolean javascript literal (BooleanLiteral, ECMA 7.8.2)
	 */
	static final int BOOLEAN_LITERAL = 0x01000;
	
	/**
	 * Matches numeric javascript literal (NumbericLiteral, ECMA 7.8.3)
	 */
	static final int NUMERIC_LITERAL = 0x02000;
	
	/**
	 * Matches string javascript literal (StringLiteral, ECMA 7.8.4)
	 */
	static final int STRING_LITERAL = 0x04000;
	
	/**
	 * Matches regular expression javascript literal (RegularExpressionLiteral, ECMA 7.8.5)
	 */
	static final int REGEXP_LITERAL = 0x08000;
	
	/**
	 * Matches javascript identifier (not reserved word) (Identifier, ECMA 7.6)
	 */
	static final int IDENTIFIER = 0x010000;
	
	/**
	 * If token is not matching any token class it is declared as error token
	 */
	static final int ERROR = 0x020000;
	
	private final String data;
	private final int state;
	private final int line;
	private final int column;
	
	JsFlexToken(String data, int state, int line, int column) {
		this.data = data;
		this.state = state;
		this.line = line + 1;
		this.column = column + 1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return data.length();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(int index) {
		return data.charAt(index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#subSequence(int, int)
	 */
	public String subSequence(int start, int end) {
		return data.substring(start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#equalsTo(java.lang.CharSequence)
	 */
	public boolean equalsTo(CharSequence str) {
		return data.contentEquals(str);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#equalsTo(char)
	 */
	public boolean equalsTo(char c) {
		return data.length() == 1 && data.charAt(0) == c;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isIdentifier()
	 */
	public boolean isIdentifier() {
		return (state & IDENTIFIER) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isKeyword(java.lang.String)
	 */
	public boolean isKeyword(String keyword) {
		return data == keyword;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isKeyword()
	 */
	public boolean isKeyword() {
		return (state & RESERVED_KEYWORD) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isReserved()
	 */
	public boolean isReserved() {
		return (state & RESERVED) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isReservedForFuture()
	 */
	public boolean isReservedForFuture() {
		return (state & FUTURE_RESERVED) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isReservedForFutureStrictMode()
	 */
	public boolean isReservedForFutureStrictMode() {
		return (state & FUTURE_RESERVED_STRICT) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isLiteral()
	 */
	public boolean isLiteral() {
		return (state & LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isNullLiteral()
	 */
	public boolean isNullLiteral() {
		return (state & NULL_LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isBooleanLiteral()
	 */
	public boolean isBooleanLiteral() {
		return (state & BOOLEAN_LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isNumberLiteral()
	 */
	public boolean isNumberLiteral() {
		return (state & NUMERIC_LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isStringLiteral()
	 */
	public boolean isStringLiteral() {
		return (state & STRING_LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isRegexpLiteral()
	 */
	public boolean isRegexpLiteral() {
		return (state & REGEXP_LITERAL) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isComment()
	 */
	public boolean isComment() {
		return (state & COMMENT) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isLineTerminator()
	 */
	public boolean isLineTerminator() {
		return (state & LINE_TERMINATOR) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isWhiteSpace()
	 */
	public boolean isWhiteSpace() {
		return (state & WHITE_SPACE) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isPunctuator()
	 */
	public boolean isPunctuator() {
		return (state & PUNKTUATOR) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isDivPunctuator()
	 */
	public boolean isDivPunctuator() {
		return (state & DIV_PUNKTUATOR) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isAnyPunctuator()
	 */
	public boolean isAnyPunctuator() {
		return (state & ANY_PUNKTUATOR) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#isError()
	 */
	public boolean isError() {
		return (state & ERROR) > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#getLine()
	 */
	public int getLine() {
		return line;
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.Token#getOffset()
	 */
	public int getOffset() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return data;
	}
}
