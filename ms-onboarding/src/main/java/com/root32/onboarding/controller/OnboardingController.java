package com.root32.onboarding.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dto.GenericResponseEntity;
import com.root32.entity.Org;
import com.root32.entity.Retailer;
import com.root32.entity.SubRetailer;
import com.root32.entity.User;
import com.root32.onboarding.service.OnboardingService;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

	private OnboardingService onboardingService;

	public OnboardingController(OnboardingService onboardingService) {
		this.onboardingService = onboardingService;
	}

	@PostMapping("retailer")
	@Transactional
	public ResponseEntity<GenericResponseEntity> createRetailer(HttpServletRequest httpServletRequest,
			@RequestBody Retailer retailer) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		User loginUser = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		retailer.setParent(userOrg);
		GenericResponseEntity gre = onboardingService.createRetailer(retailer, loginUser);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("retailers")
	@Transactional
	public Page<Retailer> getAllRetailers(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return onboardingService.findAllRetailers(page, size, userOrg);
	}

	@GetMapping("retailer/{id}")
	public Retailer getretailerById(@PathVariable Long id) {
		return onboardingService.getRetailerById(id);
	}

	@PutMapping("/retailer/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> updateRetailer(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody Retailer retailer) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = onboardingService.updateRetailer(id, retailer, user);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("deleteOrg/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> deleteOrg(@PathVariable Long id) {
		GenericResponseEntity gre = onboardingService.deleteOrg(id);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PutMapping("retailer/isActivate/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> allowOrgOrNot(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestParam Boolean isActive) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = onboardingService.allowOrgOrNot(id, isActive, user);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@GetMapping("/retailerList")
	List<Retailer> allRetailerList() {
		return onboardingService.allRetailerList();
	}
//	---------------------------------------------------------------------------------------------------------
	// SUB - RETTAILER

	@PostMapping("subRetailer")
	@Transactional
	public ResponseEntity<GenericResponseEntity> createSubRetailer(HttpServletRequest httpServletRequest,
			@RequestBody SubRetailer subRetailer) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		User loginUser = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		subRetailer.setParent(userOrg);
		GenericResponseEntity gre = onboardingService.createSubRetailer(subRetailer, loginUser);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("subRetailers")
	@Transactional
	public Page<SubRetailer> getAllSubRetailers(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return onboardingService.findAllSubRetailers(page, size, userOrg);
	}

	@GetMapping("subRetailer/{id}")
	public SubRetailer getSubRetailerById(@PathVariable Long id) {
		return onboardingService.getSubRetailerById(id);
	}

	@GetMapping("/subRetailerList")
	List<SubRetailer> allSubRetailerList(HttpServletRequest httpServletRequest) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return onboardingService.allSubRetailerList(userOrg);
	}

	@PutMapping("/subRetailer/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> updateSubRetailer(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody SubRetailer subRetailer) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = onboardingService.updateSubRetailer(id, subRetailer, user);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

}
