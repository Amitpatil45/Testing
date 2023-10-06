package com.root32.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
public class SaleData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIncludeProperties({"id","name","productCode"})
	private ProductMaster product;

	@OneToMany   
	@JsonIncludeProperties({"id","productBarcode"})
	private List<ProductBarcode> productBarcodes = new ArrayList<>();

	private BigDecimal salePrice;

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
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

	public List<ProductBarcode> getProductBarcodes() {
		return productBarcodes;
	}

	public void setProductBarcodes(List<ProductBarcode> productBarcodes) {
		this.productBarcodes = productBarcodes;
	}

}
