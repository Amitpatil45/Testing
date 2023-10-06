package com.root32.userservice.dto;

import java.util.ArrayList;
import java.util.List;

import com.root32.dto.GenericResponseEntity;
import com.root32.entity.OrgTypeEnum;

public class LoginResponse {

	private String token;
	private Long userId;
	private String emailId;
	private OrgTypeEnum orgType;
	private Long orgId;
	private String orgName;
	private String userFirstName;
	private String UserLastName;
	private List<String> authorities = new ArrayList<>();
	private Boolean isPasswordUpdate;
	private GenericResponseEntity genericResponseEntity;

	public GenericResponseEntity getGenericResponseEntity() {
		return genericResponseEntity;
	}

	public void setGenericResponseEntity(GenericResponseEntity genericResponseEntity) {
		this.genericResponseEntity = genericResponseEntity;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void addAuthority(String authority) {
		this.authorities.add(authority);
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return UserLastName;
	}

	public void setUserLastName(String userLastName) {
		UserLastName = userLastName;
	}

	public OrgTypeEnum getOrgType() {
		return orgType;
	}

	public void setOrgType(OrgTypeEnum orgType) {
		this.orgType = orgType;
	}

	public Boolean getIsPasswordUpdate() {
		return isPasswordUpdate;
	}

	public void setIsPasswordUpdate(Boolean isPasswordUpdate) {
		this.isPasswordUpdate = isPasswordUpdate;
	}

}
