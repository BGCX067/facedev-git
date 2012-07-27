package com.facedev.js.editor.behavior;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

/**
 * Strategy to be applied on double click on source code.
 * If target token is name it should be selected.
 * If target is literal it should be selected including all additional symbols (like quotation).
 * Whitespaces also should be selected on double click (multiline are also applied).
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class DoubleClickStrategy implements ITextDoubleClickStrategy {

	public void doubleClicked(ITextViewer viewer) {
		// TODO Auto-generated method stub
		
	}

}