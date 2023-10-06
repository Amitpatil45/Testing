package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class RetailerInward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JsonIgnoreProperties({ "isSealed", "createdBy","createdAt","recievedBy" })
	private AdminCase adminCase;

	@OneToOne
	@JsonIgnoreProperties({ "retailer", "createdBy","adminCase" })
	private AdminStockOutward adminStockOutward;

	@ManyToOne
	@JsonIgnoreProperties({ "password", "firstName", "lastName", "contactNumber", "createdDate", "expiryDate",
			"isLocked", "unsuccessfulAttemps", "isActive", "org", "roles", "username", "authorities",
			"accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "isPasswordUpdate",
			"isOrgAdminUser" })
	private User createdBy;

	@ManyToOne
	@JsonIgnore
	private Org org;

	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AdminCase getAdminCase() {
		return adminCase;
	}

	public void setAdminCase(AdminCase adminCase) {
		this.adminCase = adminCase;
	}

	public AdminStockOutward getAdminStockOutward() {
		return adminStockOutward;
	}

	public void setAdminStockOutward(AdminStockOutward adminStockOutward) {
		this.adminStockOutward = adminStockOutward;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	// ----------------------------------

}
