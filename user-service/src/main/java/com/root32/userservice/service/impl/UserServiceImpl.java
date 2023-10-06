package com.root32.userservice.service.impl;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.util.StringUtils;

import com.root32.configsvc.config.ErrorMessageConstant;
import com.root32.configsvc.exception.EmailMessageException;
import com.root32.configsvc.service.ConfigService;
import com.root32.configsvc.util.EmailHelper;
import com.root32.configsvc.util.JWTUtil;
import com.root32.configsvc.util.MessageTemplateEnum;
import com.root32.configsvc.util.OTPGenerator;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.OtpDto;
import com.root32.dto.UserDto;
import com.root32.dto.UserPrincipal;
import com.root32.entity.MessageTemplate;
import com.root32.entity.Org;
import com.root32.entity.PasswordHistory;
import com.root32.entity.Permission;
import com.root32.entity.PermissionCategory;
import com.root32.entity.Role;
import com.root32.entity.User;
import com.root32.entity.UserAuthenticationOTP;
import com.root32.userservice.dto.LoginResponse;
import com.root32.userservice.dto.UserOTP;
import com.root32.userservice.exception.UserAuthenticationException;
import com.root32.userservice.repository.MessageTemplateRepository;
import com.root32.userservice.repository.OrgRepository;
import com.root32.userservice.repository.PasswordHistoryRepository;
import com.root32.userservice.repository.PermissionCategoryRepository;
import com.root32.userservice.repository.PermissionRepository;
import com.root32.userservice.repository.RoleRepository;
import com.root32.userservice.repository.UserAuthenticationOTPRepository;
import com.root32.userservice.repository.UserOTPRepository;
import com.root32.userservice.repository.UserRepository;
import com.root32.userservice.repository.UserSessionRepository;
import com.root32.userservice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private ConfigService configService;
	@Autowired
	private OrgRepository orgRepo;
	@Autowired
	private MessageTemplateRepository messageTemplateRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserOTPRepository userOTPRepository;
	@Autowired
	private UserSessionRepository userSessionRepository;
	@Autowired
	private PasswordHistoryRepository passwordHistoryRepository;
	@Autowired
	private UserAuthenticationOTPRepository userAuthenticationOTPRepository;
	@Autowired
	private PermissionRepository permissionRepo;
	@Autowired
	private PermissionCategoryRepository permissionCategoryRepo;

	@Override
	public GenericResponseEntity create(User user, User loginUser, Org org) {
		if (user == null || user.getEmailId() == null || user.getEmailId().trim().equals("")) {
			throw new UserAuthenticationException("User is null");
		}

		if (org == null || org.getId() == null) {
			throw new UserAuthenticationException(
					"Org cannot be null on user signup. User cannot be created. Kindly restart the process.");
		}
		Optional<Org> orgOptional = orgRepo.findById(org.getId());
		if (orgOptional.isEmpty()) {
			throw new UserAuthenticationException(
					"Org not found for given user. User cannot be created. Kindly restart the process.");
		}
		validateMandatoryField(user);
		validateDuplicateUser(user.getEmailId(), user.getContactNumber());
		updateAuditFields(user);

		encodePassword(user);
		user.setCreatedDate(new Date());
		user.setIsActive(true);
		user.setIsOrgAdminUser(false);
		user.setIsPasswordUpdate(false);
		user.setCreatedBy(loginUser);
		user.setOrg(org);
		user.setIsHavingAnyRecords(false);
		user = userRepo.save(user);
		updateUserRelatedField(loginUser);
//		sendUserCreationEmail(emailId, user.getOrg(), userPassword, userEmail);
		return new GenericResponseEntity(201, "User created successfully");
	}

	public void updateUserRelatedField(User user) {

		User userDB = fetchUserById(user.getId());
		userDB.setIsHavingAnyRecords(true);
		userRepo.save(userDB);
		Org orgDB = fetchOrgById(userDB.getOrg().getId());
		orgDB.setIsHavingRecord(true);
		orgRepo.save(orgDB);
	}

	private Org fetchOrgById(Long id) {
		Org orgDB = orgRepo.findById(id).orElse(null);

		if (orgDB == null) {
			throw new UserAuthenticationException("Org not available for this id");
		}
		return orgDB;
	}

	private void validateMandatoryField(User user) {
		if (user.getFirstName() == null || user.getFirstName().isBlank()) {
			throw new UserAuthenticationException("Please provide first name");
		}
		if (user.getLastName() == null || user.getLastName().isBlank()) {
			throw new UserAuthenticationException("Please provide last name");
		}
		if (user.getEmailId() == null || user.getEmailId().isBlank()) {
			throw new UserAuthenticationException("Please provide email id");
		}
		if (user.getContactNumber().length() != 10) {
			throw new UserAuthenticationException("Invalid mobile number length!");
		}
		if (user.getContactNumber() == null || user.getContactNumber().isBlank()) {
			throw new UserAuthenticationException("Please provide contact number");
		}
	}

