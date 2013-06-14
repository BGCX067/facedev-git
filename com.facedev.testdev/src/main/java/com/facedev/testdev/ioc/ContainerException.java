package com.facedev.testdev.ioc;

public class ContainerException extends RuntimeException {
	private static final long serialVersionUID = -6581548337917652868L;

	public ContainerException() {
		super();
	}

	public ContainerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContainerException(String message) {
		super(message);
	}

	public ContainerException(Throwable cause) {
		super(cause);
	}
}
