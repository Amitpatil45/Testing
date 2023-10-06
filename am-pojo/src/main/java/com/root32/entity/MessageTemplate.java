package com.root32.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
@Entity
public class MessageTemplate {
	public MessageTemplate() {
		super();
	}

	public MessageTemplate(String templateKey, String templateValue, String templateCode, Date createdDate) {
		super();
		this.templateKey = templateKey;
		this.templateValue = templateValue;
		this.templateCode = templateCode;
		this.createdDate = createdDate;

	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String templateKey;

	@Column(columnDefinition = "MEDIUMTEXT")
	private String templateValue;

	private String templateCode;
	@OneToOne
	@JsonIncludeProperties({"id","emailId"})
	private User createdBy;
	private Date createdDate;
	@OneToOne
	@JsonIncludeProperties({"id","emailId"})
	private User updatedBy;
	private Date updatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public String getTemplateValue() {
		return templateValue;
	}

	public void setTemplateValue(String templateValue) {
		this.templateValue = templateValue;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	} 
}