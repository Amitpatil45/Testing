package com.root32.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {

	Role findByCode(String string);

}
