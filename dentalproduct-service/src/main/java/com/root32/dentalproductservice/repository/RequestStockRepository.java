package com.root32.dentalproductservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.RequestStatus;
import com.root32.entity.RequestStock;

@Repository
public interface RequestStockRepository extends JpaRepository<RequestStock, Long> {

	Page<RequestStock> findAllByFromOrgId(Long id, Pageable pageable);

	Page<RequestStock> findAllByToOrgId(Long id, Pageable pageable);

	List<RequestStock> findAllByFromOrgId(Long id);

	List<RequestStock> findAllByToOrgId(Long id);

	Page<RequestStock> findAllByFromOrgAndRequestDatas_Product_NameContaining(Pageable pageable, Org org,
			String search);

	Page<RequestStock> findAllByFromOrgAndRequestDatasProductNameContaining(Pageable pageable, Org org, String search);

	Page<RequestStock> findAllByFromOrg(Pageable pageable, Org org);

	Page<RequestStock> findAllByToOrgAndRequestDatasProductNameContaining(Pageable pageable, Org org, String search);

	Page<RequestStock> findAllByToOrgAndFromOrgBusinessNameContaining(Pageable pageable, Org org, String search);

	Page<RequestStock> findAllByFromOrgAndRequestStatus(Pageable pageable, Org org, RequestStatus status);

	Page<RequestStock> findAllByToOrgAndRequestStatus(Pageable pageable, Org org, RequestStatus status);

	Page<RequestStock> findAllDistinctByToOrgAndRequestDatasProductNameContaining(Pageable pageable, Org org,
			String search);

	Page<RequestStock> findAllDistinctByToOrgAndFromOrgBusinessNameContaining(Pageable pageable, Org org,
			String search);

	Page<RequestStock> findAllDistinctByFromOrgAndRequestDatasProductNameContaining(Pageable pageable, Org org,
			String search);

}
