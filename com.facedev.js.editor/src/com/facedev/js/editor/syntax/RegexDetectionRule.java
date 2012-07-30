package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.facedev.js.parser.CharUtils;

/**
 * Detect javascript regular expression literals.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class RegexDetectionRule implements IPredicateRule {
	
	private IToken successToken;
	private int cnt;
	private int prev;

	public RegexDetectionRule(IToken successToken) {
		this.successToken = successToken;
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	public IToken getSuccessToken() {
		return successToken;
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			scanToEnd(scanner);
			return successToken;
		}
		
		if (!detectStart(scanner)) {
			return Token.UNDEFINED;
		}
		
		scanToEnd(scanner);

		return successToken;
	}

	private boolean detectStart(ICharacterScanner scanner) {
		if (scanner.read() != '/') {
			scanner.unread();
			return false;
		}
		
		scanner.unread();
		
		if (!checkPrevious((JsPartitionsScanner) scanner)) {
			return false;
		}
		
		scanner.read();
		if (scanner.read() == '/') {
			scanner.unread();
			scanner.unread();
			return false;
		}
		
		return true;
	}

	private boolean checkPrevious(JsPartitionsScanner scanner) {
		cnt = 0;
		prev = 0;
		
		while (!scanner.isBegin()) {
			prev = readPrevious(scanner);
			cnt++;
			if (!Character.isWhitespace(prev)) {
				break;
			}
		}
		
		if (scanner.isBegin()) {
			moveForward(scanner);
			return true;
		}
		
		Boolean result = skipComments(scanner);
		if (result != null) {
			return result;
		}
		
		moveForward(scanner);
		return !CharUtils.isIdentifierPart(prev) && prev != '"' && prev != '\'';
	}

	private Boolean skipComments(JsPartitionsScanner scanner) {
		while (prev == '/') {
			cnt++;
			if (readPrevious(scanner) != '*') {
				moveForward(scanner);
				return false;
			}
			
			skipMultilineCommentBack(scanner);
			
			while (!scanner.isBegin()) {
				prev = readPrevious(scanner);
				cnt++;
				if (!Character.isWhitespace(prev)) {
					break;
				}
			}
			if (scanner.isBegin()) {
				moveForward(scanner);
				return true;
			}
		}
		
		if (skipSingleLineCommentBack(scanner)) {
			return skipComments(scanner);
		}
		
		return null;
	}

	private boolean skipSingleLineCommentBack(JsPartitionsScanner scanner) {
		int cnt = 0;
		while (scanner.getColumn() > 0) {
			cnt++;
			prev = readPrevious(scanner);
			if (prev == '/' && scanner.getColumn() > 0) {
				cnt++;
				prev = readPrevious(scanner);
				if (prev == '/') {
					cnt++;
					prev = readPrevious(scanner);
					this.cnt += cnt;
					return true;
				}
			}
		}
		while (cnt --> 0) scanner.read();
		return false;
	}

	private void skipMultilineCommentBack(JsPartitionsScanner scanner) {
		while (!scanner.isBegin()) {
			prev = readPrevious(scanner);
			cnt++;
			while (prev == '*' && !scanner.isBegin()) {
				prev = readPrevious(scanner);
				cnt++;
				if (prev == '/') {
					return;
				}
			}
		}
	}

	private void moveForward(JsPartitionsScanner scanner) {
		while (cnt --> 0) scanner.read();
	}
	
	private int readPrevious(JsPartitionsScanner scanner) {
		scanner.unread();
		int rez = scanner.read();
		scanner.unread();
		return rez;
	}

	private static boolean scanToEnd(ICharacterScanner scanner) {
		int c;
		boolean escaped = false;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (c == '\n' || c == '\r') {
				scanner.unread();
				return true;
			}
			if (escaped) {
				escaped = false;
				continue;
			}
			if (c == '\\') {
				escaped = true;
				continue;
			}
			if (c == '/') {
				break;
			}
		}
		if (c != '/') {
			return true;
		}
		
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (c == '\n' || c == '\r' || !CharUtils.isLetter(c)) {
				scanner.unread();
				return true;
			}
		}
		
		return true;
	}
}