//	private void sendUserCreationEmail(String emailId, Org org, String userPassword, String userEmail) {
//		String subject = "Welcome on LogSolutec Platform";
//		String templateKey = MessageTemplateEnum.USER_CREATION.toString();
//		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);
//
//		String adminName = org.getPocFirstName() + " " + org.getPocLastName();
//		String adminContactNumber = org.getMobileNumber();
//		String adminEmailId = org.getEmailId();
//
//		try {
//			EmailHelper.sendUserEmail(messageTemplate, emailId, subject, adminName, adminContactNumber, adminEmailId,
//					userPassword, userEmail);
//		} catch (MessagingException e) {
//			LOG.error("Error while sending welcome request for User creation, to " + emailId, e);
//			throw new EmailMessageException(e);
//		}
//	}

	private void updateAuditFields(User user) {

		user.setCreatedDate(new Date());
		int expiryDays = getPasswordExpiryDays();
		Date expiryDate = calcualteExpiryDate(Calendar.DATE, expiryDays);
		user.setExpiryDate(expiryDate);
		user.setIsLocked(false);
	}

	private Date calcualteExpiryDate(int field, int amount) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(field, amount);
		currentDate = c.getTime();
		return currentDate;
	}

	private int getPasswordExpiryDays() {
//		CompanyConfig companyConfig = configService.getCompanyConfig();
//		int passwordExpiryDays = companyConfig.getPasswordExpiryDays();
		int passwordExpiryDays = 365;
		passwordExpiryDays = passwordExpiryDays == 0 ? 60 : passwordExpiryDays;
		return passwordExpiryDays;
	}

	private void encodePassword(User user) {
		final String plainPassword = user.getPassword();
		user.setPassword(new BCryptPasswordEncoder(12).encode(plainPassword));
	}

	private void validateDuplicateUser(String emailId, String contactNumber) {
		User user = userRepo.findByEmailId(emailId);
		if (user != null) {
			throw new UserAuthenticationException("User with same email already exists!");
		}
		Long count = userRepo.countByContactNumber(contactNumber);
		if (count > 0) {
			throw new UserAuthenticationException("User with same contact number already exists!");

		}
	}

	@Override
	public LoginResponse createUserSession(UserPrincipal userPrincipal, String remoteAddress,
			HashMap<String, Object> requestHeaders) {

		logClientInfo(userPrincipal.getUserDetail(), remoteAddress, requestHeaders, "Login");
		User user = validateUserExistsAndLoginIsAllowed(userPrincipal);
		LoginResponse lr = new LoginResponse();
		String token = doCreateUserSession(user);
		if (user.getIsActive() == false) {
			throw new UserAuthenticationException("Unable to login.Please contact to your organization admin");
		}

//		sendUserAuthenticationCode(user, remoteAddress, requestHeaders);
		Set<Role> roles = user.getRoles();
		if (roles != null && roles.size() > 0) {

			for (Role r : roles) {
				Long roleId = r.getId();
				List<Permission> permissionList = permissionRepo.findByRoleId(roleId);
				Set<Permission> permissions = Set.copyOf(permissionList);
				lr.getAuthorities().addAll(permissions.stream().map(p -> p.getCode()).collect(Collectors.toList()));

			}
		}

		lr.setUserId(user.getId());
		lr.setEmailId(user.getEmailId());
		lr.setOrgType(user.getOrg().getOrgTypeEnum());
		lr.setOrgId(user.getOrg().getId());
		lr.setOrgName(user.getOrg().getBusinessName());
		lr.setUserFirstName(user.getFirstName());
		lr.setUserLastName(user.getLastName());
		lr.setIsPasswordUpdate(user.getIsPasswordUpdate());
		lr.setToken(token);
		return lr;
	}

	public void sendUserAuthenticationCode(User user, String remoteAddress, HashMap<String, Object> requestHeaders) {

		String OTP = OTPGenerator.generate();
		persistAuthenticationOTP(OTP, user);
		String subject = "User Authentication - OTP";

		String templateKey = MessageTemplateEnum.USER_AUTHENTICATION_OTP.toString();
		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);
		try {
			EmailHelper.sendAuthenticationOTPEmail(messageTemplate, user.getEmailId(), OTP, subject);
		} catch (MessagingException e) {
			LOG.error("Error while sending OTP to " + user.getEmailId(), e);
			throw new EmailMessageException(e);
		}

	}

	private void persistAuthenticationOTP(final String OTP, User user) {

		int otpExpiryInMinutes = configService.getCompanyConfig().getOTPExpiryMinutes();
		Date otpExpiryDate = calcualteExpiryDate(Calendar.MINUTE, otpExpiryInMinutes);

		UserAuthenticationOTP userOTP = new UserAuthenticationOTP();
		userOTP.setOTP(OTP);
		userOTP.setUser(user);
		userOTP.setCreatedDate(new Date());
		userOTP.setExpiryDate(otpExpiryDate);

		userAuthenticationOTPRepository.save(userOTP);
	}

	private String doCreateUserSession(User user) {
		String token = jwtUtil.generateToken(user);
		return token;
	}

	private void logClientInfo(String emailId, String remoteAddress, HashMap<String, Object> requestHeaders,
			String operation) {

		LOG.debug("[ {} ] attempt by [ {} ] from remoteAddress [ {} ]", operation, emailId, remoteAddress);
		if (requestHeaders != null) {
			LOG.debug("RequestHeaders for [ {} ] ", emailId);
			for (String key : requestHeaders.keySet()) {
				LOG.debug("Key [ {} ] - Value [ {} ]", key, requestHeaders.get(key));
			}
		}

	}

	private User validateUserExistsAndLoginIsAllowed(UserPrincipal userPrincipal) {
		if (userPrincipal == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.BAD_REQUEST_USER_CANNOT_BE_FORMED);
		}
		User user = null;
		user = userRepo.findByEmailId(userPrincipal.getUserDetail());
		if (user == null) {
			user = userRepo.findByContactNumber(userPrincipal.getUserDetail());
		}
		if (user == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_CREDENTIALS);
		}
		validatePassword(user, userPrincipal);
