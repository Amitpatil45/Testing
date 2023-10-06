package com.root32.configsvc.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.root32.configsvc.service.JWTUserService;
import com.root32.configsvc.util.JWTUtil;
import com.root32.entity.Org;
import com.root32.entity.User;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private JWTUserService jwtUserService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		String authorization = httpServletRequest.getHeader("Authorization");
		String token = null;
		String userName = null;

		if (null != authorization && authorization.startsWith("Bearer")) {
			token = authorization.substring(7);
			userName = jwtUtil.getUserNameFromToken(token);
		}

		if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = (User) jwtUserService.loadUserByUsername(userName);

			if (jwtUtil.validateToken(token, user)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						user.getUsername(), null, user.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				Org userOrg = getUserOrg(user);
				httpServletRequest.setAttribute(User.LOGIN_USER, user);
				httpServletRequest.setAttribute(User.USER_ORG, userOrg);

			}
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private Org getUserOrg(User user) {
		return user.getOrg();
	}
}
