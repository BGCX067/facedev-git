package com.facedev.testdev.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;


public class GeneralTestCaseInformationTab extends Composite {
	private Text nameField;
	private Text versionField;
	private DateTime creationDateField;

	GeneralTestCaseInformationTab(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label lblTestcaseName = new Label(this, SWT.NONE);
		lblTestcaseName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTestcaseName.setText("Testcase name:");
		
		nameField = new Text(this, SWT.BORDER);
		GridData nameLayoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		nameLayoutData.widthHint = 150;
		nameField.setLayoutData(nameLayoutData);
		
		Label lblVersion = new Label(this, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText("Version:");
		
		versionField = new Text(this, SWT.BORDER);
		GridData versionLayoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		versionLayoutData.widthHint = 150;
		versionField.setLayoutData(versionLayoutData);
		
		Label lblCreationDate = new Label(this, SWT.NONE);
		lblCreationDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCreationDate.setText("Creation date:");
		
		creationDateField = new DateTime(this, SWT.BORDER);
		creationDateField.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
	}

}
