package com.root32.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.root32.entity.Org;
import com.root32.entity.Role;

public interface UserDto {

	public Long getId();

	public void setId(Long id);

	public String getEmailId();

	public void setEmailId(String emailId);

	@JsonIgnore
	public Date getExpiryDate();

	public void setExpiryDate(Date expiryDate);

	@JsonIncludeProperties(value = { "id", "emailId" })
	public UserDto getCreatedBy();

	public void setCreatedBy(UserDto createdBy);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	@JsonIgnore
	public Date getLastLoginDate();

	public void setLastLoginDate(Date lastLoginDate);

	@JsonIgnore
	public Boolean getIsLocked();

	public void setIsLocked(Boolean isLocked);

	@JsonIgnore
	public int getUnsuccessfulAttemps();

	public void setUnsuccessfulAttemps(int unsuccessfulAttemps);

	@JsonIncludeProperties(value = { "id", "name" })
	public Set<Role> getRoles();

	public void addRole(Role role);

	@JsonIncludeProperties(value = { "id", "businessName" })
	public Org getOrg();

	public void setOrg(Org org);

	public String getFirstName();

	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);

	public String getContactNumber();

	public void setContactNumber(String contactNumber);

	public Boolean getIsActive();

	public void setIsActive(Boolean isActive);
}
