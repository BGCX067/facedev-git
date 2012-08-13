package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TestCaseStepsTab extends ScrolledComposite {
	

	public TestCaseStepsTab(Composite parent) {
		super(parent, SWT.V_SCROLL);
		setLayout(new FillLayout());
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		setContent(composite);
		
		for (int i = 1; i < 15; i++) {
			TestCaseStepWidget step = new TestCaseStepWidget(composite, i);
			step.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		}

		setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		setExpandHorizontal(true);
		setExpandVertical(true);
		
		getVerticalBar().setIncrement(getVerticalBar().getIncrement()*3);
		
		Listener listener = new Listener() {
		    public void handleEvent(Event event) {
			    switch (event.type) {
			    case SWT.MouseWheel:
		            int amount = event.count * 10;
			        setOrigin(0, getOrigin().y - amount);
			    }
		    }
		};
		composite.addListener(SWT.MouseWheel, listener);
	}

}
