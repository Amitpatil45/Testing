package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.SaleData;

@Repository
public interface SaleDataRepository extends JpaRepository<SaleData, Long>{

}
