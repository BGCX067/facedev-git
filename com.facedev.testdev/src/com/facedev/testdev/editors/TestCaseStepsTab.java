package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TestCaseStepsTab extends ScrolledComposite {
	

	public TestCaseStepsTab(Composite parent) {
		super(parent, SWT.V_SCROLL);
		setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		for (int i = 1; i < 15; i++) {
			TestCaseStepWidget step = new TestCaseStepWidget(composite, i);
			step.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		}
		
		setContent(composite);
		setMinSize(400, 400);
		setExpandHorizontal(true);
		setExpandVertical(true);
	}

}
