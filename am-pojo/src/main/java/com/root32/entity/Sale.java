package com.root32.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String consumerName;

	private String consumerMobile;

	private String saleReferenceNumber;

	@OneToMany(cascade = CascadeType.ALL)
	private List<SaleData> saleDatas = new ArrayList<>();

	@Column(precision = 10, scale = 2)
	private BigDecimal total;

	@Column(precision = 10, scale = 2)
	private BigDecimal adminRevenue;

	@Column(precision = 10, scale = 2)
	private BigDecimal retailerRevenue;

	@Column(precision = 10, scale = 2)
	private BigDecimal subRetailerRevenue;
	private Boolean isCash;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "contactNumber", "createdDate", "expiryDate", "isLocked", "unsuccessfulAttemps",
			"isActive", "org", "roles", "username", "authorities", "accountNonExpired", "accountNonLocked",
			"credentialsNonExpired", "enabled" })
	private User createdBy;

	private Date createdAt;

	@ManyToOne
	@JsonIgnoreProperties({ "pocFirstName", "pocLastName", "emailId", "mobileNumber", "orgTypeEnum", "isActive",
			"orgCode", "isHavingRecord", "address", "createdBy", "updatedBy", "parent", "createdDate", "updateDate",
			"commissionInPercent" })
	private Org org;

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

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
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

	public String getSaleReferenceNumber() {
		return saleReferenceNumber;
	}

	public void setSaleReferenceNumber(String saleReferenceNumber) {
		this.saleReferenceNumber = saleReferenceNumber;
	}

}
