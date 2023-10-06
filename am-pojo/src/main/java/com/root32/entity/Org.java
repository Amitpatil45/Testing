package com.root32.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties()
//@JsonNaming(value = PropertyNamingStrategy.class)
public class Org {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name cannot be blank")
	@Size(max = 100)
	private String businessName;

	private String pocFirstName;
	private String pocLastName;

	@NotEmpty
	@Size(min = 8, message = "EmailId should have at least 8 characters")
	@Email
	@Column(unique = true, nullable = false)
	private String emailId;

	@Column(unique = true, nullable = false)
	private String mobileNumber;

	@JsonIgnore
	@Column(unique = true)
	private String phoneNumber;

	private OrgTypeEnum orgTypeEnum;
	private Boolean isActive;
	private String orgCode;

	private Boolean isHavingRecord;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Address address;

	private Float commissionInPercent;

	public Float getCommissionInPercent() {
		return commissionInPercent;
	}

	public void setCommissionInPercent(Float commissionInPercent) {
		this.commissionInPercent = commissionInPercent;
	}

	@ManyToOne
	@JsonIgnoreProperties({ "password", "isPasswordUpdate", "isOrgAdminUser", "contactNumber", "createdDate",
			"expiryDate", "isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled" })
	private User createdBy;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "isPasswordUpdate", "isOrgAdminUser", "contactNumber", "createdDate",
			"expiryDate", "isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled" })
	private User updatedBy;

	private Date createdDate;

	private Date updateDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getPocFirstName() {
		return pocFirstName;
	}

	public void setPocFirstName(String pocFirstName) {
		this.pocFirstName = pocFirstName;
	}

	public String getPocLastName() {
		return pocLastName;
	}

	public void setPocLastName(String pocLastName) {
		this.pocLastName = pocLastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public OrgTypeEnum getOrgTypeEnum() {
		return orgTypeEnum;
	}

	public void setOrgTypeEnum(OrgTypeEnum orgTypeEnum) {
		this.orgTypeEnum = orgTypeEnum;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getIsHavingRecord() {
		return isHavingRecord;
	}

	public void setIsHavingRecord(Boolean isHavingRecord) {
		this.isHavingRecord = isHavingRecord;
	}

}
