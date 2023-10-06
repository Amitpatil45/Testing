package com.root32.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class AdminStockOutward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties({ "pocFirstName", "pocLastName", "emailId", "mobileNumber", "orgTypeEnum", "isActive",
			"orgCode", "isHavingRecord", "address", "createdBy", "updatedBy", "parent", "createdDate", "updateDate",
			"commissionInPercent" })
	private Org retailer;

	@OneToOne
	@JsonIgnoreProperties({ "isSealed", "createdAt", "createdBy", "recievedBy" })
	private AdminCase adminCase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AdminStockOutwardData> datas = new ArrayList<>();

	private Long totalUnits;

	private CaseStatus caseStatus;

	private Date createdAt;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "contactNumber", "createdDate", "expiryDate", "isLocked", "unsuccessfulAttemps",
			"isActive", "org", "roles", "username", "authorities", "accountNonExpired", "accountNonLocked",
			"credentialsNonExpired", "enabled", "isPasswordUpdate", "isOrgAdminUser" })
	private User createdBy;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "contactNumber", "createdDate", "expiryDate", "isLocked", "unsuccessfulAttemps",
			"isActive", "org", "roles", "username", "authorities", "accountNonExpired", "accountNonLocked",
			"credentialsNonExpired", "enabled", "isPasswordUpdate", "isOrgAdminUser" })
	private User updatedBy;
	private Date updatedAt;
	private Date recievedDate;

	public Date getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}

	public Long getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(Long totalUnits) {
		this.totalUnits = totalUnits;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Org getRetailer() {
		return retailer;
	}

	public void setRetailer(Org retailer) {
		this.retailer = retailer;
	}

	public AdminCase getAdminCase() {
		return adminCase;
	}

	public void setAdminCase(AdminCase adminCase) {
		this.adminCase = adminCase;
	}

	public List<AdminStockOutwardData> getDatas() {
		return datas;
	}

	public void setDatas(List<AdminStockOutwardData> datas) {
		this.datas = datas;
	}

	public CaseStatus getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(CaseStatus caseStatus) {
		this.caseStatus = caseStatus;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
