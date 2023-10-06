package com.root32.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;

public class InitiateSaleResponseDto {

	@JsonIgnoreProperties({ "description", "category", "isActive", "images", "videos", "createdBy", "updatedBy",
			"createdAt", "updatedAt", "", })
	private ProductMaster product;

	@JsonIgnoreProperties({ "isRecieved" })
	private ProductBarcode productBarcode;

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

	public InitiateSaleResponseDto(ProductMaster product, ProductBarcode productBarcode) {
		super();
		this.product = product;
		this.productBarcode = productBarcode;
	}

	public InitiateSaleResponseDto() {
	}

}
