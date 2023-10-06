package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.User;
import com.root32.userservice.dto.UserOTP;

public interface UserOTPRepository extends JpaRepository<UserOTP, Long> {

	UserOTP findTopByUserOrderByCreatedDateDesc(User user);

}
