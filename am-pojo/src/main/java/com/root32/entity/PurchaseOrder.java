package com.root32.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PurchaseOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String poCode;

	@JsonIgnoreProperties({ "email", "mobile", "address", "createdBy", "updatedBy", "createdAt", "updatedAt" })
	@OneToOne
	private ManufacturerMaster manufacturer;

	@OneToMany(cascade = CascadeType.ALL)
	private List<PoData> poData = new ArrayList<>();

	private String instructions;
	private Date expectedDelivery;
	private Date completedDate;

	private Boolean isRecieved;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPoCode() {
		return poCode;
	}

	public void setPoCode(String poCode) {
		this.poCode = poCode;
	}

	public ManufacturerMaster getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerMaster manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<PoData> getPoData() {
		return poData;
	}

	public void setPoData(List<PoData> poData) {
		this.poData = poData;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public Date getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public Boolean getIsRecieved() {
		return isRecieved;
	}

	public void setIsRecieved(Boolean isRecieved) {
		this.isRecieved = isRecieved;
	}

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
			"isOrgAdminUser" })
	private User createdBy;

	private Date createdAt;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
