package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.PoData;

@Repository
public interface PoDataRepository extends JpaRepository<PoData,Long>{

	Long countByProductId(Long id);

}
