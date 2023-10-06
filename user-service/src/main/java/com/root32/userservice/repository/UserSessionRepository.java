package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.User;
import com.root32.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

	Long countByUser(User user);

	void deleteByUser(User user);
}
