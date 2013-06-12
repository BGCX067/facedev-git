package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

import com.facedev.js.editor.appearance.ColorManager;

/**
 * Styles scanner for javascript doc.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public class JsDocsScanner extends RuleBasedScanner {
	
	public JsDocsScanner() {
		IToken docToken = new Token(new TextAttribute(
				ColorManager.getInstance().getColor(ColorManager.JSDOC_COLOR)));
		IRule[] rules = new IRule[1];
		
		rules[0] = new MultiLineRule("/**", "*/", docToken, (char)0, true);
		
		setRules(rules);
	}
}