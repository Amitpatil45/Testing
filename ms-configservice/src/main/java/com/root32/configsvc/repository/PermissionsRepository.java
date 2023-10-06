package com.root32.configsvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.root32.entity.Permission;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission, Long>{

	@Query(value="select * from permission left join role_permissions on  permission.id = role_permissions.permissions_id where role_id = :roleId",nativeQuery = true)
	List<Permission> findByRoleId(@Param("roleId")Long roleId);

}
