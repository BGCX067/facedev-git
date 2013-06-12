package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.facedev.js.editor.appearance.ColorManager;

/**
 * Handles javascript string and regexp literals.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsLiteralsScanner extends RuleBasedScanner {
	public JsLiteralsScanner() {
		IToken literal = new Token(new TextAttribute(
				ColorManager.getInstance().getColor(ColorManager.LITERAL_COLOR)));
		
		IRule[] rules = new IRule[2];
		
		rules[0] = new SingleLineRule("\"", "\"", literal, '\\', true);
		rules[1] = new SingleLineRule("'", "'", literal, '\\', true);
		
		setRules(rules);
	}
}
