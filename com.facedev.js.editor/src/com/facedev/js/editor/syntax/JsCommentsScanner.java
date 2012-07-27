package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.facedev.js.editor.appearance.ColorManager;

/**
 * Provides syntax coloring for comments.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsCommentsScanner extends RuleBasedScanner {
	
	public JsCommentsScanner() {
		IToken comment = new Token(new TextAttribute(
				ColorManager.getInstance().getColor(ColorManager.COMMENT_COLOR)));
		IRule[] rules = new IRule[2];
		
		rules[0] = new MultiLineRule("/*", "*/", comment);
		rules[1] = new SingleLineRule("//", "\n", comment);
		//rules[2] = new SingleLineRule("//", "\r", comment);
		
		setRules(rules);
	}
}