package com.facedev.testdev.editors;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.FrameworkUtil;

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
				getShell().setMinimized(true);
				RunWindow window = new RunWindow();
				window.open();
			}
		});
		btnNewRun.setText("New");
		
		final Button btnContinue = new Button(composite, SWT.NONE);
		btnContinue.setEnabled(false);
		btnContinue.setText("Continue");
		btnContinue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().setMinimized(true);
				RunWindow window = new RunWindow();
				window.open();
			}
		});
		
		final Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.setEnabled(false);
		btnRemove.setText("Remove");
		
		final Tree tree = new Tree(this, SWT.BORDER | SWT.SINGLE);
		tree.setLayoutData(BorderLayout.CENTER);
		
		final TreeItem item = new TreeItem(tree, SWT.NULL);
	    item.setText("Revision 1");
	    item.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
	    
	    URL url = FrameworkUtil.getBundle(getClass()).getResource("icons/tstrun.png");
	    
	    final TreeItem item2 = new TreeItem(item, SWT.NULL);
	    item2.setText("Run 1 [15.08.2012 14:45]");
	    item2.setImage(ImageDescriptor.createFromURL(url).createImage());
	    
	    final TreeItem item3 = new TreeItem(item, SWT.NULL);
	    item3.setText("Run 2 [15.08.2012 15:04]");
	    item3.setImage(ImageDescriptor.createFromURL(url).createImage());
	    
	    tree.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				
				if (tree.getSelection().length == 0 ||
						tree.getSelection()[0] == item) {
					btnContinue.setEnabled(false);
					btnRemove.setEnabled(false);
					return;
				}
				btnContinue.setEnabled(true);
				btnRemove.setEnabled(true);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
