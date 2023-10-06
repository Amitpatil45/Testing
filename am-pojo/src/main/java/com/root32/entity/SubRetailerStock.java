package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class SubRetailerStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private ProductMaster product;

	@OneToOne
	private ProductBarcode productBarcode;

	@ManyToOne
	private RetailerCase retailerCase;

	@ManyToOne
	private User scannedBy;

	private Date createdAt;

	private Boolean isSold;

	private Date saleDate;

	@ManyToOne
	private SubRetailer subRetailer;

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

	public RetailerCase getRetailerCase() {
		return retailerCase;
	}

	public void setRetailerCase(RetailerCase retailerCase) {
		this.retailerCase = retailerCase;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsSold() {
		return isSold;
	}

	public void setIsSold(Boolean isSold) {
		this.isSold = isSold;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public SubRetailer getSubRetailer() {
		return subRetailer;
	}

	public void setSubRetailer(SubRetailer subRetailer) {
		this.subRetailer = subRetailer;
	}

	public User getScannedBy() {
		return scannedBy;
	}

	public void setScannedBy(User scannedBy) {
		this.scannedBy = scannedBy;
	}

}
