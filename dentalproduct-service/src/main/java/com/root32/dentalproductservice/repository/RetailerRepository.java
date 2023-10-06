package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Retailer;

@Repository
public interface RetailerRepository extends JpaRepository<Retailer, Long> {

//	Retailer findByParentId(Long id);

}
