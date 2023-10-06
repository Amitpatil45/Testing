package com.root32.onboarding.service.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.root32.configsvc.exception.EmailMessageException;
import com.root32.configsvc.service.ConfigService;
import com.root32.configsvc.util.EmailHelper;
import com.root32.configsvc.util.MessageTemplateEnum;
import com.root32.dto.GenericResponseEntity;
import com.root32.entity.MessageTemplate;
import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;
import com.root32.entity.Retailer;
import com.root32.entity.Role;
import com.root32.entity.SubRetailer;
import com.root32.entity.User;
import com.root32.onboarding.exception.OnboardingServiceException;
import com.root32.onboarding.repository.AddressRepository;
import com.root32.onboarding.repository.MessageTemplateRepository;
import com.root32.onboarding.repository.OnboardingRepository;
import com.root32.onboarding.repository.OrgRepository;
import com.root32.onboarding.repository.RolesRepository;
import com.root32.onboarding.repository.SubRetailerRepository;
import com.root32.onboarding.repository.UserRepository;
import com.root32.onboarding.service.OnboardingService;

@Service
public class DefaultOnboardingService implements OnboardingService {

	private static final Logger LOG = LogManager.getLogger(DefaultOnboardingService.class);
	private static final String TOKEN_PATTERN_STR = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
	private static final Pattern TOKEN_PATTERN = Pattern.compile(TOKEN_PATTERN_STR);
	private final OnboardingRepository onboardingRepository;
	private final SubRetailerRepository subRetailerRepository;
	private ConfigService configService;
	@Autowired
	private MessageTemplateRepository messageTemplateRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private RolesRepository roleRepo;

	public DefaultOnboardingService(OnboardingRepository onboardingRepository,
			SubRetailerRepository subRetailerRepository, MessageTemplateRepository messageTemplateRepository,
			ConfigService configService) {

		this.onboardingRepository = onboardingRepository;
		this.subRetailerRepository = subRetailerRepository;
		this.configService = configService;
	}

	@Override
	public GenericResponseEntity createRetailer(Retailer retailer, User loginUser) {

		validateDuplicateData(retailer);
		validateMandatoryFields(retailer, 'c');
		retailer.setCreatedDate(new Date());
		retailer.setOrgCode("RET-" + generate7DigitNumber());
		retailer.setOrgTypeEnum(OrgTypeEnum.RETAILER);
		retailer.setIsActive(true);
		retailer.setIsHavingRecord(false);
		retailer.setCreatedBy(loginUser);
		retailer = onboardingRepository.save(retailer);
		updateUserRelatedField(retailer.getCreatedBy());
		int passwordLength = 6;
		String randomPassword = generateRandomPassword(passwordLength);
		User retailerFirstUser = createRetailerFirstUser(retailer, loginUser, randomPassword);

		sendOnboardingEmailWithUserCredentials(retailer.getEmailId(), retailer.getMobileNumber(), retailer.getParent(),
				randomPassword, retailerFirstUser.getEmailId(), 'r');

		return new GenericResponseEntity(201, "Retailer onboarding initiated");
	}

	private void validateMandatoryFields(Retailer retailer, char typeOfMethod) {
		if (retailer.getBusinessName() == null || retailer.getBusinessName().isBlank()) {
			throw new OnboardingServiceException("Please provide business name");
		}
		if (typeOfMethod == 'c') {
			if (retailer.getCommissionInPercent() == null || retailer.getCommissionInPercent() > 100
					|| retailer.getCommissionInPercent() < 0) {
//				throw new OnboardingServiceException("Please provide commission in percent");
				retailer.setCommissionInPercent((float) 0);
			}
			if (retailer.getEmailId() == null || retailer.getEmailId().isBlank()) {
				throw new OnboardingServiceException("Please provide email id");
			}
			if (retailer.getMobileNumber() == null || retailer.getMobileNumber().isBlank()) {
				throw new OnboardingServiceException("Please provide mobile number");
			}

		}

		if (retailer.getPocFirstName() == null || retailer.getPocFirstName().isBlank()) {
			throw new OnboardingServiceException("Please provide first name");
		}
		if (retailer.getPocLastName() == null || retailer.getPocLastName().isBlank()) {
			throw new OnboardingServiceException("Please provide last name");
		}
		if (retailer.getAddress() == null) {
			throw new OnboardingServiceException("Please provide address");
		}
		if (retailer.getAddress().getPincode() == null) {
			throw new OnboardingServiceException("Please provide pincode");
		}
		if (retailer.getAddress().getCity() == null || retailer.getAddress().getCity().isBlank()) {
			throw new OnboardingServiceException("Please provide city");
		}
		if (retailer.getAddress().getCountry() == null || retailer.getAddress().getCountry().isBlank()) {
			throw new OnboardingServiceException("Please provide country");
		}
		if (retailer.getAddress().getDistrict() == null || retailer.getAddress().getDistrict().isBlank()) {
			throw new OnboardingServiceException("Please provide district");
		}
		if (retailer.getAddress().getState() == null || retailer.getAddress().getState().isBlank()) {
			throw new OnboardingServiceException("Please provide state");
		}

	}

