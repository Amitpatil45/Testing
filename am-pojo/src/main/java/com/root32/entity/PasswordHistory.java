package com.root32.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PasswordHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	// Size(min = 8, message = "Password should have at least 8 characters")
	private String password;
	private Date changedDate;

	@OneToOne
	@JsonIgnoreProperties(value = { "password", "expiryDate", "createdBy", "createdDate", "lastLoginDate", "isLocked",
			"unsuccessfulAttemps", "roles", "org", "enabled", "accountNonExpired", "accountNonLocked", "username",
			"locked", "authorities", "credentialsNonExpired", "updatedDate", "updatedBy", "org" })
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(Date changedDate) {
		this.changedDate = changedDate;
	}

}