package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class SubRetailerInward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JsonIgnoreProperties({ "isSealed", "createdBy", "createdAt", })
	private RetailerCase retailerCase;

	@OneToOne
	@JsonIgnoreProperties({ "createdBy", "retailerCase" })
	private RetailerStockOutward retailerStockOutward;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "firstName", "lastName" })
	private User createdBy;

	@ManyToOne
	@JsonIgnore
	private SubRetailer subRetailer;

	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RetailerCase getRetailerCase() {
		return retailerCase;
	}

	public void setRetailerCase(RetailerCase retailerCase) {
		this.retailerCase = retailerCase;
	}

	public RetailerStockOutward getRetailerStockOutward() {
		return retailerStockOutward;
	}

	public void setRetailerStockOutward(RetailerStockOutward retailerStockOutward) {
		this.retailerStockOutward = retailerStockOutward;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public SubRetailer getSubRetailer() {
		return subRetailer;
	}

	public void setSubRetailer(SubRetailer subRetailer) {
		this.subRetailer = subRetailer;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
