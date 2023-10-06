package com.root32.onboarding.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.root32.entity.Org;
import com.root32.entity.Retailer;

public interface OnboardingRepository extends JpaRepository<Retailer, Long> {

	Page<Retailer> findAllByParent(Pageable pageable, Org userOrg);

	Retailer findFirstByMobileNumberOrEmailId(String mobileNumber, String emailId);

	@Query(value="SELECT * FROM org where org_type_enum = 1",nativeQuery=true)
	Page<Retailer> getretailers(Pageable pageable);

	List<Retailer> findAllByIsActive(boolean b);


}
