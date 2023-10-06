package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class AdminCase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String caseCode;

	private Boolean isSealed;

	private Date createdAt;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
		"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
		"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
		"isOrgAdminUser" })
	private User createdBy;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
		"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
		"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
		"isOrgAdminUser" })
	private User recievedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public Boolean getIsSealed() {
		return isSealed;
	}

	public void setIsSealed(Boolean isSealed) {
		this.isSealed = isSealed;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getRecievedBy() {
		return recievedBy;
	}

	public void setRecievedBy(User recievedBy) {
		this.recievedBy = recievedBy;
	}

}
