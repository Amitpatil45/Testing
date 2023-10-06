package com.root32.onboarding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.root32.pojo.ErrorResponseEntity;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final int PRECONDITION_FAILED = 412;

	@ExceptionHandler(OnboardingServiceException.class)
	public ResponseEntity<ErrorResponseEntity> handleProcurementServiceException(
			OnboardingServiceException onboardingServiceException) {
		ErrorResponseEntity errorResponseEntity = new ErrorResponseEntity(PRECONDITION_FAILED,
				onboardingServiceException.getMessage());
		return new ResponseEntity<>(errorResponseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorResponseEntity> handleDuplicateRecordException(RecordNotFoundException dre) {
		String message = dre.getMessage();
		ErrorResponseEntity responseEntity = new ErrorResponseEntity(PRECONDITION_FAILED, message);
		return new ResponseEntity<>(responseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(ParamServiceException.class)
	public ResponseEntity<ErrorResponseEntity> handleParamServiceException(ParamServiceException pse) {
		ErrorResponseEntity errorResponseEntity = new ErrorResponseEntity(PRECONDITION_FAILED, pse.getMessage());
		return new ResponseEntity<>(errorResponseEntity, HttpStatus.PRECONDITION_FAILED);
	}
}
