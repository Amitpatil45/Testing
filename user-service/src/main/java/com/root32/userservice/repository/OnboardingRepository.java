package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.Retailer;

public interface OnboardingRepository extends JpaRepository<Retailer, Long> {

	Long countByBusinessNameContainingIgnoreCase(String string);

}
