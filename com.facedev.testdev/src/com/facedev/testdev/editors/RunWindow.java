package com.facedev.testdev.editors;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Button;
import swing2swt.layout.FlowLayout;

public class RunWindow extends ApplicationWindow {
	private static final int DEFAULT_WIDTH = 300;
	private static final int DEFAULT_HEIGHT = 400;
	private Text txtActual;
	
	protected RunWindow() {
		super(new Shell(Display.getCurrent(), SWT.CLOSE | SWT.TITLE | SWT.RESIZE | SWT.ON_TOP));
		setBlockOnOpen(false);
	}

	public int open() {
		int result = super.open();
		getShell().forceActive();
		Rectangle client = Display.getCurrent().getClientArea();
		getShell().setLocation(client.width - DEFAULT_WIDTH - 5, 
				client.height - DEFAULT_HEIGHT - 5);
		getShell().setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		return result;
	}
	
	protected Control createContents(Composite parent) {
		Composite result = new Composite(parent, SWT.NONE);
		result.setLayout(new FillLayout(SWT.VERTICAL));
		
		Label lblStepDescription = new Label(result, SWT.NONE);
		lblStepDescription.setText("This is step");
		
		Composite compositeExpected = new Composite(result, SWT.NONE);
		compositeExpected.setLayout(new BorderLayout());
		
		Label lblExpected = new Label(compositeExpected, SWT.NONE);
		lblExpected.setText("Expected:");
		lblExpected.setLayoutData(BorderLayout.NORTH);
		
		Label lblExpectedDescription = new Label(compositeExpected, SWT.NONE);
		lblExpectedDescription.setText("This is expected");
		lblExpectedDescription.setLayoutData(BorderLayout.CENTER);
		
		Composite compositeActual = new Composite(result, SWT.NONE);
		compositeActual.setLayout(new BorderLayout());
		
		Label lblActual = new Label(compositeActual, SWT.NONE);
		lblActual.setText("Actual:");
		lblActual.setLayoutData(BorderLayout.NORTH);
		
		txtActual = new Text(compositeActual, SWT.BORDER);
		txtActual.setLayoutData(BorderLayout.CENTER);
		
		Composite buttons = new Composite(compositeActual, SWT.NONE);
		buttons.setLayoutData(BorderLayout.SOUTH);
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		Button btnPassed = new Button(buttons, SWT.NONE);
		btnPassed.setText("Passed");
		
		Button btnFailed = new Button(buttons, SWT.NONE);
		btnFailed.setText("Failed");
		
		Button btnNa = new Button(buttons, SWT.NONE);
		btnNa.setText("N/A");
		
		return result;
	}
}
