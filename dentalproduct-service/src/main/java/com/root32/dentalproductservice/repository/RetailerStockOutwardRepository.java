package com.root32.dentalproductservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.RetailerCase;
import com.root32.entity.RetailerStockOutward;

@Repository
public interface RetailerStockOutwardRepository extends JpaRepository<RetailerStockOutward,Long>{

	Optional<RetailerStockOutward> findByRetailerCase(RetailerCase retailerCase);
	

}
