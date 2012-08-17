package com.facedev.testdev.model;

public class TestCaseStep {
	
	private TestCaseStepType type;
	private String description;
	private String includePath;
	
	public TestCaseStep() {
	}

	public TestCaseStep(TestCaseStepType type, String description, String includePath) {
		this.type = type;
		this.description = description;
		this.includePath = includePath;
	}

	public TestCaseStepType getType() {
		return type;
	}

	public void setType(TestCaseStepType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIncludePath() {
		return includePath;
	}

	public void setIncludePath(String includePath) {
		this.includePath = includePath;
	}
}
