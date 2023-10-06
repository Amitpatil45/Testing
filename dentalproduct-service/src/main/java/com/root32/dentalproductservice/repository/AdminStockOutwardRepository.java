package com.root32.dentalproductservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.entity.AdminStockOutward;

@Repository
public interface AdminStockOutwardRepository extends JpaRepository<AdminStockOutward, Long> {

	@Query(value = "Select * from admin_stock_outward", nativeQuery = true)
	Page<AdminStockOutward> getAllAdminStock(Pageable pageable);

	AdminStockOutward findByAdminCaseId(Long id);

	Page<AdminStockOutward> findAllByRetailerBusinessNameContaining(Pageable pageable, String search);

	Page<AdminStockOutward> findAllByAdminCaseCaseCodeContaining(Pageable pageable, String search);


	Page<AdminStockOutward> findAllByDatasProductNameContaining(Pageable pageable, String search);

	Optional<AdminStockOutward> findByAdminCaseCaseCode(String caseCode);

	Page<AdminStockOutward> findAllDistinctByDatasProductNameContaining(Pageable pageable, String search);

}