//		validatePasswordIsNotExpired(user);
		// TODO generateWarningForPasswordExpiryingWithinWeek
		return user;
	}

	private void validatePasswordIsNotExpired(User user) {

		Date currentDate = new Date();
		Date passwordExpiryDate = user.getExpiryDate();
		if (currentDate.after(passwordExpiryDate)) {
			throw new UserAuthenticationException(ErrorMessageConstant.PASSWORD_IS_EXPIRED);
		}
	}

	private void validatePassword(final User user, UserPrincipal userPrincipal) {

		if (!new BCryptPasswordEncoder(12).matches(userPrincipal.getPassword(), user.getPassword())) {
//			User dbUser = logUnsuccessfulAttemps(user);
//			if (dbUser.isLocked()) {
//				throw new UserAuthenticationException(ErrorMessageConstant.USER_IS_LOCKED);
//			}
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_CREDENTIALS);
		}

	}
//	private User logUnsuccessfulAttemps(User user) {
//		int unsuccessfulAttemps = user.getUnsuccessfulAttemps();
//		unsuccessfulAttemps++;
//		user.setUnsuccessfulAttemps(unsuccessfulAttemps);
//		LOG.debug("UnsuccessfulAttemps for user [ {} ] is now [ {} ]", user.getEmailId(), unsuccessfulAttemps);
//		int maxUnsuccessfulAttemps = configService.getCompanyConfig().getMaxUnsuccessfulAttemps();
//		boolean lockAfterMaxUnsuccessfulAttemps = configService.getCompanyConfig()
//				.shouldLockAfterMaxUnsuccessfulAttemps();
//		if (lockAfterMaxUnsuccessfulAttemps && unsuccessfulAttemps > maxUnsuccessfulAttemps) {
//			user.setIsLocked(true);
//			LOG.error("User [ {} ] has crossed MaxUnsuccessfulAttemps [ {} ], hence locking the user!!!!",
//					user.getEmailId(), maxUnsuccessfulAttemps);
//		}
//		User dbUser = userRepo.save(user);
//		return dbUser;
//
//	}

	@Override
	public GenericResponseEntity createOtp(OtpDto otpDto, String remoteAddress,
			HashMap<String, Object> requestHeaders) {
		logClientInfo(otpDto.getUserDetail(), remoteAddress, requestHeaders, "Forgot Password");
		User user = null;
		user = getUserByContactNumber(otpDto.getUserDetail());
		if (user == null) {
			user = getUserByEmailId(otpDto.getUserDetail());
		}
		String OTP = OTPGenerator.generate();

		persistOTP(OTP, user);

		String subject = "Reset Password - OTP";
		String templateKey = MessageTemplateEnum.FORGOT_PASSWORD_OTP.toString();
		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);

		try {
			EmailHelper.sendForgotPasswordOTPEmail(messageTemplate, user.getEmailId(), OTP, subject);
		} catch (MessagingException e) {
			LOG.error("Error while sending OTP to " + user.getEmailId(), e);
			throw new EmailMessageException(e);
		}

		return new GenericResponseEntity(200, "OTP sent on email");
	}

	private User getUserByContactNumber(final String contactNumber) {
		if (!StringUtils.hasLength(contactNumber)) {
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_INPUT);
		}
		User user = userRepo.findByContactNumber(contactNumber);

