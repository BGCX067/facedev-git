package com.facedev.js.editor.behavior;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;

/**
 * DamagerRepair implementation for comments and javascript documentation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsDefaultDamagerRepairer extends DefaultDamagerRepairer {
	
	protected TextAttribute defaultAttribute;

	public JsDefaultDamagerRepairer(ITokenScanner scanner, TextAttribute defaultAttribute) {
		super(scanner);
		this.defaultAttribute = defaultAttribute;
	}

	protected TextAttribute getTokenTextAttribute(IToken token) {
		Object data= token.getData();
		if (data instanceof TextAttribute)
			return (TextAttribute) data;
		return defaultAttribute;
	}

}
