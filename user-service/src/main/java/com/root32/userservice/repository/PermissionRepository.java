package com.root32.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.root32.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	@Query(value = "select * from permission left join role_permissions on  permission.id = role_permissions.permissions_id where role_id = :id", nativeQuery = true)
	List<Permission> findByRoleId(Long id);

}
