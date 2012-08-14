package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class TestCaseRunsTab extends Composite {

	public TestCaseRunsTab(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(BorderLayout.NORTH);
		composite.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		Button btnNewRun = new Button(composite, SWT.NONE);
		btnNewRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewRun.setText("New");
		
		Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.setText("Remove");
		
		Tree tree = new Tree(this, SWT.BORDER);
		tree.setLayoutData(BorderLayout.CENTER);
		
		TreeItem item = new TreeItem(tree, SWT.NULL);
	    item.setText("ITEM");
	    
	    TreeItem item2 = new TreeItem(item, SWT.NULL);
	    item2.setText("ITEM2");
	    
	    TreeItem item3 = new TreeItem(item2, SWT.NULL);
	    item3.setText("ITEM3");
	}
}
