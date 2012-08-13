package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class TestCaseStepWidget extends Composite {
	private static final int SKIP_SCROLL_EVENTS_NUMBER = 2;
	
	private Text text;
	private Composite includeProject;

	public TestCaseStepWidget(final Composite parent, int number) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		label.setText(String.valueOf(number) + ".");
		
		final Combo combo = new Combo(this, SWT.NONE);
		combo.add("Text step");
		combo.add("Include testcase");
		combo.select(0);
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Composite container = new Composite(this, SWT.NONE);
		final StackLayout layout = new StackLayout();
		container.setLayout(layout);
		
		createTextField(container, parent);
		createIncludeProjectField(container);
		
		layout.topControl = text;
		
		final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.heightHint = 50;
		container.setLayoutData(gd_text);
		
		combo.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				layout.topControl = combo.getSelectionIndex() > 0 ? includeProject : text;
				TestCaseStepWidget.this.layout();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createIncludeProjectField(Composite container) {
		includeProject = new Composite(container, SWT.NONE);
		includeProject.setLayout(null);
		
		Combo combo = new Combo(includeProject, SWT.NONE);
		combo.setBounds(5, 5, 200, 20);
		combo.add("Test A");
		combo.add("Test B");
	}

	private void createTextField(final Composite container, final Composite parent) {
		text = new Text(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		text.addListener(SWT.MouseWheel, new Listener() {
			
			private int bufferCount = SKIP_SCROLL_EVENTS_NUMBER;
			
			public void handleEvent(Event event) {
				if (event.type!= SWT.MouseWheel) {
					return;
				}
				int num = text.getSize().y / text.getLineHeight();
				ScrollBar vbar = text.getVerticalBar();
		    	if ((event.count > 0 && vbar.getSelection() <= vbar.getMinimum()) ||
		    			(event.count < 0 && vbar.getSelection() >= (vbar.getMaximum() - num))) {
		    		if (bufferCount >= SKIP_SCROLL_EVENTS_NUMBER) {
		    			parent.notifyListeners(event.type, event);
		    		} else {
		    			bufferCount++;
		    		}
		    	} else {
		    		bufferCount = 0;
		    	}
			}
		});
	}

}
