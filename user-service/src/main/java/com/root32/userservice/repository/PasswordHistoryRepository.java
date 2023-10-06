package com.root32.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.PasswordHistory;
import com.root32.entity.User;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

	int countByUser(User dbUser);

	void deleteFirstByUserOrderByChangedDateAsc(User dbUser);

	List<PasswordHistory> findByUser(User userDB);

}
