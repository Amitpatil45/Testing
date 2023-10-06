package com.root32.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Permission implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	public Permission(String code, String label, PermissionCategory permissionCategory) {
		super();
		this.code = code;
		this.label = label;
		this.permissionCategory = permissionCategory;

	}

	public Permission() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "char(8)", unique = true, nullable = false)
	private String code;

	private String label;

	@JsonIgnoreProperties(value = { "createdBy", "creationDate", "lastModifiedBy", "lastModifiedDate" })
	@ManyToOne
	private PermissionCategory permissionCategory;

	private Date createdDate;

	private Date updatedDate;

	@Override
	public String getAuthority() {
		return code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Permission privilege = (Permission) obj;
		if (!privilege.equals(privilege.code)) {
			return false;
		}
		return true;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PermissionCategory getPermissionCategory() {
		return permissionCategory;
	}

	public void setPermissionCategory(PermissionCategory permissionCategory) {
		this.permissionCategory = permissionCategory;
	}

}
