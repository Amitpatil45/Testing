package com.root32.dentalproductservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.root32.pojo.ErrorResponseEntity;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final int PRECONDITION_FAILED = 412;

	@ExceptionHandler(DataValidationException.class)
	public ResponseEntity<ErrorResponseEntity> resourceNotFoundException(DataValidationException ex,
			WebRequest request) {
		ErrorResponseEntity message = new ErrorResponseEntity(HttpStatus.PRECONDITION_FAILED.value(), ex.getMessage());

		return new ResponseEntity<ErrorResponseEntity>(message, HttpStatus.PRECONDITION_FAILED);
	}
}
