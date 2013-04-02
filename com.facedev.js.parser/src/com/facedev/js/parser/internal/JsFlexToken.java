package com.facedev.js.parser.internal;

import com.facedev.js.parser.Token;

final class JsFlexToken implements Token {
	
	JsFlexToken(CharSequence data, int state) {
		
	}

	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	public char charAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equalsTo(CharSequence str) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equalsTo(char c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isIdentifier() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isKeyword(String keyword) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isKeyword() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDigitLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStringLiteral() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRegex() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isComment() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isExpressionEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getLine() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

}
