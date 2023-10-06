package com.root32.configsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.User;
import com.root32.entity.UserSession;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long>{

	Long countByUserAndUuId(User user, String sid);

}
