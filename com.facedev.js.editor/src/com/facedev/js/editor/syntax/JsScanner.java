package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.facedev.js.editor.appearance.ColorManager;

/**
 * Default javascript source scanner.
 * Provides syntax coloring for keywords and literals.
 * 
 * @author alex.bereznevatiy@gmail.com
 * 
 */
public class JsScanner extends RuleBasedScanner {
	
	public JsScanner() {
		Color foreground = ColorManager.getInstance().getColor(ColorManager.KEYWORD_COLOR);
		IToken keywordToken = new Token(new TextAttribute(foreground, null, SWT.BOLD));
		
		IToken string = new Token(new TextAttribute(
				ColorManager.getInstance().getColor(ColorManager.LITERAL_COLOR)));
		
		final JsKeyword[] values = JsKeyword.values();
		final int length = values.length + 3;
		IRule[] rules = new IRule[length];
		
		rules[0] = new SingleLineRule("\"", "\"", string, '\\');
		rules[1] = new SingleLineRule("'", "'", string, '\\');
		rules[2] = new WhitespaceRule(new WhitespaceDetector());
		
		for (int i = 3; i < length; i++) {
			rules[i] = new StringMatchRule(values[i - 3].getKeyword(), keywordToken);
		}

		setRules(rules);
	}
}