	private User createRetailerFirstUser(Retailer retailer, User loginUser, String randomPassword) {

		Long count = userRepo.countByOrg(retailer);

		if (count == 0) {
			Role retailerRole = (Role) roleRepo.findByCode("RET");
			User retailerFirstUser = new User();
			retailerFirstUser.setContactNumber(retailer.getMobileNumber());
			retailerFirstUser.setCreatedBy(loginUser);
			retailerFirstUser.setCreatedDate(new Date());
			retailerFirstUser.setEmailId(retailer.getEmailId());
			retailerFirstUser.setFirstName(retailer.getPocFirstName());
			retailerFirstUser.setLastName(retailer.getPocLastName());
			retailerFirstUser.setIsActive(true);
			retailerFirstUser.setIsOrgAdminUser(true);
			retailerFirstUser.setIsPasswordUpdate(false);
			retailerFirstUser.setIsHavingAnyRecords(false);
			retailerFirstUser.setOrg(retailer);
			retailerFirstUser.setIsHavingAnyRecords(false);
			String encodePassword = encodePassword(randomPassword);
			retailerFirstUser.setPassword(encodePassword);
			retailerFirstUser.addRoles(retailerRole);
			return userRepo.save(retailerFirstUser);

		}
		return null;
	}

	private String encodePassword(String password) {
		final String plainPassword = password;
		return new BCryptPasswordEncoder(12).encode(plainPassword);
	}

	public static String generateRandomPassword(int length) {
		final String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(charset.length());
			char randomChar = charset.charAt(randomIndex);
			password.append(randomChar);
		}

