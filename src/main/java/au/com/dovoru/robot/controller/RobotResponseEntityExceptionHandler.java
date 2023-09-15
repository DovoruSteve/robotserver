package au.com.dovoru.robot.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler so a neat error object is returned to the client
 * 
 * @author stephen
 *
 */

record ApiError(int status, String message) {
}

@RestControllerAdvice
public class RobotResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private final Logger logger = Logger.getLogger(RobotResponseEntityExceptionHandler.class.getSimpleName());

	@ExceptionHandler(value = { IllegalArgumentException.class, })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		logger.log(Level.WARNING, "Caught exception {0} - returning status {1}",
				new Object[] { ex.getMessage(), HttpStatus.BAD_REQUEST });
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        StringBuilder errorMessage = new StringBuilder();
        boolean semiRequired = false;
        for (ObjectError error : allErrors) {
        	if (semiRequired) {
        		errorMessage.append("; ");
        	} else {
        		semiRequired = true;
        	}
            errorMessage.append(error.getDefaultMessage());
        }
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
		logger.log(Level.WARNING, "Caught exception {0} - returning status {1}",
				new Object[] { ex.getMessage(), HttpStatus.BAD_REQUEST });
		return new ResponseEntity<>(apiError, status);
	}
}
