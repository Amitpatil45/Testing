//package com.root32.onboarding.controller;
//
//import java.text.ParseException;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.Subscription;
//import com.ameliorate.entity.User;
//import com.ameliorate.onboarding.service.SubscriptionService;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//@RestController
//@RequestMapping("/api/v1/")
//public class SubscriptionController {
//
//	private SubscriptionService subscriptionService;
//
//	public SubscriptionController(SubscriptionService subscriptionService) {
//		this.subscriptionService = subscriptionService;
//	}
//
//	@PostMapping("subscription")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> createSubscription(HttpServletRequest httpServletRequest, @RequestBody Subscription subscription) throws ParseException {
//		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
//		GenericResponseEntity gre = subscriptionService.createSubscription(subscription, userOrg);
//		return new ResponseEntity<>(gre, HttpStatus.CREATED);
//	}
//
//	@GetMapping("subscriptions")
//	@Transactional
//	public Page<Subscription> getAllSubscriptions(HttpServletRequest httpServletRequest,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
//
//		return subscriptionService.getAllSubscriptions(page, size);
//	}
//
//	@GetMapping("/subscription/{id}")
//	@Transactional
//	public Subscription fetchSubscriptionById(@PathVariable() Long id) {
//		return subscriptionService.fetchSubscriptionById(id);
//	}
//
//	@PutMapping("/subscription/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> updateSubscription(HttpServletRequest httpServletRequest,
//			@PathVariable("id") Long id, @RequestBody Subscription subscription) {
//		GenericResponseEntity gre = subscriptionService.updateSubscription(id, subscription);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//	}
//
//	@DeleteMapping("subscription/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> deleteSubscriptionById(@PathVariable("id") Long id) {
//		GenericResponseEntity gre = subscriptionService.deleteSubscriptionById(id);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//}
