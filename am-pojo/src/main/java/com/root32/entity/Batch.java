package com.root32.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties({ "highlightText", "description", "category", "uom", "salePrice", "regularPrice", "discount",
			"weight", "isActive", "thumbnailImage", "images", "videos", "createdBy", "updatedBy", "createdAt",
			"updatedAt" })
	private ProductMaster product;
	private String batchCode;
	private Long quantity;
	private Long recievedQantity;
	private Long remainingQuantity;

	@Column(columnDefinition = "DATE")
	private Date manufacturedDate;
	@Column(columnDefinition = "DATE")
	private Date expiryDate;

	private Date recievedDate;
	private Boolean isRecieved;

	@ManyToOne
	@JsonIgnore
	private PurchaseOrder belongsToPo;

	private Date updatedDate;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
			"isOrgAdminUser" })
	private User updatedBy;

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getRecievedQantity() {
		return recievedQantity;
	}

	public void setRecievedQantity(Long recievedQantity) {
		this.recievedQantity = recievedQantity;
	}

	public Date getManufacturedDate() {
		return manufacturedDate;
	}

	public void setManufacturedDate(Date manufacturedDate) {
		this.manufacturedDate = manufacturedDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}

	public Boolean getIsRecieved() {
		return isRecieved;
	}

	public void setIsRecieved(Boolean isRecieved) {
		this.isRecieved = isRecieved;
	}

	public PurchaseOrder getBelongsToPo() {
		return belongsToPo;
	}

	public void setBelongsToPo(PurchaseOrder belongsToPo) {
		this.belongsToPo = belongsToPo;
	}

	public Long getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(Long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

}
