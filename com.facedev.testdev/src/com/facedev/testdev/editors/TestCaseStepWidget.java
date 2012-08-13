package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
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

	public TestCaseStepWidget(final Composite parent, int number) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		label.setText(String.valueOf(number) + ".");
		
		Combo combo = new Combo(this, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Composite container = new Composite(this, SWT.NONE);
		container.setLayout(new StackLayout());
		
		createTextField(container, parent);
		
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text.heightHint = 50;
		container.setLayoutData(gd_text);
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
