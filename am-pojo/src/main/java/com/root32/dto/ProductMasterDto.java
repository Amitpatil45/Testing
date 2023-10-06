package com.root32.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.root32.entity.CategoryMaster;
import com.root32.entity.UomMaster;

public class ProductMasterDto {

	private String name;
	private String highlightText;

	private String description;

	@ManyToOne
	@JsonIgnoreProperties({ "description", "imageUrl", "isActive", "status", "createdBy", "updatedBy", "createdAt",
			"updatdAt" })
	private CategoryMaster category;

	@ManyToOne
	@JsonIgnoreProperties({ "createdBy", "updatedBy", "createdAt", "updatdAt" })
	private UomMaster uom;

	@Column(precision = 10, scale = 2)
	private BigDecimal salePrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal regularPrice;

	@Column(precision = 10, scale = 2)
	private Float discount;

	@Column(precision = 10, scale = 2)
	private Double weight;

	private String thumbnailImage;

	private List<Base64Image> images;

	// private List<ProductVideo> videos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHighlightText() {
		return highlightText;
	}

	public void setHighlightText(String highlightText) {
		this.highlightText = highlightText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryMaster getCategory() {
		return category;
	}

	public void setCategory(CategoryMaster category) {
		this.category = category;
	}

	public UomMaster getUom() {
		return uom;
	}

	public void setUom(UomMaster uom) {
		this.uom = uom;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(BigDecimal regularPrice) {
		this.regularPrice = regularPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public List<Base64Image> getImages() {
		return images;
	}

	public void setImages(List<Base64Image> images) {
		this.images = images;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

}
