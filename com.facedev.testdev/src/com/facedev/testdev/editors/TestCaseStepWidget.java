package com.facedev.testdev.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.facedev.testdev.model.TestCaseStep;

import swing2swt.layout.FlowLayout;

public class TestCaseStepWidget extends Composite {
	private static final int SKIP_SCROLL_EVENTS_NUMBER = 2;
	
	private Text text;
	private Composite includeTest;
	private Label selectedTestLabel;

	public TestCaseStepWidget(final Composite parent, TestCaseStep step, int number) {
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

		final Composite container = new Composite(this, SWT.NONE);
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
				layout.topControl = combo.getSelectionIndex() > 0 ? includeTest : text;
				container.layout();
				TestCaseStepWidget.this.layout();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createIncludeProjectField(final Composite container) {
		includeTest = new Composite(container, SWT.NONE);
		includeTest.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		Button btnOpenFile = new Button(includeTest, SWT.NONE);
		btnOpenFile.setText("Open File");
		
		btnOpenFile.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setText("Select testcase file");
				String file = dialog.open();
				if (file == null || file.length() == 0) {
					return;
				}
				selectedTestLabel.setText(file);
				container.layout();
				System.out.println("File: " + file);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		
		Button btnOpenWorkspace = new Button(includeTest, SWT.NONE);
		btnOpenWorkspace.setText("Open workspace");
		
		btnOpenWorkspace.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), 
						new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider());
				dialog.setTitle("Select testcase file");
				dialog.setMessage("Select testcase to include:");
				dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
				dialog.open();
				IFile file = (IFile) dialog.getFirstResult();
				if (file == null) {
					return;
				}
				
				selectedTestLabel.setText(file.getFullPath().toOSString());
				container.layout();
				System.out.println("Workspace: " + file.getRawLocation().toOSString());
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		selectedTestLabel = new Label(includeTest, SWT.NONE);
		selectedTestLabel.setText("Not selected...");
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
