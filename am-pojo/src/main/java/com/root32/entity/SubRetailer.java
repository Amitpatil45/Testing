package com.root32.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
@DiscriminatorValue("3")
public class SubRetailer extends Org {
	@ManyToOne
	@JsonIncludeProperties({ "id", "businessName", "orgTypeEnum", "emailId", "mobileNumber", "orgCode" })
	private Org parent;

	
	

	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SubRetailer)) {
			return false;
		}
		if (this.getId() == ((SubRetailer) obj).getId()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

}
