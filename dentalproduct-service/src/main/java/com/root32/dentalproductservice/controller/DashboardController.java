package com.root32.dentalproductservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dentalproductservice.service.DashboardService;
import com.root32.dto.Dashboard;
import com.root32.entity.Org;
import com.root32.entity.User;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping
	public Dashboard getDashboard(HttpServletRequest request) {
		Org userOrg = (Org) request.getAttribute(User.USER_ORG);
		return dashboardService.getDashboard(userOrg);
	}

}
