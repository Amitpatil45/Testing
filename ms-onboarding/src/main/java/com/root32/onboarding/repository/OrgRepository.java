package com.root32.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.Org;

public interface OrgRepository extends JpaRepository<Org, Long> {

	Org findByMobileNumberOrEmailId(String mobileNumber, String emailId);

}
