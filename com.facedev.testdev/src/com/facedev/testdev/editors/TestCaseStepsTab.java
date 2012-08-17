package com.facedev.testdev.editors;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.facedev.testdev.model.TestCase;
import com.facedev.testdev.model.TestCaseStep;
import com.facedev.utils.BeanUtils;

public class TestCaseStepsTab extends ScrolledComposite {
	
	private Composite stepsComposite;
	private List<TestCaseStep> testcaseSteps;

	public TestCaseStepsTab(Composite parent, final TestCase testcase) {
		super(parent, SWT.V_SCROLL);
		setLayout(new FillLayout());
		stepsComposite = new Composite(this, SWT.NONE);
		stepsComposite.setLayout(new GridLayout(1, false));
		setContent(stepsComposite);

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
		stepsComposite.addListener(SWT.MouseWheel, listener);
		
		BeanUtils.bind(testcase, this, "steps", "testcaseSteps");
	}

	public List<TestCaseStep> getTestcaseSteps() {
		return testcaseSteps;
	}

	public void setTestcaseSteps(List<TestCaseStep> testcaseSteps) {
		this.testcaseSteps = testcaseSteps;
		int index = 1;
		for (TestCaseStep step : testcaseSteps) {
			TestCaseStepWidget stepWidget = new TestCaseStepWidget(stepsComposite, step, index++);
			stepWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		}
		
		setMinSize(stepsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
}
