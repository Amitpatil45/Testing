package com.root32.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.root32.entity.Org;

public class RetailerWiseStockClassDto {

	@JsonIncludeProperties({ "id", "businessName" })
	private Org retailer;

	private List<ProductAndQuantityClassDto> products = new ArrayList<>();

//	private ProductMaster product;
//
//	private Long quantity;

	private Date lastRefillDate;

	private Date lastSaleDate;

//	public ProductMaster getProduct() {
//		return product;
//	}
//
//	public void setProduct(ProductMaster product) {
//		this.product = product;
//	}
//
//	public Long getQuantity() {
//		return quantity;
//	}
//
//	public void setQuantity(Long quantity) {
//		this.quantity = quantity;
//	}

	public Org getRetailer() {
		return retailer;
	}

	public void setRetailer(Org retailer) {
		this.retailer = retailer;
	}

//	public List<ProductAndQuantityClassDto> getProducts() {
//		return products;
//	}
//
//	public void setProducts(List<ProductAndQuantityClassDto> products) {
//		this.products = products;
//	}

	public Date getLastRefillDate() {
		return lastRefillDate;
	}

	public void setLastRefillDate(Date lastRefillDate) {
		this.lastRefillDate = lastRefillDate;
	}

	public Date getLastSaleDate() {
		return lastSaleDate;
	}

	public void setLastSaleDate(Date lastSaleDate) {
		this.lastSaleDate = lastSaleDate;
	}

	public List<ProductAndQuantityClassDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductAndQuantityClassDto> products) {
		this.products = products;
	}

	

}
