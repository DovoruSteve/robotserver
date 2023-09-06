package au.com.dovoru.robot.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler so a neat error object is returned to the client
 * @author stephen
 *
 */
record ApiError(int status, String message) {}

@RestControllerAdvice
public class RobotResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { IllegalArgumentException.class, MissingParameterException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
