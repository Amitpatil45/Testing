package com.root32.configsvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.root32.configsvc.repository.JWTUserRepository;
import com.root32.configsvc.service.JWTUserService;
import com.root32.entity.User;

@Service
public class JWTUserServiceImpl implements JWTUserService {

	@Autowired
	private JWTUserRepository jwtUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = jwtUserRepository.findByEmailId(username);
		if (user == null) {
			throw new IllegalArgumentException("Invalid UserName");
		}
		return user;
	}

}
