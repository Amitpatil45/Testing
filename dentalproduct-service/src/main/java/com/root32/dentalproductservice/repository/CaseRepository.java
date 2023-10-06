package com.root32.dentalproductservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.AdminCase;

@Repository
public interface CaseRepository extends JpaRepository<AdminCase, Long> {

	AdminCase findTopByOrderByIdDesc();

	List<AdminCase> findAllByIsSealed(boolean b);

	Optional<AdminCase> findByCaseCode(String caseCode);

}
