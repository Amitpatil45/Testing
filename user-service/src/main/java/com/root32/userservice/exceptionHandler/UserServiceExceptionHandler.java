package com.root32.userservice.exceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.root32.configsvc.exception.EmailMessageException;
import com.root32.pojo.ErrorResponseEntity;
import com.root32.userservice.exception.UserAuthenticationException;

@RestControllerAdvice
public class UserServiceExceptionHandler {

	private static final int PRECONDITION_FAILED = 412;
	private static final int SERVER_ERROR = 500;

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ErrorResponseEntity> handleSQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException sqlICE) {

		String errorMessage = sqlICE.getMessage();
		String sqlState = sqlICE.getSQLState();
		ErrorResponseEntity responseEntity = new ErrorResponseEntity(PRECONDITION_FAILED, errorMessage, sqlState);
		return new ResponseEntity<>(responseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseEntity> handleDataIntegrityViolationException(
			DataIntegrityViolationException dataIVE) {

		String errorMessage = dataIVE.getMessage();
		Throwable rootCause = dataIVE.getRootCause();
		String rootCauseMessage = rootCause != null ? rootCause.getMessage() : errorMessage;
		ErrorResponseEntity responseEntity = new ErrorResponseEntity(PRECONDITION_FAILED, rootCauseMessage);
		return new ResponseEntity<>(responseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(UserAuthenticationException.class)
	public ResponseEntity<ErrorResponseEntity> handleUserAuthenticationException(UserAuthenticationException uae) {

		String message = uae.getMessage();
		ErrorResponseEntity responseEntity = new ErrorResponseEntity(PRECONDITION_FAILED, message);
		responseEntity.setAdditionalData(uae.getAdditionalInfo());
		return new ResponseEntity<>(responseEntity, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(EmailMessageException.class)
	public ResponseEntity<ErrorResponseEntity> handleEmailMessageException(EmailMessageException eme) {
		String message = "SysError : EM1001 - OTP couldn't be sent on Email. If you continue to get this error then please contact our support team.";
		ErrorResponseEntity responseEntity = new ErrorResponseEntity(SERVER_ERROR, message);
		return new ResponseEntity<>(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
