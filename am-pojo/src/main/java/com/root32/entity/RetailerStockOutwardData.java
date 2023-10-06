package com.root32.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class RetailerStockOutwardData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIncludeProperties({ "id", "productCode", "name", "highlightText" })
	private ProductMaster product;

	private Long quantity;

	@OneToMany
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public List<ProductBarcode> getProductBarcodes() {
		return productBarcodes;
	}

	public void setProductBarcodes(List<ProductBarcode> productBarcodes) {
		this.productBarcodes = productBarcodes;
	}
	
	
	
}
