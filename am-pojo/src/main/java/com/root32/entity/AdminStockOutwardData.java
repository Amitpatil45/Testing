package com.root32.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class AdminStockOutwardData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties({ "highlightText", "description", "category", "uom", "salePrice", "regularPrice", "discount",
			"weight", "isActive", "thumbnailImage", "images", "videos", "createdBy", "updatedBy", "createdAt",
			"updatedAt" })
	private ProductMaster product;
	
	private Long quantity;

	@OneToMany // (cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "isRecieved" })
	private List<ProductBarcode> productBarcodes = new ArrayList<>();

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

	public List<ProductBarcode> getProductBarcodes() {
		return productBarcodes;
	}

	public void setProductBarcodes(List<ProductBarcode> productBarcodes) {
		this.productBarcodes = productBarcodes;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	
	
}
