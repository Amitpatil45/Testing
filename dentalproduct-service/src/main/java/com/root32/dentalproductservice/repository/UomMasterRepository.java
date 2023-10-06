package com.root32.dentalproductservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.UomMaster;

public interface UomMasterRepository extends JpaRepository<UomMaster, Long> {

	boolean existsByName(String name);

	int countByNameIgnoreCase(String name);

	int countByUomCode(String uomCode);

	int countByName(String name);

	Page<UomMaster> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<UomMaster> findByUomCodeContainingIgnoreCase(String searchItem, Pageable pageable);

}
