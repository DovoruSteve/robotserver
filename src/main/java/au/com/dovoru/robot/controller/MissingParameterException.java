package au.com.dovoru.robot.controller;

public class MissingParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MissingParameterException(String message) {
		super(message);
	}

	public MissingParameterException(String message, Throwable cause) {
		super(message, cause);
	}
}
