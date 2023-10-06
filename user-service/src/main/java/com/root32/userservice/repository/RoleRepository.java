package com.root32.userservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Page<Role> findByOrg(Org userOrg, Pageable pageable);

	Page<Role> findByOrgAndNameContainingIgnoreCase(Org userOrg, String name, Pageable pageable);

	List<Role> findByOrgAndNameContainingIgnoreCase(Org userOrg, String name);

	List<Role> findByOrg(Org userOrg);

	long countByName(String name);

	long countByCodeIgnoreCase(String code);

	Role findByName(String string);

	List<Role> findByOrgAndIsEnabled(Org userOrg, boolean b);

	List<Role> findByOrgAndNameContainingIgnoreCaseAndIsEnabled(Org userOrg, String name, boolean b);

	Role findByCode(String string);

	long countByNameAndOrg(String name, Org org);

}
