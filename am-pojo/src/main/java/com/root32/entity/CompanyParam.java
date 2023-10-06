package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class CompanyParam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long nextPurchaseInwardNumber;
	private Long nextIssueToProcessNumber;
	private Long nextInwardFromProcessNumber;
	private Long nextFinishClothInwardNumber;
	private Long nextInwardFromRollingNumber;
	private Long nextReturnFromPartyNumber;
	int passwordExpiryDays;
	int maxUnsuccessfulAttempts;
	boolean lockAfterMaxUnsuccessfulAttempts;
	int otpExpiryInMinutes;

	private Long nextSaleNumber;
	@OneToOne
	private Org org;

	@ManyToOne
	@JsonIgnoreProperties(value = { "password", "expiryDate", "createdBy", "createdDate", "lastLoginDate", "isLocked",
			"unsuccessfulAttemps", "roles", "org", "enabled", "accountNonExpired", "accountNonLocked", "username",
			"locked", "authorities", "credentialsNonExpired", "updatedDate", "updatedBy","org" })
	private User createdBy;
	private Date createdDate;
	@ManyToOne
	@JsonIgnoreProperties(value = { "password", "expiryDate", "createdBy", "createdDate", "lastLoginDate", "isLocked",
			"unsuccessfulAttemps", "roles", "org", "enabled", "accountNonExpired", "accountNonLocked", "username",
			"locked", "authorities", "credentialsNonExpired", "updatedDate", "updatedBy","org" })
	private User updatedBy;
	private Date updatedDate;

	public Long getNextPurchaseInwardNumber() {
		return nextPurchaseInwardNumber;
	}

	public void setNextPurchaseInwardNumber(Long nextPurchaseInwardNumber) {
		this.nextPurchaseInwardNumber = nextPurchaseInwardNumber;
	}

	public Long getNextIssueToProcessNumber() {
		return nextIssueToProcessNumber;
	}

	public void setNextIssueToProcessNumber(Long nextIssueToProcessNumber) {
		this.nextIssueToProcessNumber = nextIssueToProcessNumber;
	}

	public Long getNextInwardFromProcessNumber() {
		return nextInwardFromProcessNumber;
	}

	public void setNextInwardFromProcessNumber(Long nextInwardFromProcessNumber) {
		this.nextInwardFromProcessNumber = nextInwardFromProcessNumber;
	}

	public Long getNextFinishClothInwardNumber() {
		return nextFinishClothInwardNumber;
	}

	public void setNextFinishClothInwardNumber(Long nextFinishClothInwardNumber) {
		this.nextFinishClothInwardNumber = nextFinishClothInwardNumber;
	}

	public Long getNextInwardFromRollingNumber() {
		return nextInwardFromRollingNumber;
	}

	public void setNextInwardFromRollingNumber(Long nextInwardFromRollingNumber) {
		this.nextInwardFromRollingNumber = nextInwardFromRollingNumber;
	}

	public Long getNextReturnFromPartyNumber() {
		return nextReturnFromPartyNumber;
	}

	public void setNextReturnFromPartyNumber(Long nextReturnFromPartyNumber) {
		this.nextReturnFromPartyNumber = nextReturnFromPartyNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPasswordExpiryDays() {
		return passwordExpiryDays;
	}

	public void setPasswordExpiryDays(int passwordExpiryDays) {
		this.passwordExpiryDays = passwordExpiryDays;
	}

	public int getMaxUnsuccessfulAttempts() {
		return maxUnsuccessfulAttempts;
	}

	public void setMaxUnsuccessfulAttempts(int maxUnsuccessfulAttempts) {
		this.maxUnsuccessfulAttempts = maxUnsuccessfulAttempts;
	}

	public boolean isLockAfterMaxUnsuccessfulAttempts() {
		return lockAfterMaxUnsuccessfulAttempts;
	}

	public void setLockAfterMaxUnsuccessfulAttempts(boolean lockAfterMaxUnsuccessfulAttempts) {
		this.lockAfterMaxUnsuccessfulAttempts = lockAfterMaxUnsuccessfulAttempts;
	}

	public int getOtpExpiryInMinutes() {
		return otpExpiryInMinutes;
	}

	public void setOtpExpiryInMinutes(int otpExpiryInMinutes) {
		this.otpExpiryInMinutes = otpExpiryInMinutes;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Long getNextSaleNumber() {
		
		return nextSaleNumber;
	}

	public void setNextSaleNumber(Long nextSaleNumber) {
		this.nextSaleNumber = nextSaleNumber;
	}

}