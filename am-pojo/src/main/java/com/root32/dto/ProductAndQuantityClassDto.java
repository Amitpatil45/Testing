package com.root32.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.root32.entity.ProductMaster;

public class ProductAndQuantityClassDto {
	@JsonIncludeProperties({ "id", "productCode", "name" })
	private ProductMaster product;

	private Long quantity;

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

}
