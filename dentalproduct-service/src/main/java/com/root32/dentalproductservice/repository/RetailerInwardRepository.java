package com.root32.dentalproductservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.RetailerInward;

@Repository
public interface RetailerInwardRepository extends JpaRepository<RetailerInward, Long> {

	Page<RetailerInward> findAllByOrg(Pageable pageable, Org org);

	List<RetailerInward> findAllByOrg(Org org);

	Page<RetailerInward> findAllByOrgAndAdminCaseCaseCodeContaining(Pageable pageable, Org org, String search);

	// works but unused
//	@Query("SELECT ri FROM RetailerInward ri " + "JOIN ri.adminStockOutward ao " + "JOIN ao.datas data "
//			+ "WHERE ri.org = :org " + "AND data.product.name LIKE %:search%")
//	Page<RetailerInward> findAllByOrgAndAdminStockOutwardDatasProductNameContaining(Pageable pageable, Org org,
//			String search);

	Page<RetailerInward> findAllByOrgAndAdminStockOutward_Datas_Product_NameContaining(Pageable pageable, Org org,
			String search);

	Page<RetailerInward> findAllDistinctByOrgAndAdminStockOutward_Datas_Product_NameContaining(Pageable pageable,
			Org org, String search);

}
