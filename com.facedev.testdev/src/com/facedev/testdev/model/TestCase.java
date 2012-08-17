package com.facedev.testdev.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents testcase with all general data, runs and steps references.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class TestCase {
	private String name = "test";
	private String version = "0.0.1";
	private Date creationDate = new Date();
	
	@SuppressWarnings("serial")
	private List<TestCaseStep> steps = new ArrayList<TestCaseStep>() {
		{
			add(new TestCaseStep(TestCaseStepType.RAW, "first step", null));
			add(new TestCaseStep(TestCaseStepType.RAW, "second step", null));
			add(new TestCaseStep(TestCaseStepType.INCLUDE, null, "c:/"));
		}
	};
	
	private PropertyChangeSupport props = new PropertyChangeSupport(this);
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		props.firePropertyChange("name", oldName, name);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		String oldVersion = this.version;
		this.version = version;
		props.firePropertyChange("version", oldVersion, version);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		Date oldDate = this.creationDate;
		this.creationDate = creationDate;
		props.firePropertyChange("creationDate", oldDate, creationDate);
	}

	public List<TestCaseStep> getSteps() {
		return steps;
	}

	public void setSteps(List<TestCaseStep> steps) {
		List<TestCaseStep> oldSteps = this.steps;
		this.steps = steps;
		props.firePropertyChange("steps", oldSteps, steps);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
        props.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        props.removePropertyChangeListener(listener);
    }
}
