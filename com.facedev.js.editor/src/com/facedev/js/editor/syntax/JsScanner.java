package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.facedev.js.editor.appearance.ColorManager;

/**
 * Default javascript source scanner.
 * Provides syntax coloring for keywords and digit literals.
 * Also handles unterminated strings.
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsScanner extends RuleBasedScanner {
	
	public JsScanner() {
		Color foreground = ColorManager.getInstance().getColor(ColorManager.KEYWORD_COLOR);
		IToken keywordToken = new Token(new TextAttribute(foreground, null, SWT.BOLD));
		
		final JsKeyword[] values = JsKeyword.values();
		final int definedCount = 1;
		final int length = values.length + definedCount;
		IRule[] rules = new IRule[length];
		
		rules[0] = new WhitespaceRule(new WhitespaceDetector());
		
		for (int i = definedCount; i < length; i++) {
			rules[i] = new StringMatchRule(values[i - definedCount].getKeyword(), keywordToken);
		}

		setRules(rules);
	}
}