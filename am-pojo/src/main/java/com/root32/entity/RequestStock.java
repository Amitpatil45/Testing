package com.root32.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class RequestStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	List<RequestStockData> requestDatas = new ArrayList<>();

	private RequestStatus requestStatus;

	@ManyToOne
	@JsonIgnore
	private Org toOrg;

	@ManyToOne
	@JsonIgnoreProperties({ "pocFirstName", "pocLastName", "emailId", "mobileNumber", "orgTypeEnum", "isActive",
			"orgCode", "isHavingRecord", "address", "createdBy", "updatedBy", "parent", "createdDate", "updateDate",
			"commissionInPercent" })
	private Org fromOrg;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
			"isOrgAdminUser" })
	private User createdBy;

	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public List<RequestStockData> getRequestDatas() {
		return requestDatas;
	}

	public void setRequestDatas(List<RequestStockData> requestDatas) {
		this.requestDatas = requestDatas;
	}

	public Org getToOrg() {
		return toOrg;
	}

	public void setToOrg(Org toOrg) {
		this.toOrg = toOrg;
	}

	public Org getFromOrg() {
		return fromOrg;
	}

	public void setFromOrg(Org fromOrg) {
		this.fromOrg = fromOrg;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