		return password.toString();
	}

	private void sendOnboardingEmailWithUserCredentials(String emailId, String mobileNumber, Org org,
			String userPassword, String userEmail, char type) {
		String subject = "Welcome on Root32 Platform";

		String templateKey;
		if (type == 'r') {
			templateKey = MessageTemplateEnum.ONBOARDING.toString();
		} else {
			templateKey = MessageTemplateEnum.ONBOARDING_SUBRETAILER.toString();
		}
		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);

		String adminName = org.getPocFirstName() + " " + org.getPocLastName();
		String adminContactNumber = org.getMobileNumber();
		String adminEmailId = org.getEmailId();

		try {
			EmailHelper.sendUserEmail(messageTemplate, emailId, subject, adminName, adminContactNumber, adminEmailId,
					userPassword, userEmail, mobileNumber);
		} catch (MessagingException e) {
			LOG.error("Error while sending welcome mail for User creation, to " + emailId, e);
			throw new EmailMessageException(e);
		}
	}

	private int generate7DigitNumber() {
		SecureRandom sr = new SecureRandom();
		int token = sr.nextInt(10000000);

		while (token < 1000000 || token > 10000000) {
			token = sr.nextInt(10000000);
		}
		return token;
	}

	private void validateDuplicateData(Retailer retailer) {
		String mobileNumber = retailer.getMobileNumber();
		String emailId = retailer.getEmailId();
		Retailer retailerDb = onboardingRepository.findFirstByMobileNumberOrEmailId(mobileNumber, emailId);

		if (retailerDb != null && retailerDb.getMobileNumber().equals(mobileNumber)) {
			LOG.error("Duplicate Record Found - Mobile Number - " + mobileNumber);
			throw new OnboardingServiceException("Duplicate Record Found - CompanyName");
		} else if (retailerDb != null && retailerDb.getEmailId().equals(emailId)) {
			LOG.error("Duplicate Record Found - EmailId - " + emailId);
			throw new OnboardingServiceException("Duplicate Record Found - EmailId");
		}

	}

	@Override
	public Page<Retailer> findAllRetailers(int page, int size, Org userOrg) {

		Pageable pageable = buildPagable(page, size);
		Page<Retailer> retailers = onboardingRepository.getretailers(pageable);
		return retailers;
	}

	private Pageable buildPagable(int page, int size) {
		if (page < 0) {
			page = 0;
		}
		if (size < 0 || size > 100) {
			size = 25;
		}
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
		return pageable;
	}

	@Override
	public Retailer getRetailerById(Long id) {
		return fetchRetailerById(id);
	}

	private Retailer fetchRetailerById(Long id) {
		Optional<Retailer> retailerOptional = onboardingRepository.findById(id);
		if (retailerOptional.isEmpty())
			throw new OnboardingServiceException("Retailer not availble for the given id");
		return retailerOptional.get();

	}

	@Override
	public GenericResponseEntity updateRetailer(Long id, Retailer retailer, User user) {
		Retailer retailerDB = getRetailerById(id);
		validateMandatoryFields(retailer, 'u');
		retailerDB.setUpdatedBy(user);
		retailerDB.setUpdateDate(new Date());
		retailerDB.setPocFirstName(retailer.getPocFirstName());
		retailerDB.setPocLastName(retailer.getPocLastName());
		retailerDB.setBusinessName(retailer.getBusinessName());
		retailerDB.setAddress(retailer.getAddress());
		onboardingRepository.save(retailerDB);
		updateUserRelatedField(retailerDB.getUpdatedBy());
		return new GenericResponseEntity(202, "Retailer updated successfully!");
	}

	private void updateRetailerUserAsPerIsActive(Org org) {
		if (org.getIsActive() == false) {
			List<User> userList = userRepo.findAllByOrg(org);
			for (User user : userList) {
				user.setIsActive(false);
				userRepo.save(user);
			}

		} else {
			List<User> userList = userRepo.findAllByOrg(org);
			for (User user : userList) {
				user.setIsActive(true);
				userRepo.save(user);
			}
		}
	}

	public User fetchUserById(Long id) {
		User user = userRepo.findById(id).orElse(null);
		if (user == null) {
			throw new OnboardingServiceException("User not available for this id");
		}
		return user;
	}

	public void updateUserRelatedField(User user) {

		User userDB = fetchUserById(user.getId());
		userDB.setIsHavingAnyRecords(true);
		userRepo.save(userDB);
		Org orgDB = fetchOrgById(userDB.getOrg().getId());
		orgDB.setIsHavingRecord(true);
		orgRepository.save(orgDB);
	}

	private Org fetchOrgById(Long id) {
		Org orgDB = orgRepository.findById(id).orElse(null);
		;
		if (orgDB == null) {
			throw new OnboardingServiceException("Org not available for this id");
		}
		return orgDB;
	}

	@Override
	public GenericResponseEntity deleteOrg(Long id) {

		Org orgDB = fetchOrgById(id);
		if (orgDB.getIsHavingRecord() == true) {
			throw new OnboardingServiceException(
					"Can't delete this Organisation.This organization is already used in record(s),but you can deactivate this organization");
		}
		Long addressId = orgDB.getAddress().getId();
		List<Long> userIdList = userRepo.getUserIdList(orgDB.getId());
		addressRepository.deleteById(addressId);
		userRepo.deleteAllByIdIn(userIdList);
		orgRepository.delete(orgDB);
		return new GenericResponseEntity(202, "Organization deleted successfully!");
	}

	@Override
	public GenericResponseEntity allowOrgOrNot(Long id, Boolean isActive, User user) {

		Org orgDB = fetchOrgById(id);
//		if (orgDB != null) {
		orgDB.setIsActive(isActive);
		orgDB.setUpdatedBy(user);
		orgDB.setUpdateDate(new Date());
		orgDB = orgRepository.save(orgDB);
		updateRetailerUserAsPerIsActive(orgDB);
		if (orgDB.getOrgTypeEnum().equals(OrgTypeEnum.RETAILER)) {

			if (orgDB.getIsActive() == false) {
				return new GenericResponseEntity(202, "Retailer deactivated Successfully");
			} else {
				return new GenericResponseEntity(202, "Retailer activated Successfully");
			}
		}
//		}
		if (orgDB.getOrgTypeEnum().equals(OrgTypeEnum.SUB_RETAILER)) {
			if (orgDB.getIsActive() == false) {
				return new GenericResponseEntity(202, "Sub-Retailer deactivated Successfully");
			} else {
				return new GenericResponseEntity(202, "Sub-Retailer activated Successfully");
			}
		}
		return new GenericResponseEntity(202, "User Data Updated Successfully");
	}

	@Override
	public List<Retailer> allRetailerList() {

		return onboardingRepository.findAllByIsActive(true);
	}

