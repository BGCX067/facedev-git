package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TestCaseStepWidget extends Composite {
	private Text text;

	public TestCaseStepWidget(Composite parent, int number) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		label.setText(String.valueOf(number) + ".");
		
		text = new Text(this, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 50;
		text.setLayoutData(gd_text);
	}

}
