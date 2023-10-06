//package com.root32.onboarding.service.impl;
//
//import java.text.ParseException;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import com.ameliorate.configsvc.service.ConfigService;
//import com.ameliorate.entity.LicenseType;
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.Subscription;
//import com.ameliorate.onboarding.exception.RecordNotFoundException;
//import com.ameliorate.onboarding.repository.SubscriptionRepository;
//import com.ameliorate.onboarding.service.SubscriptionService;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//@Service
//public class DefaultSubscriptionService implements SubscriptionService {
//
//	private SubscriptionRepository subscriptionRepository;
//	private final ConfigService configService;
//
//	public DefaultSubscriptionService(SubscriptionRepository subscriptionRepository, ConfigService configService) {
//		this.subscriptionRepository = subscriptionRepository;
//		this.configService = configService;
//
//	}
//
//	@Override
//	public GenericResponseEntity createSubscription(Subscription subscription, Org userOrg) throws ParseException {
//		String number = generateSubscriptionNumber();
//		subscription.setNumber(number);
//		
//		if (subscription.getLicenseType() == LicenseType.DEMO) {
//			Date softExpiryDate = calcualteDate(Calendar.DATE, 15);
//			subscription.setSoftExpiryDate(softExpiryDate);
//			Date hardExpiryDate = calcualteDate(Calendar.DATE, 30);
//			subscription.setHardExpiryDate(hardExpiryDate);
//
//		} else if (subscription.getLicenseType() == LicenseType.MONTHLY) {
//			Date softExpiryDate = calcualteDate(Calendar.MONTH, 1);
//			subscription.setSoftExpiryDate(softExpiryDate);
//			Date hardExpiryDate = calcualteDate(Calendar.MONTH, 2);
//			subscription.setHardExpiryDate(hardExpiryDate);
//
//		} else {
//			Date softExpiryDate = calcualteDate(Calendar.YEAR, 1);
//			subscription.setSoftExpiryDate(softExpiryDate);
//			Date hardExpiryDate = calcualteDate(Calendar.MONTH, 15);
//			subscription.setHardExpiryDate(hardExpiryDate);
//		}
//
//		subscription = subscriptionRepository.save(subscription);
//		return new GenericResponseEntity(201, "Subscription Created Successfully", subscription.getId());
//	}
//
//	public String generateSubscriptionNumber() {
//		String prefix = "SUB";
//		String uniqueID = configService.getPlatformConfig().getNextSubscriptionNumber();
//		String number = prefix + "-" + uniqueID + "-" + Calendar.getInstance().get(Calendar.YEAR);
//		return number;
//	}
//
//	@Override
//	public Page<Subscription> getAllSubscriptions(int page, int size) {
//		if (page < 0) {
//			page = 0;
//		}
//		if (page < 0 || page > 100) {
//			page = 25;
//		}
//		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
//		return subscriptionRepository.findAll(pageable);
//	}
//
//	@Override
//	public Subscription fetchSubscriptionById(Long id) {
//		Optional<Subscription> subscription = subscriptionRepository.findById(id);
//		if (subscription.isEmpty()) {
//			throw new RecordNotFoundException("Subscription data not found");
//		}
//		return subscription.get();
//	}
//
//	@Override
//	public GenericResponseEntity updateSubscription(Long id, Subscription subscription) {
//		Subscription subscriptionDB = subscriptionRepository.findById(id).get();
//
//		if (subscription.getSubscriptionType() != null) {
//			subscriptionDB.setSubscriptionType(subscription.getSubscriptionType());
//		}
//		
//		if (subscription.getLicenseType() != null) {
//			subscriptionDB.setLicenseType(subscription.getLicenseType());
//		}
//		
//		if (subscription.getHardExpiryDate() != null) {
//			subscriptionDB.setHardExpiryDate(subscription.getHardExpiryDate());
//		}
//		
//		if (subscription.getSoftExpiryDate() != null) {
//			subscriptionDB.setSoftExpiryDate(subscription.getSoftExpiryDate());
//		}
//
//		subscriptionRepository.save(subscriptionDB);
//		return new GenericResponseEntity(201, "Subscription updated Successfully");
//	}
//
//	@Override
//	public GenericResponseEntity deleteSubscriptionById(Long id) {
//		subscriptionRepository.deleteById(id);
//		return new GenericResponseEntity(201, "Subscription deleted successfully!");
//	}
//
//	private Date calcualteDate(int field, int amount) {
//		Date currentDate = new Date();
//		Calendar c = Calendar.getInstance();
//		c.setTime(currentDate);
//		c.add(field, amount);
//		currentDate = c.getTime();
//		return currentDate;
//	}
//}
