package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SubRetailerToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private SubRetailer subRetailer;

	private String token;
	private Date createDate;
	private Date expiryPeriod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SubRetailer getSubRetailer() {
		return subRetailer;
	}

	public void setSubRetailer(SubRetailer subRetailer) {
		this.subRetailer = subRetailer;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpiryPeriod() {
		return expiryPeriod;
	}

	public void setExpiryPeriod(Date expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}

}