//---------------------------------------------------------------  SUB - RETAILER  --------------------------------------

	private void validateDuplicateData(SubRetailer subRetailer) {
		String mobileNumber = subRetailer.getMobileNumber();
		String emailId = subRetailer.getEmailId();
//		System.out.println("mobile no : " + mobileNumber);

//		SubRetailer subRetailerDB = subRetailerRepository.findFirstByMobileNumberOrEmailId(mobileNumber, emailId);
		Org orgDB = orgRepository.findByMobileNumberOrEmailId(mobileNumber, emailId);

//		System.out.println(subRetailerDB);
//		System.out.println(subRetailerDB.getMobileNumber());
		if (orgDB != null && orgDB.getMobileNumber().equals(mobileNumber)) {
			LOG.error("Duplicate Record Found - Mobile Number - " + mobileNumber);
			throw new OnboardingServiceException("Duplicate Record Found - Mobile Number");
		} else if (orgDB != null && orgDB.getEmailId().equals(emailId)) {
			LOG.error("Duplicate Record Found - EmailId - " + emailId);
			throw new OnboardingServiceException("Duplicate Record Found - EmailId");
		}

	}

	private void validateMandatoryFields(SubRetailer subRetailer, char typeOfMethod) {
		if (subRetailer.getBusinessName() == null || subRetailer.getBusinessName().isBlank()) {
			throw new OnboardingServiceException("Please provide business name");
		}
		if (typeOfMethod == 'c') {
			if (subRetailer.getCommissionInPercent() == null || subRetailer.getCommissionInPercent() > 100
					|| subRetailer.getCommissionInPercent() < 0) {
//				throw new OnboardingServiceException("Please provide commission in percent");
				subRetailer.setCommissionInPercent((float) 0);
			}
			if (subRetailer.getEmailId() == null || subRetailer.getEmailId().isBlank()) {
				throw new OnboardingServiceException("Please provide email id");
			}
			if (subRetailer.getMobileNumber() == null || subRetailer.getMobileNumber().isBlank()) {
				throw new OnboardingServiceException("Please provide mobile number");
			}

		}

		if (subRetailer.getPocFirstName() == null || subRetailer.getPocFirstName().isBlank()) {
			throw new OnboardingServiceException("Please provide first name");
		}
		if (subRetailer.getPocLastName() == null || subRetailer.getPocLastName().isBlank()) {
			throw new OnboardingServiceException("Please provide last name");
		}
		if (subRetailer.getAddress() == null) {
			throw new OnboardingServiceException("Please provide address");
		}
		if (subRetailer.getAddress().getPincode() == null) {
			throw new OnboardingServiceException("Please provide pincode");
		}
		if (subRetailer.getAddress().getCity() == null || subRetailer.getAddress().getCity().isBlank()) {
			throw new OnboardingServiceException("Please provide city");
		}
		if (subRetailer.getAddress().getCountry() == null || subRetailer.getAddress().getCountry().isBlank()) {
			throw new OnboardingServiceException("Please provide country");
		}
		if (subRetailer.getAddress().getDistrict() == null || subRetailer.getAddress().getDistrict().isBlank()) {
			throw new OnboardingServiceException("Please provide district");
		}
		if (subRetailer.getAddress().getState() == null || subRetailer.getAddress().getState().isBlank()) {
			throw new OnboardingServiceException("Please provide state");
		}

	}

	private User createSubRetailerFirstUser(SubRetailer subRetailer, User loginUser, String randomPassword) {

		Long count = userRepo.countByOrg(subRetailer);

		if (count == 0) {
			Role retailerRole = (Role) roleRepo.findByCode("SRT");
			User retailerFirstUser = new User();
			retailerFirstUser.setContactNumber(subRetailer.getMobileNumber());
			retailerFirstUser.setCreatedBy(loginUser);
			retailerFirstUser.setCreatedDate(new Date());
			retailerFirstUser.setEmailId(subRetailer.getEmailId());
			retailerFirstUser.setFirstName(subRetailer.getPocFirstName());
			retailerFirstUser.setLastName(subRetailer.getPocLastName());
			retailerFirstUser.setIsActive(true);
			retailerFirstUser.setIsOrgAdminUser(true);
			retailerFirstUser.setIsPasswordUpdate(false);
			retailerFirstUser.setIsHavingAnyRecords(false);
			retailerFirstUser.setOrg(subRetailer);
			retailerFirstUser.setIsHavingAnyRecords(false);
			String encodePassword = encodePassword(randomPassword);
			retailerFirstUser.setPassword(encodePassword);
			retailerFirstUser.addRoles(retailerRole);
			return userRepo.save(retailerFirstUser);

		}
		return null;
	}

	@Override
	public GenericResponseEntity createSubRetailer(SubRetailer subRetailer, User loginUser) {

		validateDuplicateData(subRetailer);
		validateMandatoryFields(subRetailer, 'c');

		subRetailer.setCreatedDate(new Date());
		subRetailer.setOrgCode("SRT-" + generate7DigitNumber());
		subRetailer.setOrgTypeEnum(OrgTypeEnum.SUB_RETAILER);
		subRetailer.setIsActive(true);
		subRetailer.setIsHavingRecord(false);
		subRetailer.setCreatedBy(loginUser);
		subRetailer = subRetailerRepository.save(subRetailer);
		updateUserRelatedField(subRetailer.getCreatedBy());

		int passwordLength = 6;
		String randomPassword = generateRandomPassword(passwordLength);
		User subRetailerFirstUser = createSubRetailerFirstUser(subRetailer, loginUser, randomPassword);

		sendOnboardingEmailWithUserCredentials(subRetailer.getEmailId(), subRetailer.getMobileNumber(),
				subRetailer.getParent(), randomPassword, subRetailerFirstUser.getEmailId(), 's');

		return new GenericResponseEntity(201, "Sub-Retailer onboarding initiated.");
	}

	@Override
	public Page<SubRetailer> findAllSubRetailers(int page, int size, Org userOrg) {

		Pageable pageable = buildPagable(page, size);
		Page<SubRetailer> subRetailers = subRetailerRepository.findAllByParent(userOrg, pageable);
		return subRetailers;
	}

	private SubRetailer fetchSubRetailerById(Long id) {
		Optional<SubRetailer> subRetailerOptional = subRetailerRepository.findById(id);
		if (subRetailerOptional.isEmpty()) {
			throw new OnboardingServiceException("Sub-Retailer not available for this id.");
		}
		return subRetailerOptional.get();
	}

	@Override
	public SubRetailer getSubRetailerById(Long id) {
		return fetchSubRetailerById(id);
	}

	@Override
	public List<SubRetailer> allSubRetailerList(Org org) {

		return subRetailerRepository.findAllByIsActiveAndParent(true, org);
	}

	@Override
	public GenericResponseEntity updateSubRetailer(Long id, SubRetailer subRetailer, User user) {

		SubRetailer subRtailerDB = fetchSubRetailerById(id);
		validateMandatoryFields(subRetailer, 'u');
		subRetailer.setBusinessName(subRetailer.getBusinessName().trim());

		if (!subRetailer.getBusinessName().equals(subRtailerDB.getBusinessName())) {
			Long count = subRetailerRepository.countByBusinessNameAndParent(subRetailer.getBusinessName(),
					subRtailerDB.getParent());
			if (count > 0) {
				throw new OnboardingServiceException("Duplicate business name!");
			}
		}
		subRtailerDB.setUpdatedBy(user);
		subRtailerDB.setUpdateDate(new Date());
		subRtailerDB.setPocFirstName(subRetailer.getPocFirstName());
		subRtailerDB.setPocLastName(subRetailer.getPocLastName());
		subRtailerDB.setBusinessName(subRetailer.getBusinessName());
		subRtailerDB.setAddress(subRetailer.getAddress());
		subRetailerRepository.save(subRtailerDB);
		updateUserRelatedField(user);
//		updateAdminUserFieldsOfSubRetailer(subRtailerDB, user);
		return new GenericResponseEntity(202, "Sub-Retailer updated successfully!");
	}

	public void updateAdminUserFieldsOfSubRetailer(SubRetailer subRetailer, User loginUser) {

		User adminUserDB = userRepo.findByEmailId(subRetailer.getEmailId());

		adminUserDB.setIsHavingAnyRecords(true);
		adminUserDB.setFirstName(subRetailer.getPocFirstName());
		adminUserDB.setLastName(subRetailer.getPocLastName());
		adminUserDB.setUpdatedBy(loginUser);
		adminUserDB.setUpdatedDate(new Date());
		userRepo.save(adminUserDB);

	}

//	@Override
//	public GenericResponseEntity updateRetailer(Long id, Retailer retailer, User user) {
//		Retailer retailerDB = getRetailerById(id);
//		validateMandatoryFields(retailer, 'u');
//		retailerDB.setUpdatedBy(user);
//		retailerDB.setUpdateDate(new Date());
//		retailerDB.setPocFirstName(retailer.getPocFirstName());
//		retailerDB.setPocLastName(retailer.getPocLastName());
//		retailerDB.setBusinessName(retailer.getBusinessName());
//		retailerDB.setAddress(retailer.getAddress());
//		onboardingRepository.save(retailerDB);
//		updateUserRelatedField(retailerDB.getUpdatedBy());
//		return new GenericResponseEntity(202, "Retailer updated successfully!");
//	}
}
