package com.facedev.testdev.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.facedev.testdev.model.TestCase;

/**
 * Provides UI for editing and running testcases.
 * Page 1 is general testcase information.
 * Page 2 is the list of steps
 * Page 3 is the run history.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class TestCaseEditor extends MultiPageEditorPart {
	
	private TestCase testcase;
	
	private GeneralTestCaseInformationTab generalInfoTab;
	private TestCaseStepsTab stepsTab;

	public TestCaseEditor() {
		super();
		this.testcase = new TestCase();
	}

	@Override
	protected void createPages() {
		createGeneralInformationPage();
		createStepsPage();
		createRunsPage();
	}

	private void createGeneralInformationPage() {
		generalInfoTab = new GeneralTestCaseInformationTab(getContainer());
		
		int index = addPage(generalInfoTab);
		setPageText(index, "General");
	}
	
	private void createStepsPage() {
		stepsTab = new TestCaseStepsTab(getContainer());		
		int index = addPage(stepsTab);
		setPageText(index, "Steps");
	}
	
	private void createRunsPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 4;

		Label label = new Label(composite, SWT.NONE);
		
		GridData gd = new GridData(GridData.BEGINNING);
//		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		label.setText("Runs:");
		
		int index = addPage(composite);
		setPageText(index, "Runs");
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
//		getEditor(0).doSave(monitor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
//		IEditorPart editor = getEditor(0);
//		editor.doSaveAs();
//		setPageText(0, editor.getTitle());
//		setInput(editor.getEditorInput());
	}
	
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (editorInput instanceof IFileEditorInput) {
			super.init(site, editorInput);
		} else {
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		}
		
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
}
