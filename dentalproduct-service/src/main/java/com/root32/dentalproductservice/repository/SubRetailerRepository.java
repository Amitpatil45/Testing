package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.SubRetailer;

@Repository
public interface SubRetailerRepository extends JpaRepository<SubRetailer,Long> {

//	SubRetailer findByOrgId(Long id);

	
}
