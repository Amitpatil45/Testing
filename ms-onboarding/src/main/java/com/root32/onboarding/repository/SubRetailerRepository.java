package com.root32.onboarding.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.Org;
import com.root32.entity.SubRetailer;

public interface SubRetailerRepository extends JpaRepository<SubRetailer, Long> {

	SubRetailer findFirstByMobileNumberOrEmailId(String mobileNumber, String emailId);

	Page<SubRetailer> findAllByParent(Org userOrg, Pageable pageable);

	List<SubRetailer> findAllByIsActiveAndParent(boolean b, Org org);

	Long countByBusinessNameAndParent(String businessName, Org parent);

//	Page<Vendor> findAllByOnboardingStatus(Pageable pageable, OnboardingStatus onboardingStatus);
//
//	Page<Vendor> findAllByParentAndOnboardingStatus(Pageable pageable, Org userOrg, OnboardingStatus onboardingStatus);
//
//	Page<Vendor> findAllByParent(Pageable pageable, Org userOrg);
//
//	Page<Vendor> findAllByBusinessTypeIdAndOnboardingStatus(Pageable pageable, Short businessType,
//			OnboardingStatus onboardingStatus);
//
//	Page<Vendor> findAllByParentAndBusinessTypeIdAndOnboardingStatus(Pageable pageable, Org userOrg, Short businessType,
//			OnboardingStatus onboardingStatus);
//
//	Page<Vendor> findAllByBusinessTypeId(Pageable pageable, Short businessType);
//
//	Page<Vendor> findAllByParentAndBusinessTypeId(Pageable pageable, Org userOrg, Short businessType);
//
//	Vendor findFirstByPocEmailId(String pocEmailId);
//
//	List<Vendor> findAllByParentAndOnboardingStatus(Org userOrg, OnboardingStatus onboardingStatus);

}
