package com.root32.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties()
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;
	public static final String LOGIN_USER = "LOGIN_USER";
	public static final String USER_ORG = "USER_ORG";
	
	@Transient
	private Set<GrantedAuthority> authorities = new HashSet<>();


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Size(min = 8, message = "EmailId should have at least 8 characters")
	@Email
	private String emailId;

	private String password;

	private String firstName;
	private String lastName;
	private String contactNumber;
	private Date createdDate;
	@JsonIgnore
	private Date expiryDate;
	@JsonIgnore
	private Date lastLoginDate;
	@JsonIgnore
	private Boolean isLocked;
	@JsonIgnore
	private int unsuccessfulAttemps;

	private Boolean isPasswordUpdate;

	private Boolean isOrgAdminUser;

	private Boolean isActive;

	@JsonIgnore
	private Boolean isHavingAnyRecords;

	@ManyToOne
	private Org org;

	@ManyToMany(fetch = FetchType.EAGER)
	Set<Role> roles = new HashSet<>();

	private Date updatedDate;

	@ManyToOne
	@JsonIgnore
	private User updatedBy;

	@ManyToOne
	@JsonIgnore
	private User createdBy;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getUsername() {
		return emailId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return expiryDate.after(new Date());
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return expiryDate.after(new Date());
	}

	@Override
	public boolean isEnabled() {
		return !isLocked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public int getUnsuccessfulAttemps() {
		return unsuccessfulAttemps;
	}

	public void setUnsuccessfulAttemps(int unsuccessfulAttemps) {
		this.unsuccessfulAttemps = unsuccessfulAttemps;
	}

	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRoles(Role roles) {
		this.roles.add(roles);
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getLoginUser() {
		return LOGIN_USER;
	}

	public static String getUserOrg() {
		return USER_ORG;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsPasswordUpdate() {
		return isPasswordUpdate;
	}

	public void setIsPasswordUpdate(Boolean isPasswordUpdate) {
		this.isPasswordUpdate = isPasswordUpdate;
	}

	public Boolean getIsOrgAdminUser() {
		return isOrgAdminUser;
	}

	public void setIsOrgAdminUser(Boolean isOrgAdminUser) {
		this.isOrgAdminUser = isOrgAdminUser;
	}

	public Boolean getIsHavingAnyRecords() {
		return isHavingAnyRecords;
	}

	public void setIsHavingAnyRecords(Boolean isHavingAnyRecords) {
		this.isHavingAnyRecords = isHavingAnyRecords;
	}

	public void addAuthority(GrantedAuthority ga) {
		authorities.add(ga);
	}
}
