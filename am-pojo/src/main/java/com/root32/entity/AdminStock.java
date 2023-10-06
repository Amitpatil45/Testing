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
public class AdminStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties({ "email", "mobile", "address", "createdBy", "updatedBy", "createdAt", "updatedAt" })
	private ManufacturerMaster manufacturer;

	@ManyToOne
	@JsonIgnoreProperties({ "highlightText", "description", "category", "uom", "salePrice", "regularPrice", "discount",
			"weight", "isActive", "thumbnailImage", "images", "videos", "createdBy", "updatedBy", "createdAt",
			"updatedAt" })
	private ProductMaster product;

	@OneToOne
	private ProductBarcode productBarcode;

	@ManyToOne
	@JsonIgnore
	private Org org;

	@ManyToOne
	@JsonIncludeProperties({ "batchCode" })
	private Batch batch;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
			"isOrgAdminUser" })
	private User scannedBy;

	private Date createdAt;
//-------------------------------- new fields
	@JsonIgnore
	private Boolean isSold;

	private Date saleDate;

//-------------------------------------------

	@JsonIgnore
	private Boolean isOutward;
	private Date outwardDate;

	// --------------------------------------

	public Date getSaleDate() {
		return saleDate;
	}

	public Date getOutwardDate() {
		return outwardDate;
	}

	public void setOutwardDate(Date outwardDate) {
		this.outwardDate = outwardDate;
	}

	public Boolean getIsOutward() {
		return isOutward;
	}

	public void setIsOutward(Boolean isOutward) {
		this.isOutward = isOutward;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public Boolean getIsSold() {
		return isSold;
	}

	public void setIsSold(Boolean isSold) {
		this.isSold = isSold;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductMaster getProduct() {
		return product;
	}

	public void setProduct(ProductMaster product) {
		this.product = product;
	}

	public ProductBarcode getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(ProductBarcode productBarcode) {
		this.productBarcode = productBarcode;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public User getScannedBy() {
		return scannedBy;
	}

	public void setScannedBy(User scannedBy) {
		this.scannedBy = scannedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public AdminStock() {
		super();
	}

	public AdminStock(ManufacturerMaster manufacturer, ProductMaster product, ProductBarcode productBarcode, Org org,
			Batch batch, User scannedBy, Date createdAt, Boolean isSold, Date saleDate, Boolean isOutward,
			Date outwardDate) {
		super();
		this.manufacturer = manufacturer;
		this.product = product;
		this.productBarcode = productBarcode;
		this.org = org;
		this.batch = batch;
		this.scannedBy = scannedBy;
		this.createdAt = createdAt;
		this.isSold = isSold;
		this.saleDate = saleDate;
		this.isOutward = isOutward;
		this.outwardDate = outwardDate;
	}

	public ManufacturerMaster getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerMaster manufacturer) {
		this.manufacturer = manufacturer;
	}

}
