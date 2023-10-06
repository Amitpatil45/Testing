package com.root32.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class ProductMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String productCode;
	@Column(unique = true)
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

	private Boolean isActive;

	private String thumbnailImage;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ProductImage> images;

	// @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// private List<ProductVideo> videos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	// public List<ProductVideo> getVideos() {
	// 	return videos;
	// }

	// public void setVideos(List<ProductVideo> videos) {
	// 	this.videos = videos;
	// }

	// -----------------------------------------------------------------------------
	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled" })
	private User createdBy;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled" })
	private User updatedBy;

	private Date createdAt;

	private Date updatedAt;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
}