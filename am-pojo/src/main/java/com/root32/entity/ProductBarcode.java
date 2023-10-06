package com.root32.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ProductBarcode {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productBarcode;
	private Boolean isRecieved;

	@ManyToOne
	@JsonIgnore
	private Batch belongsToBatch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	public Boolean getIsRecieved() {
		return isRecieved;
	}

	public void setIsRecieved(Boolean isRecieved) {
		this.isRecieved = isRecieved;
	}

	public Batch getBelongsToBatch() {
		return belongsToBatch;
	}

	public void setBelongsToBatch(Batch belongsToBatch) {
		this.belongsToBatch = belongsToBatch;
	}

}