//		if (user == null) {
//			LOG.error("User [ {} ] is not found in system", contactNumber);
//			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_CREDENTIALS);
//		}

		return user;
	}

	private void persistOTP(final String OTP, User user) {

		int otpExpiryInMinutes = configService.getCompanyConfig().getOTPExpiryMinutes();
		Date otpExpiryDate = calcualteExpiryDate(Calendar.MINUTE, otpExpiryInMinutes);

		UserOTP userOTP = new UserOTP();
		userOTP.setOTP(OTP);
		userOTP.setUser(user);
		userOTP.setCreatedDate(new Date());
		userOTP.setExpiryDate(otpExpiryDate);

		userOTPRepository.save(userOTP);
	}

	@Override
	public GenericResponseEntity validateOtp(OtpDto otpDto) {
		User user = null;
		user = getUserByContactNumber(otpDto.getUserDetail());
		if (user == null) {
			user = getUserByEmailId(otpDto.getUserDetail());
		}

		UserOTP userOTP = userOTPRepository.findTopByUserOrderByCreatedDateDesc(user);
		validateUserOTP(userOTP, otpDto.getOtp(), user.getEmailId());
		return new GenericResponseEntity(202, "OTP validated successfully");

	}

	private User getUserByEmailId(String emailId) {
		if (!StringUtils.hasLength(emailId)) {
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_INPUT);
		}
		User user = userRepo.findByEmailId(emailId);

		if (user == null) {
			LOG.error("User [ {} ] is not found in system", emailId);
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_CREDENTIALS);
		}

		return user;
	}

	private void validateUserOTP(UserOTP userOTP, String otp, String emailId) {

		if (!StringUtils.hasLength(otp)) {
			LOG.error("Invalid OPT [ {} ] sent", otp);
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_INPUT);
		}

		if (userOTP == null) {
			LOG.error("No User OTP record found for {}", emailId);
			throw new UserAuthenticationException(ErrorMessageConstant.USER_OTP_RECORD_NOT_FOUND);
		}

		if (!userOTP.getOTP().equals(otp)) {
			LOG.error("OTP did not match for : {}, db OTP : {} and ui OTP : {} ", emailId, userOTP.getOTP(), otp);
			throw new UserAuthenticationException(ErrorMessageConstant.OTP_DID_NOT_MATCH);
		}

		if (new Date().after(userOTP.getExpiryDate())) {
			LOG.error("OTP is expired, db expiry : {} and current : {} ", userOTP.getExpiryDate(), new Date());
			throw new UserAuthenticationException(ErrorMessageConstant.OTP_EXPIRED);
		}
	}

	@Override
	public GenericResponseEntity update(OtpDto otpDto) {
		if (otpDto == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.BAD_REQUEST_USER_CANNOT_BE_FORMED);
		}
		String userDetail = otpDto.getUserDetail();

		User dbUserByEmail = userRepo.findByEmailId(userDetail);
		User dbUserbyContactNumber = userRepo.findByContactNumber(otpDto.getUserDetail());

		if (dbUserByEmail != null && dbUserbyContactNumber == null) {
			dbUserByEmail.setUpdatedDate(new Date());
			String encodePassword = encodePassword(otpDto.getPassword());
			dbUserByEmail.setPassword(encodePassword);
			dbUserByEmail.setIsPasswordUpdate(true);
			userRepo.save(dbUserByEmail);
			System.out.println("is password =" + dbUserByEmail.getIsPasswordUpdate());
		}
		if (dbUserByEmail == null && dbUserbyContactNumber != null) {
			dbUserbyContactNumber.setUpdatedDate(new Date());
			String encodePassword = encodePassword(otpDto.getPassword());
			dbUserbyContactNumber.setPassword(encodePassword);
			dbUserbyContactNumber.setIsPasswordUpdate(true);
			userRepo.save(dbUserbyContactNumber);
			System.out.println("is password =" + dbUserbyContactNumber.getIsPasswordUpdate());

		}

		return new GenericResponseEntity(202, "User password updated successfully");
	}

	private String encodePassword(String password) {
		final String plainPassword = password;
		return new BCryptPasswordEncoder(12).encode(plainPassword);
	}

	private void validateDuplicateRecordsWhileUpdate(User user, User dbUser) {
		if (!user.getContactNumber().equals(dbUser.getContactNumber())) {
			Long count = userRepo.countByContactNumber(user.getContactNumber());
			if (count > 0) {
				throw new UserAuthenticationException("Another user is already exist with this contact number");
			}
		}
		if (!user.getEmailId().equals(dbUser.getEmailId())) {
			Long count = userRepo.countByEmailId(user.getEmailId());
			if (count > 0) {
				throw new UserAuthenticationException("Another user is already exist with this email id");
			}
		}
	}

	@Override
	public Page<UserDto> getAllUsers(int page, int size, Org userOrg, User loginUser) {
		Pageable pageable = buildPagable(page, size);

		return userRepo.findAllByOrgAndIsOrgAdminUser(userOrg, false, pageable);
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
	public User fetchUserById(Long id) {
		User user = userRepo.findById(id).orElse(null);
		if (user == null) {
			throw new UserAuthenticationException("User not available for the given id");
		}
		user.setPassword(null);
		return user;
	}

	@Override
	public GenericResponseEntity updatePassword(UserPrincipal userPrincipal, User loggedUser) {
		if (userPrincipal == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.BAD_REQUEST_USER_CANNOT_BE_FORMED);
		}
		User dbUser = null;
		dbUser = userRepo.findByEmailId(loggedUser.getEmailId());
		if (dbUser == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_CREDENTIALS);
		}
		if (userPrincipal.getPassword() != null && !userPrincipal.getPassword().isBlank()) {
			validatePasswordByUser(dbUser, userPrincipal);
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
//			persistPasswordHistoryForUpdate(dbUser, userPrincipal, bCryptPasswordEncoder);
			dbUser.setPassword(bCryptPasswordEncoder.encode(userPrincipal.getPassword()));
			dbUser.setIsPasswordUpdate(true);
		}
		userRepo.save(dbUser);

		return new GenericResponseEntity(202, "User Password Updated Successfully");
	}

	private void validatePasswordByUser(final User dbUser, UserPrincipal userPrinciple) {

		if (!new BCryptPasswordEncoder(12).matches(userPrinciple.getCurrentPassword(), dbUser.getPassword())) {
			throw new UserAuthenticationException(ErrorMessageConstant.CURRENT_PASSWORD_NOT_MATCH);
		}

	}

	public void persistPasswordHistoryForUpdate(User dbUser, UserPrincipal userPrinciple,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		validateLast3PasswordForUpdate(dbUser, userPrinciple, bCryptPasswordEncoder);
		int count = passwordHistoryRepository.countByUser(dbUser);
		if (count > 1) {
			passwordHistoryRepository.deleteFirstByUserOrderByChangedDateAsc(dbUser);
		}
		PasswordHistory ph = new PasswordHistory();
		ph.setPassword(dbUser.getPassword());
		ph.setChangedDate(new Date());
		ph.setUser(dbUser);
		passwordHistoryRepository.save(ph);
	}

	public void validateLast3PasswordForUpdate(User userDB, UserPrincipal user,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		if (bCryptPasswordEncoder.matches(user.getPassword(), userDB.getPassword())) {
			throw new UserAuthenticationException("Password cannot be same as last 3 passwords");
		}
		List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findByUser(userDB);
		for (PasswordHistory passwordHistory : passwordHistoryList) {
			if (bCryptPasswordEncoder.matches(user.getPassword(), passwordHistory.getPassword())) {
				throw new UserAuthenticationException("Password cannot be same as last 3 passwords");
			}
		}
	}

	/* TODO the following methods to be used in update */

	public void persistPasswordHistory(User dbUser, User user, BCryptPasswordEncoder bCryptPasswordEncoder) {
		validateLast3Password(dbUser, user, bCryptPasswordEncoder);
		int count = passwordHistoryRepository.countByUser(dbUser);
		if (count > 1) {
			passwordHistoryRepository.deleteFirstByUserOrderByChangedDateAsc(dbUser);
		}
		PasswordHistory ph = new PasswordHistory();
		ph.setPassword(dbUser.getPassword());
		ph.setChangedDate(new Date());
		ph.setUser(dbUser);
		passwordHistoryRepository.save(ph);
	}

	public void validateLast3Password(User userDB, User user, BCryptPasswordEncoder bCryptPasswordEncoder) {
		if (bCryptPasswordEncoder.matches(user.getPassword(), userDB.getPassword())) {
			throw new UserAuthenticationException("Password cannot be same as last 3 passwords");
		}
		List<PasswordHistory> passwordHistoryList = passwordHistoryRepository.findByUser(userDB);
		for (PasswordHistory passwordHistory : passwordHistoryList) {
			if (bCryptPasswordEncoder.matches(user.getPassword(), passwordHistory.getPassword())) {
				throw new UserAuthenticationException("Password cannot be same as last 3 passwords");
			}
		}
	}

	@Override
	public GenericResponseEntity createAuthenticationOtp(String contactNumber, String remoteAddress,
			HashMap<String, Object> requestHeaders) {
		logClientInfo(contactNumber, remoteAddress, requestHeaders, "Authentication");

		User user = getUserByContactNumber(contactNumber);

		String OTP = OTPGenerator.generate();

		persistAuthenticationOTP(OTP, user);

		String subject = "User Authentication - OTP";
		String templateKey = MessageTemplateEnum.USER_AUTHENTICATION_OTP.toString();
		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);

		try {
			EmailHelper.sendAuthenticationOTPEmail(messageTemplate, user.getEmailId(), OTP, subject);
		} catch (MessagingException e) {
			LOG.error("Error while sending OTP to " + user.getEmailId(), e);
			throw new EmailMessageException(e);
		}

		return new GenericResponseEntity(200, "OTP sent on email");
	}

	@Override
	public GenericResponseEntity validateAuthenticationOtp(String contactNumber, String otp) {

		User user = getUserByContactNumber(contactNumber);
		UserAuthenticationOTP userAuthenticationOTP = userAuthenticationOTPRepository
				.findTopByUserOrderByCreatedDateDesc(user);
		validateUserAuthentictionOTP(userAuthenticationOTP, otp, user.getEmailId());
		boolean authenticationVerified = true;
		return new GenericResponseEntity(202, "OTP validated successfully", authenticationVerified);
	}

	private void validateUserAuthentictionOTP(UserAuthenticationOTP userAuthenticationOTP, String otp, String emailId) {

		if (!StringUtils.hasLength(otp)) {
			LOG.error("Invalid OPT [ {} ] sent", otp);
			throw new UserAuthenticationException(ErrorMessageConstant.INVALID_INPUT);
		}

		if (userAuthenticationOTP == null) {
			LOG.error("No User OTP record found for {}", emailId);
			throw new UserAuthenticationException(ErrorMessageConstant.USER_OTP_RECORD_NOT_FOUND);
		}

		if (!userAuthenticationOTP.getOTP().equals(otp)) {
			LOG.error("OTP did not match for : {}, db OTP : {} and ui OTP : {} ", emailId,
					userAuthenticationOTP.getOTP(), otp);
			throw new UserAuthenticationException(ErrorMessageConstant.OTP_DID_NOT_MATCH);
		}
		if (new Date().after(userAuthenticationOTP.getExpiryDate())) {
			LOG.error("OTP is expired, db expiry : {} and current : {} ", userAuthenticationOTP.getExpiryDate(),
					new Date());
			throw new UserAuthenticationException(ErrorMessageConstant.OTP_EXPIRED);
		}

	}

	@Override
	public GenericResponseEntity updateUserProfile(User user, User loggedUser) {

		validateMandatoryField(user);

		if (user == null) {
			throw new UserAuthenticationException(ErrorMessageConstant.BAD_REQUEST_USER_CANNOT_BE_FORMED);
		}
		Long userId = user.getId();
		User dbUser = null;
		if (userId != null) {
			dbUser = userRepo.findById(userId).orElse(null);
		} 
		if (dbUser == null) {
			throw new UserAuthenticationException("User not available");
		}
		validateDuplicateRecordsWhileUpdate(user, dbUser);
		dbUser.setUpdatedBy(loggedUser);
		dbUser.setUpdatedDate(new Date());
		dbUser.setFirstName(user.getFirstName());
		dbUser.setLastName(user.getLastName());
		dbUser.setContactNumber(user.getContactNumber());
		userRepo.save(dbUser);

		Set<Role> userRoles = user.getRoles();
		if (userRoles != null && userRoles.size() > 0) {
			Set<Role> dbRoles = new HashSet<Role>();
			for (Role role : userRoles) {
				Role dbRole = roleRepo.getById(role.getId());
				dbRoles.add(dbRole);
			}
			dbUser.getRoles().clear();
			userRepo.save(dbUser);
			dbUser.getRoles().addAll(dbRoles);
			userRepo.save(dbUser);
		}

		return new GenericResponseEntity(202, "User Data Updated Successfully");
	}

	@Override
	public GenericResponseEntity allowUserOrNot(Long id, Boolean isActive) {

		User userDB = userRepo.findById(id).get();
		System.out.println("password  " + userDB.getPassword());

		if (userDB != null) {
			userDB.setIsActive(isActive);
			userDB = userRepo.save(userDB);
			if (userDB.getIsActive() == false) {
				return new GenericResponseEntity(202, "User deactivated Successfully");
			} else {
				return new GenericResponseEntity(202, "User activated Successfully");
			}
		}
		return new GenericResponseEntity(202, "User Data Updated Successfully");
	}

	@Override
	public GenericResponseEntity deleteUser(Long id) {

		User userDb = fetchUserById(id);
		if (userDb.getIsHavingAnyRecords() == true) {
			throw new UserAuthenticationException(
					"Can't delete this user.This user is already used in record(s),but you can deactivate the user");
		}
		userRepo.deleteById(id);
		return new GenericResponseEntity(202, "User deleted Successfully");
	}

	@Override
	public Page<Role> fetchAllRoles(int page, int size, User loginUser, Org userOrg, String name) {
		Pageable pageable = buildPagable(page, size);
		if (name == null || name.isBlank()) {
			return roleRepo.findByOrg(userOrg, pageable);
		}
		return roleRepo.findByOrgAndNameContainingIgnoreCase(userOrg, name, pageable);

	}

	@Override
	public List<Role> getAllRoles(Org userOrg, String name) {
		if (name == null || name.isBlank()) {
			return roleRepo.findByOrgAndIsEnabled(userOrg, true);
		}
		return roleRepo.findByOrgAndNameContainingIgnoreCaseAndIsEnabled(userOrg, name, true);

	}

	@Override
	public Page<Permission> fetchAllPermissions(int page, int size) {
		Pageable pageable = buildPagable(page, size);
		return permissionRepo.findAll(pageable);
	}

	@Override
	public List<Permission> getAllPermissions(User loginUser) {
		Long id = getLeastRoleId(loginUser);
		System.out.println("id  " + id);
		return permissionRepo.findByRoleId(id);
	}

	private Long getLeastRoleId(User user) {

		Set<Role> roles = user.getRoles();
		System.out.println("roles " + roles);
		Long id = 0l;
		for (Role r : roles) {
			if (r.getId() > id) {
				id = r.getId();
			} else {
				break;
			}
		}
		return id;

	}

	private Role getSuperAdminRoleFromUser(User user) {
		return user.getRoles().stream().filter(p -> p.getCode().equalsIgnoreCase("ADM")).findAny().orElse(null);
	}

	@Override
	public List<PermissionCategory> getAllPermissionCategories() {
		return permissionCategoryRepo.findAll();
	}

	@Override
	public Permission getPermissionById(Long id) throws UserAuthenticationException {
		return permissionRepo.findById(id)
				.orElseThrow(() -> new UserAuthenticationException("Permission not found for given id"));
	}

	@Override
	public Role getRoleById(Long id) throws UserAuthenticationException {
		return roleRepo.findById(id).orElseThrow(() -> new UserAuthenticationException("Role not found for given id"));
	}

	@Override
	public GenericResponseEntity updateRole(Role role, Org userOrg, User loginUser) throws UserAuthenticationException {
		if (StringUtils.isEmpty(role.getName()))
			throw new UserAuthenticationException("Please fill up role name");

		Role roleDb = getRoleById(role.getId());
		if (!roleDb.getName().equalsIgnoreCase(role.getName())) {
			long countDb = roleRepo.countByNameAndOrg(role.getName(), userOrg);
			if (countDb > 0)
				throw new UserAuthenticationException("Role name already exists!");
		}

		roleDb.setName(role.getName());
		roleDb.setVisible(true);

		if (!roleDb.getPermissions().isEmpty()) {
			roleDb.getPermissions().clear();
			roleDb.getPermissions().addAll(role.getPermissions());
		}
		roleDb.setUpdatedDate(new Date());
		roleDb.setUpdatedBy(loginUser);
		roleRepo.save(roleDb);
		return new GenericResponseEntity(202, "Role Updated Successfully");
	}

	private void validateRoleName(long count, String name, Org org) throws UserAuthenticationException {
		if (StringUtils.isEmpty(name))
			throw new UserAuthenticationException("Please fill up role name");
		long countDb = roleRepo.countByNameAndOrg(name, org);
		if (countDb > count)
			throw new UserAuthenticationException("Role name already exists!");

	}

	@Override
	public GenericResponseEntity updatePermission(Permission permission) throws UserAuthenticationException {
		Permission permissionDb = getPermissionById(permission.getId());

		if (!StringUtils.isEmpty(permission.getLabel()))
			permissionDb.setLabel(permission.getLabel());

		permissionRepo.save(permissionDb);
		return new GenericResponseEntity(202, "Permission Updated Successfully");
	}

	@Override
	public GenericResponseEntity deleteRole(Long id) throws UserAuthenticationException {
		Role roleDb = getRoleById(id);
		List<User> users = userRepo.findByRoles(roleDb);
		if (roleDb.getPermissions().isEmpty() && users.isEmpty()) {
			roleRepo.delete(roleDb);
			return new GenericResponseEntity(202, "Role deleted Successfully");
		} else {
//			roleDb.setEnabled(false);
//			roleRepo.save(roleDb);
//			return new GenericResponseEntity(202, "Can't delete,due to pemissions assigned or it is assigned to users.But you can disable this Role");
			throw new UserAuthenticationException(
					"Can't delete,due to pemissions assigned or it is assigned to users.But you can disable this Role");
		}
	}

	@Override
	public GenericResponseEntity createRole(Role role, Org org, User loginUser) throws UserAuthenticationException {

		validateRoleName(0, role.getName(), org);

		Role roleToSave = new Role();
		roleToSave.setName(role.getName());
//		roleToSave.setCode(role.getCode().toUpperCase());
		roleToSave.getPermissions().addAll(role.getPermissions());
		roleToSave.setEnabled(true);
//		roleToSave.setVisible(role.isVisible());
		roleToSave.setVisible(true);
		roleToSave.setOrg(org);
		roleToSave.setCreatedDate(new Date());
		roleToSave.setCreatedBy(loginUser);
		roleRepo.save(roleToSave);
		return new GenericResponseEntity(201, "Role created successfully");
	}

	@Override
	public GenericResponseEntity createPermission(Permission permission) throws UserAuthenticationException {

		if (StringUtils.isEmpty(permission.getLabel()))
			throw new UserAuthenticationException("Please fill up the permission label");

		permission.setCode("AUT" + generate4DigitNumber());
		permissionRepo.save(permission);
		return new GenericResponseEntity(201, "Permission created Successfully");
	}

	private int generate4DigitNumber() {
		SecureRandom sr = new SecureRandom();
		int token = sr.nextInt(10000);

		while (token < 1000 || token > 10000) {
			token = sr.nextInt(10000);
		}
		return token;
	}

	@Override
	public long isRoleCodeExist(String code) {
		return roleRepo.countByCodeIgnoreCase(code);
	}

	@Override
	public GenericResponseEntity isDeActivateRole(Long id, Boolean isActive) {
		Role roleDB = roleRepo.findById(id).get();

		if (roleDB != null) {
			roleDB.setEnabled(isActive);
			roleDB = roleRepo.save(roleDB);
			if (roleDB.isEnabled() == false) {
				return new GenericResponseEntity(202, "Role deactivated Successfully");
			} else {
				return new GenericResponseEntity(202, "Role activated Successfully");
			}
		}
		return new GenericResponseEntity(202, "Role Updated Successfully");
	}
}
