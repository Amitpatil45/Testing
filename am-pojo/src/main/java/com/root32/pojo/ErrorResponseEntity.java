package com.root32.pojo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorResponseEntity {

	private int errorCode;
	private String errorMessage;
	private String sqlState;
	private Map<String, String> additionalData;

	public ErrorResponseEntity(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ErrorResponseEntity(int errorCode, String errorMessage, String sqlState) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.sqlState = sqlState;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSqlState() {
		return sqlState;
	}

	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}

	public Map<String, String> getAdditionalData() {
		return additionalData;
	}
	
	public void setAdditionalData(Map<String, String> additionalData) {
		this.additionalData = additionalData;
	}
}