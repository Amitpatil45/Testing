package com.root32.dentalproductservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class DataValidationException extends RuntimeException {
	// private static final long serialVersionUID = 1L;

	public DataValidationException(String msg) {
		super(msg);
	}
}
