package com.root32.dentalproductservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.RetailerCase;

@Repository
public interface RetailerCaseRepository extends JpaRepository<RetailerCase,Long>{

	RetailerCase findTopByOrgOrderByIdDesc(Org org);

	Optional<RetailerCase> findByCodeAndOrg(String code, Org org);

}
