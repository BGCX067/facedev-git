package com.facedev.js.editor.content;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;

public class JsContentAssistProcessor implements IContentAssistProcessor {

	public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer,
			int documentOffset) {
		IDocument document = textViewer.getDocument();
		
		// TODO: implement parameters
		List<JsContentAssistSuggestion> suggestions = JsSuggesters.query(document, "");
		
		if (suggestions == null || suggestions.isEmpty()) {
			return null;
		}
		
		List<ICompletionProposal> result = new ArrayList<ICompletionProposal>(suggestions.size());

		for (JsContentAssistSuggestion suggestion : suggestions) {
			result.add(createProposal(suggestion));
		}

		return result.toArray(new ICompletionProposal[result.size()]);
	}
	
	private ICompletionProposal createProposal(JsContentAssistSuggestion suggestion) {
		Image image = suggestion.getType() == null ? null : suggestion.getType().getImage();
		return new CompletionProposal(suggestion.getProposal(), 
				suggestion.getReplacementOffset(), 
				suggestion.getReplacementLength(), 
				suggestion.getCursorPosition(), 
				image, 
				suggestion.getDisplayName(), 
				null, 
				null);
	}

	public IContextInformation[] computeContextInformation(ITextViewer textViewer,
			int documentOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
