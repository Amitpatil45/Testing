package com.root32.entity;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("2")
public class Retailer extends Org {
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
		if (obj == null || !(obj instanceof Retailer)) {
			return false;
		}
		if (this.getId() == ((Retailer) obj).getId()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
}
