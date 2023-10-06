package com.root32.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class GenericResponseEntity {

	private Integer code;
	private String message;
	private Boolean authVerified;
	private Long id;

	public GenericResponseEntity(String message) {
		this.message = message;
	}

	public GenericResponseEntity(Integer code, String message) {

		this.code = code;
		this.message = message;
	}
	
	public GenericResponseEntity(Integer code, String message, Long id) {

		this.code = code;
		this.message = message;
		this.id = id;
	}

	public GenericResponseEntity(Integer code, String message, Boolean authVerified) {

		this.code = code;
		this.message = message;
		this.authVerified = authVerified;
	}

	public GenericResponseEntity() {
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getAuthVerified() {
		return authVerified;
	}

	public void setAuthVerified(Boolean authVerified) {
		this.authVerified = authVerified;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
