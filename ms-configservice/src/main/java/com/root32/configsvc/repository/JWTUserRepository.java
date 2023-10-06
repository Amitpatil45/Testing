package com.root32.configsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.User;

@Repository
public interface JWTUserRepository extends JpaRepository<User, Long> {

	public User findByEmailId(String emailId);
	
}
