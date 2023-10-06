//package com.root32.onboarding.service;
//
//import java.text.ParseException;
//
//import org.springframework.data.domain.Page;
//
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.Subscription;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//public interface SubscriptionService {
//
//	Page<Subscription> getAllSubscriptions(int page, int size);
//
//	Subscription fetchSubscriptionById(Long id);
//
//	GenericResponseEntity updateSubscription(Long id, Subscription subscription);
//
//	GenericResponseEntity deleteSubscriptionById(Long id);
//
//	GenericResponseEntity createSubscription(Subscription subscription, Org userOrg) throws ParseException;
//
//}
