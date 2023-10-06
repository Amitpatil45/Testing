package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class RetailerStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private ProductMaster product;

	@OneToOne
	private ProductBarcode productBarcode;

	@ManyToOne
	private AdminCase belongsToCase;

	private Date createdAt;

	private Boolean isSold;

	private Date saleDate;

	private Boolean isOutward;
	private Date outwardDate;
	@ManyToOne
	private Org org;

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

	public AdminCase getBelongsToCase() {
		return belongsToCase;
	}

	public void setBelongsToCase(AdminCase belongsToCase) {
		this.belongsToCase = belongsToCase;
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

	public Boolean getIsOutward() {
		return isOutward;
	}

	public void setIsOutward(Boolean isOutward) {
		this.isOutward = isOutward;
	}

	public Date getOutwardDate() {
		return outwardDate;
	}

	public void setOutwardDate(Date outwardDate) {
		this.outwardDate = outwardDate;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

}
