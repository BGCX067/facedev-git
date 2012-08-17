package com.facedev.testdev.editors;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.facedev.testdev.model.TestCase;
import com.facedev.utils.BeanUtils;


public class GeneralTestCaseInformationTab extends Composite {
	private Text nameField;
	private Text versionField;
	private DateTime creationDateField;
	
	GeneralTestCaseInformationTab(Composite parent, final TestCase testcase) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(2, false));
		
		Label lblTestcaseName = new Label(this, SWT.NONE);
		lblTestcaseName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTestcaseName.setText("Testcase name:");
		
		nameField = new Text(this, SWT.BORDER);
		GridData nameLayoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		nameLayoutData.widthHint = 150;
		nameField.setLayoutData(nameLayoutData);
		BeanUtils.bind(testcase, this, "name", "testcaseName");
		
		Label lblVersion = new Label(this, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText("Version:");
		
		versionField = new Text(this, SWT.BORDER);
		GridData versionLayoutData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		versionLayoutData.widthHint = 150;
		versionField.setLayoutData(versionLayoutData);
		BeanUtils.bind(testcase, this, "version", "testcaseVersion");
		
		Label lblCreationDate = new Label(this, SWT.NONE);
		lblCreationDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCreationDate.setText("Creation date:");
		
		creationDateField = new DateTime(this, SWT.BORDER);
		creationDateField.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		BeanUtils.bind(testcase, this, "creationDate", "testcaseCreationDate");
	}

	public String getTestcaseName() {
		return nameField.getText();
	}

	public void setTestcaseName(String name) {
		nameField.setText(name == null ? "" : name);
	}
	
	public String getTestcaseVersion() {
		return versionField.getText();
	}

	public void setTestcaseVersion(String version) {
		versionField.setText(version == null ? "" : version);
	}
	
	public Date getTestcaseCreationDate() {
		Calendar calendar = new GregorianCalendar(creationDateField.getYear(),
				creationDateField.getMonth(), creationDateField.getDay());
		return calendar.getTime();
	}

	public void setTestcaseCreationDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		creationDateField.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
	}
}
