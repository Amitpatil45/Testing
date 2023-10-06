package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.User;
import com.root32.entity.UserAuthenticationOTP;

public interface UserAuthenticationOTPRepository extends JpaRepository<UserAuthenticationOTP, Long> {

	UserAuthenticationOTP findTopByUserOrderByCreatedDateDesc(User user);

}
