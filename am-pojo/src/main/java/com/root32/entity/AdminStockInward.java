package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class AdminStockInward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIncludeProperties({ "id", "productCode", "name" })
	private ProductMaster product;

	@ManyToOne
	@JsonIncludeProperties({ "batchCode" })
	private Batch batch;

	private Long quantity;

	@ManyToOne
	@JsonIncludeProperties({ "id", "businessName", "email" })
	private ManufacturerMaster manufacturer;

	private Date createdDate;

	@ManyToOne
	@JsonIncludeProperties({ "id", "emailId", "firstName", "lastName" })
	private User createdBy;

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

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public ManufacturerMaster getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerMaster manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

}
