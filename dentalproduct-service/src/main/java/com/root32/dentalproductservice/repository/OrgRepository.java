package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;

@Repository
public interface OrgRepository extends JpaRepository<Org, Long> {

	long countByBusinessNameContainingIgnoreCase(String businessName);

}
