package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;

@Repository
public interface OrgRepository extends JpaRepository<Org, Long> {

	long countByBusinessNameContainingIgnoreCase(String businessName);

	Org findByOrgTypeEnum(OrgTypeEnum admin);

	Org findByOrgCode(String string);

	

}
