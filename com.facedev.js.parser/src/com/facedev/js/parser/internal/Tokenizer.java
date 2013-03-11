package com.facedev.js.parser.internal;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import com.facedev.js.parser.CharUtils;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

/**
 * Splits characters from some reader on tokens.
 * Javascript token is represented as either identifier or literal or operator single character.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
final class Tokenizer implements TokenSource {
	
	/**
	 * Default token type if others are not applied.
	 */
	private static final int TOKEN_DEFAULT = 0;

	/**
	 * Token type for java word literal 
	 */
	private static final int TOKEN_IDENTIFIER = 1;

	/**
	 * Token type for digit literal
	 */
	private static final int TOKEN_DIGIT_LITERAL = 2;

	/**
	 * Token type for string literal
	 */
	private static final int TOKEN_STRING_LITERAL = 8;

	/**
	 * Token type for comment
	 */
	private static final int TOKEN_COMMENT = 16;
	
	/**
	 * Token type for javascript regexp.
	 */
	private static final int TOKEN_REGEX = 32;
	
	/**
	 * Token type for expression delimiters.
	 */
	private static final int TOKEN_DELIMITER = 64;
	
	/**
	 * Token type for keywords
	 */
	private static final int TOKEN_KEYWORD = 128;
	
	private JsSourceReader reader;
	
	private ArrayToken current;
	private ArrayToken head;
	
	private int nextChar;
	
	private int currentLine;
	private int currentOffset;
	
	private int previousNonCommentType;

	private KeywordsFastMap keywordsMap;
	
	Tokenizer(Reader source) {
		currentLine = 1;
		
		reader = new JsSourceReader(source);
		current = head = new ArrayToken();
		keywordsMap = new KeywordsFastMap();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#current()
	 */
	public Token current() {
		if (current == head) {
			return null;
		}
		return current;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#next()
	 */
	public Token next() throws JsParseException {
		if (current == null) {
			return null;
		} else if (current.next == null) {
			current.next = readNext();
		}
		current = current.next;
		return current();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#previous()
	 */
	public Token previous() throws JsParseException {
		if (current == null) {
			current = head.previous;
		} else if (current.previous == head) {
			throw new JsParseException("Already reach the begining of the stream or commit index");
		} else {
			current = current.previous;
		}
		return current();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#commit()
	 */
	public void commit() {
		if (current == null) {
			current = head;
			head.next = null;
			head.previous = head;
			return;
		}
		if (current == head) {
			return;
		}
		current.previous = head;
		head.next = current;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.internal.TokenSource#rollback(com.facedev.js.parser.Token)
	 */
	public void rollback(Token to) {
		if (to == null) {
			throw new NullPointerException("Unable to rollback to NULL token");
		}
		if (!(to instanceof ArrayToken)) {
			throw new IllegalArgumentException("This type of tokens cannot be accepted by tokenizer: " + to.getClass().getName());
		}
		current = (ArrayToken)to;
	}

	/**
	 * Array-based implementation of {@link Token} interface.
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	private static class ArrayToken implements Token {
		
		private ArrayToken next;
		private ArrayToken previous;
		
		private char[] array;
		private int line;
		private int offset;
		private int type;
		
		private String keyword;

		ArrayToken(char[] array, int type, int line, int offset, String keyword) {
			this.array = array;
			this.line = line;
			this.offset = offset;
			this.type = type;
			this.keyword = keyword;
		}

		// internal for head token
		// should never be returned to outside.
		ArrayToken() {
			previous = this;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#equalsTo(java.lang.CharSequence)
		 */
		public boolean equalsTo(CharSequence str) {
			if (array.length != str.length()) {
				return false;
			}

			for (int i = 0; i < array.length; i++) {
				if (array[i] != str.charAt(i)) {
					return false;
				}
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#equalsTo(char)
		 */
		public boolean equalsTo(char c) {
			return array.length == 1 && array[0] == c;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isIdentifier()
		 */
		public boolean isIdentifier() {
			return (type & TOKEN_IDENTIFIER) > 0;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isDigitLiteral()
		 */
		public boolean isDigitLiteral() {
			return (type & TOKEN_DIGIT_LITERAL) > 0;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isStringLiteral()
		 */
		public boolean isStringLiteral() {
			return (type & TOKEN_STRING_LITERAL) > 0;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isRegex()
		 */
		public boolean isRegex() {
			return (type & TOKEN_REGEX) > 0;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isComment()
		 */
		public boolean isComment() {
			return (type & TOKEN_COMMENT) > 0;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isKeyword(java.lang.String)
		 */
		public boolean isKeyword(String keyword) {
			return this.keyword == keyword;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#isExpressionEnd()
		 */
		public boolean isExpressionEnd() {
			return (type & TOKEN_DELIMITER) > 0;
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
			return offset;
		}

		/*
		 * (non-Javadoc)
		 * @see com.facedev.js.parser.Token#writeTo(java.lang.StringBuilder)
		 */
		public void writeTo(StringBuilder result) {
			result.append(array);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.CharSequence#length()
		 */
		public int length() {
			return array.length;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.CharSequence#charAt(int)
		 */
		public char charAt(int index) {
			return array[index];
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.CharSequence#subSequence(int, int)
		 */
		public CharSequence subSequence(int start, int end) {
			return new String(array, start, end - start);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return keyword == null ? new String(array) : keyword;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return 31 * line + offset;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof ArrayToken) {
				ArrayToken other = (ArrayToken) obj;
				return Arrays.equals(other.array, array);
			}
			return false;
		}
	}

	private ArrayToken readNext() throws JsParseException {
		try {
			if (nextChar == 0) {
				nextChar = reader.read();
				currentOffset++;
				skipWhiteSpaces();
			}

			if (nextChar < 0) {
				return null;
			}

			int line = currentLine, offset = currentOffset, type = TOKEN_DEFAULT;
			
			StringBuilder result = new StringBuilder();
			String keyword = null;

			result.append((char)nextChar);

			if (CharUtils.isIdentifierStart(nextChar)) {
				keyword = readName(result);
				type = keyword == null ? TOKEN_IDENTIFIER : TOKEN_KEYWORD;
			} else if (nextChar == '\'' || nextChar == '"') {
				readStringLiteral(result, (char) nextChar);
				type = TOKEN_STRING_LITERAL;
			} else if (Character.isDigit(nextChar)) {
				readDigit(result);
				type = TOKEN_DIGIT_LITERAL;
			} else if (nextChar == '/') {
				nextChar = reader.read();
				currentOffset ++;
				result.append((char)nextChar);
				if (nextChar == '*') {
					readMultilineComment(result);
					type = TOKEN_COMMENT;
				} else if (nextChar == '/') {
					readSingleLineComment(result);
					type = TOKEN_COMMENT;
				} else if (previousNonCommentType == TOKEN_DEFAULT ||
						previousNonCommentType == TOKEN_DELIMITER) {
					readRegexLiteral(result);
					type = TOKEN_REGEX;
				} else {
					readComplexOperator(result);
				}
			} else if (CharUtils.isExpressionDelimiter(nextChar)) {
				type = TOKEN_DELIMITER;
				readExpressionDelimiter();
			} else if (nextChar == '.') {
				nextChar = reader.read();
				if (Character.isDigit(nextChar)) {
					result.append((char)nextChar);
					readDigit(result);
					type = TOKEN_DIGIT_LITERAL;
				}
			} else {
				readComplexOperator(result);
			}

			skipWhiteSpaces();

			char[] rez = new char[result.length()];
			result.getChars(0, rez.length, rez, 0);

			ArrayToken token = new ArrayToken(rez, type, line, offset, keyword);
			
			current.next = token;
			head.previous = token;
			token.previous = current;
			
			if (type != TOKEN_COMMENT) {
				previousNonCommentType = type;
			}
			
			return token;
		} catch (IOException ex) {
			throw new JsParseException(ex);
		}
	}

	private void readExpressionDelimiter() throws IOException {
		int prev = nextChar;
		nextChar = reader.read();
		
		if (prev == '\r' && nextChar == '\n') {
			nextChar = reader.read();
		}
		currentOffset = 1;
		currentLine++;
	}

	private void readRegexLiteral(StringBuilder result) throws IOException {
		int c;

		boolean escape = false;
		while ((c = reader.read()) >= 0) {
			if (c == '\r' || c == '\n') {
				nextChar = c;
				return;
			}
			result.append((char)c);
			
			if (escape) {
				escape = false;
				continue;
			}
			if (c == '\\') {
				escape = true;
			} else if (c == '/') {
				break;
			}
		}
		
		if (c < 0) {
			nextChar = c;
			return;
		}
		
		while ((c = reader.read()) >= 0 && CharUtils.isLetter(c)) {
			result.append((char)c);
		}

		nextChar = c;
	}

	private void readComplexOperator(StringBuilder result) throws IOException {
		if (result.length() == 2) {
			// Handle "tail" from comments parsing block

			if (result.charAt(1) == '=') {
				nextChar = reader.read();
				currentOffset ++;
			} else {
				result.deleteCharAt(1);
			}
			return;
		}

		int c = reader.read();
		currentOffset ++;

		if (c != '=' && c != nextChar) {
			nextChar = c;
			return;
		}

		

		if (c == '=') {
			char s = (char) nextChar;

			if (s != '>' && s != '<' && s != '*' && s != '/' && s != '+' &&
				s != '-' && s != '=' && s != '%' && s != '|' && s != '&' &&
				s != '^' && s != '!') {
				nextChar = c;
				return;
			}
			
			nextChar = reader.read();
			result.append((char)c);
			
			if ((s == '!' || s == '=') && nextChar == '=') {
				result.append((char)nextChar);
				nextChar = reader.read();
			}
			
			return;
		}
		
		result.append((char)c);

		if (c == '+' || c == '-' || c == '&' || c == '|') {
			nextChar = reader.read();
			return;
		}

		if (c != '>' && c != '<') {
			result.deleteCharAt(result.length() - 1);
			nextChar = c;
			return;
		}

		c = reader.read();
		currentOffset ++;

		if (c != '=' && c != '>') {
			nextChar = c;
			return;
		}

		result.append((char)c);

		if (c == '=') {
			nextChar = reader.read();
			return;
		}

		c = reader.read();
		currentOffset ++;

		if (c == '=') {
			result.append((char)c);
			c = reader.read();
			currentOffset ++;
		}

		nextChar = c;
	}

	private void readDigit(StringBuilder result) throws IOException {
		int c;

		while ((c = reader.read()) >= 0 &&
				(CharUtils.isIdentifierPart(c) || c == '.')) {
			result.append((char)c);
		}

		nextChar = c;
	}

	private void readStringLiteral(StringBuilder result, char quote) throws IOException {
		int c;

		boolean escaped = false;
		while ((c = reader.read()) >= 0) {

			if (c == '\n' || c == '\r') {
				nextChar = reader.read();
				return;
			}

			result.append((char)c);

			if (escaped) {
				escaped = false;
			} else if(c == '\\') {
				escaped = true;
			} else if(c == quote) {
				nextChar = reader.read();
				return;
			}
		}
	}

	private void readSingleLineComment(StringBuilder result) throws IOException {
		int c;
		while ((c = reader.read()) >= 0 && c != '\n' && c != '\r') {
			currentOffset++;
			result.append((char)c);
		}

		currentOffset++;
		nextChar = c;
	}

	private void readMultilineComment(StringBuilder result) throws IOException {
		int c, prev = 0;

		boolean flag = false;

		while ((c = reader.read()) >= 0) {
			currentOffset++;
			result.append((char)c);
			if (prev == '\r' && c == '\n') {
				continue;
			}

			if (c == '\n' || c == '\r') {
				currentOffset = 1;
				currentLine++;
			}

			if (c == '*') {
				flag = true;
			} else if (c == '/' && flag) {
				nextChar = reader.read();
				return;
			} else {
				flag = false;
			}

			prev = c;
		}
	}

	private String readName(StringBuilder result) throws IOException {
		int c;

		while ((c = reader.read()) >= 0 && CharUtils.isIdentifierPart(c)) {
			currentOffset++;
			result.append((char)c);
		}

		currentOffset++;
		nextChar = c;
		
		return keywordsMap.get(result);
	}

	private void skipWhiteSpaces() throws IOException {
		int c = nextChar;

		while (c >= 0 && CharUtils.isWhitespace(c)) {
			currentOffset++;
			c = reader.read();
		}

		nextChar = c;
	}
}
