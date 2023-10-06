package com.root32.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.root32.entity.Org;
import com.root32.entity.SaleData;
import com.root32.entity.User;

public class SaleDto {

	private Long id;

	private String consumerName;

	private String consumerMobile;

	private List<SaleData> saleDatas;

	private BigDecimal total;

	private BigDecimal adminRevenue;

	private BigDecimal retailerRevenue;

	private BigDecimal subRetailerRevenue;
	private Boolean isCash;

	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled" })
	private User createdBy;

	private Date createdAt;

	@JsonIgnoreProperties({ "pocFirstName", "pocLastName", "emailId", "mobileNumber", "orgTypeEnum", "isActive",
			"orgCode", "isHavingRecord", "address", "createdBy", "updatedBy", "parent", "createdDate", "updateDate",
			"commissionInPercent" })
	private Org org;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getConsumerMobile() {
		return consumerMobile;
	}

	public void setConsumerMobile(String consumerMobile) {
		this.consumerMobile = consumerMobile;
	}

	public List<SaleData> getSaleDatas() {
		return saleDatas;
	}

	public void setSaleDatas(List<SaleData> saleDatas) {
		this.saleDatas = saleDatas;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getAdminRevenue() {
		return adminRevenue;
	}

	public void setAdminRevenue(BigDecimal adminRevenue) {
		this.adminRevenue = adminRevenue;
	}

	public BigDecimal getRetailerRevenue() {
		return retailerRevenue;
	}

	public void setRetailerRevenue(BigDecimal retailerRevenue) {
		this.retailerRevenue = retailerRevenue;
	}

	public BigDecimal getSubRetailerRevenue() {
		return subRetailerRevenue;
	}

	public void setSubRetailerRevenue(BigDecimal subRetailerRevenue) {
		this.subRetailerRevenue = subRetailerRevenue;
	}

	public Boolean getIsCash() {
		return isCash;
	}

	public void setIsCash(Boolean isCash) {
		this.isCash = isCash;
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

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

}
