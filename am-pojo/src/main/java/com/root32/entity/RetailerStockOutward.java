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
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class RetailerStockOutward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "pocFirstName", "pocLastName" })
	private Org fromRetailer;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "pocFirstName", "pocLastName" })
	private Org toSubRetailer;

	@OneToOne
	@JsonIgnoreProperties({ "isSealed", "createdAt", "createdBy", "recievedBy" })
	private RetailerCase retailerCase;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RetailerStockOutwardData> datas = new ArrayList<>();

	private Long totalUnits;

	private CaseStatus caseStatus;

	private Date createdAt;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "firstName", "lastName" })
	private User createdBy;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "firstName", "lastName" })
	private User updatedBy;
	private Date updatedAt;
	private Date recievedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Org getFromRetailer() {
		return fromRetailer;
	}

	public void setFromRetailer(Org fromRetailer) {
		this.fromRetailer = fromRetailer;
	}

	public Org getToSubRetailer() {
		return toSubRetailer;
	}

	public void setToSubRetailer(Org toSubRetailer) {
		this.toSubRetailer = toSubRetailer;
	}

	public RetailerCase getRetailerCase() {
		return retailerCase;
	}

	public void setRetailerCase(RetailerCase retailerCase) {
		this.retailerCase = retailerCase;
	}

	public List<RetailerStockOutwardData> getDatas() {
		return datas;
	}

	public void setDatas(List<RetailerStockOutwardData> datas) {
		this.datas = datas;
	}

	public Long getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(Long totalUnits) {
		this.totalUnits = totalUnits;
	}

	public CaseStatus getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(CaseStatus caseStatus) {
		this.caseStatus = caseStatus;
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

	public Date getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}

}
