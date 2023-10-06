package com.root32.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.Pincode;

public interface PincodeRepository extends JpaRepository<Pincode, Long> {

	Pincode findByPin(String pin);
}
