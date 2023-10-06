package com.root32.dentalproductservice.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class UserAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Map<String, String> additonalInfo;

	public UserAuthenticationException(String message) {
		super(message);
	}

	public Map<String, String> getAdditionalInfo() {
		return additonalInfo;
	}

	public void setAdditonalInfo(Map<String, String> additonalInfo) {
		this.additonalInfo = additonalInfo;
	}
}
