package com.root32.dentalproductservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.root32.entity.ManufacturerMaster;

public interface ManufacturerMasterRepository extends JpaRepository<ManufacturerMaster, Long> {

	int countByEmail(String email);

	int countByMobile(String mobileNumber);

	int countByBusinessName(String businessName);

	List<ManufacturerMaster> findAllByIsActive(boolean b);


	Page<ManufacturerMaster> findAllByFullNameContainingIgnoreCase(String searchItem, Pageable pageable);
	Page<ManufacturerMaster> findAllByBusinessNameContainingIgnoreCase(String searchItem, Pageable pageable);
Page<ManufacturerMaster> findAllByAddressCityContainingIgnoreCase( String searchItem, Pageable pageable);

}
