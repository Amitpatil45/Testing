package com.root32.userservice.dayzero;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.root32.configsvc.repository.PlatformParamRepository;
import com.root32.entity.MessageTemplate;
import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;
import com.root32.entity.Parent;
import com.root32.entity.Permission;
import com.root32.entity.PermissionCategory;
import com.root32.entity.PlatformParam;
import com.root32.entity.Retailer;
import com.root32.entity.Role;
import com.root32.entity.SubRetailer;
import com.root32.entity.User;
import com.root32.userservice.repository.MessageTemplateRepository;
import com.root32.userservice.repository.OnboardingRepository;
import com.root32.userservice.repository.OrgRepository;
import com.root32.userservice.repository.PermissionCategoryRepository;
import com.root32.userservice.repository.PermissionRepository;
import com.root32.userservice.repository.RoleRepository;
import com.root32.userservice.repository.SubRetailerRepository;
import com.root32.userservice.repository.UserRepository;

@Component
public class DayZeroScript {

	@Autowired
	private OrgRepository orgRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MessageTemplateRepository messageTemplateRepository;

	@Autowired
	private OnboardingRepository onboardingRepository;

	@Autowired
	private SubRetailerRepository subRetailerRepository;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PermissionRepository permissionRepo;

	@Autowired
	private PermissionCategoryRepository permissionCategoryRepo;

	@Autowired
	private PlatformParamRepository platformParamRepository;

	@PostConstruct
	public void init() {

		createPermissionIfNotFound();
		createRolesIfNotFound();
		Parent org = createAdminIfNotFound("Root32");
		User user = new User();
		user.setOrg(org);
		user.setEmailId("gaurav.joshi@amelioratesolutions.com");
		Role adminRole = (Role) roleRepo.findByCode("ADM");
		user = createAdminUserIfNotFound(user, adminRole);

		/* create a retailer and its user */
		Retailer retailer = createSampleRetailer(org);

		User retailerUser = new User();
		retailerUser.setOrg(retailer);
		retailerUser.setEmailId("nikhil.ranbhare@amelioratesolutions.com");
		Role retailerRole = (Role) roleRepo.findByCode("RET");
		if (retailerRole.getOrg() == null) {
			if (org != null) {
				retailerRole.setOrg(org);
				retailerRole = roleRepo.save(retailerRole);
			} else {
				Org adminOrg = orgRepo.findByOrgCode("OID-ROOT32");
				retailerRole.setOrg(adminOrg);
				retailerRole = roleRepo.save(retailerRole);
			}
		}
		retailerUser = createRetailerUser(retailerUser, retailerRole);

		/* create a sub-retailer and its user */
		SubRetailer subRetailer = createSampleSubRetailer(retailer);

		User subRetailerUser = new User();
		subRetailerUser.setOrg(subRetailer);
		subRetailerUser.setEmailId("badal.kumar@amelioratesolutions.com");
		Role subRetailerRole = (Role) roleRepo.findByCode("SRT");
		if (subRetailerRole.getOrg() == null) {
			if (org != null) {
				subRetailerRole.setOrg(retailer);
				subRetailerRole = roleRepo.save(subRetailerRole);

			} else {
				Org adminOrg = orgRepo.findByOrgCode("OID-RETAILER");
				subRetailerRole.setOrg(adminOrg);
				subRetailerRole = roleRepo.save(subRetailerRole);
			}
		}
		subRetailerUser = createSubRetailerUser(subRetailerUser, subRetailerRole);

		assignRoleToDefaultOrgUser(adminRole, retailerRole, subRetailerRole);
		createMessageTemplateIfNotFound();
		intigrationSetting();


	}

	private Parent createAdminIfNotFound(String name) {
		Parent parent = null;
		Long orgCount = orgRepo.countByBusinessNameContainingIgnoreCase(name);
		if (orgCount == 0) {
			parent = new Parent();
			parent.setBusinessName(name);
			parent.setPocFirstName("Gaurav");
			parent.setPocLastName("Joshi");
			parent.setEmailId("gaurav.joshi@amelioratesolutions.com");
			parent.setMobileNumber("9049745973");
			parent.setIsActive(true);
			parent.setOrgTypeEnum(OrgTypeEnum.ADMIN);
			parent.setOrgCode("OID-ROOT32");
			parent.setIsHavingRecord(true);
			parent = orgRepo.save(parent);
		}
		return parent;
	}

	private User createAdminUserIfNotFound(User user, Role role) {

		User dbUser = userRepository.findByEmailId(user.getEmailId());
		if (dbUser == null) {
			user.setPassword("Root@123");
			user.setFirstName("Gaurav");
			user.setLastName("Joshi");
			user.setContactNumber("9049745973");
			user.setCreatedDate(new Date());
			user.setIsLocked(false);
			user.setUnsuccessfulAttemps(0);
			user.setIsActive(true);
			updateAuditFields(user);
			encodePassword(user);
			user.setIsHavingAnyRecords(false);
			user.setIsPasswordUpdate(true);
			user.setIsOrgAdminUser(true);
			user.getRoles().add(role);
			user = userRepository.saveAndFlush(user);
			return user;
		}
		return dbUser;
	}

	private Retailer createSampleRetailer(Org adminOrg) {

		Retailer retailerOrg = null;
		Long orgCount = onboardingRepository.countByBusinessNameContainingIgnoreCase("Retailer firm");

		if (orgCount == 0) {
			retailerOrg = new Retailer();
			retailerOrg.setBusinessName("Retailer firm");
			retailerOrg.setPocFirstName("Nikhil");
			retailerOrg.setPocLastName("Ranbhare");
			retailerOrg.setEmailId("nikhil.ranbhare@amelioratesolutions.com");
			retailerOrg.setMobileNumber("9049043211");
			retailerOrg.setIsActive(true);
			retailerOrg.setOrgTypeEnum(OrgTypeEnum.RETAILER);
			retailerOrg.setParent(adminOrg);
			retailerOrg.setOrgCode("OID-RETAILER");
			retailerOrg = orgRepo.save(retailerOrg);
		}
		return retailerOrg;

	}

	private User createRetailerUser(User retailer, Role retailerRole) {
		User dbUser = userRepository.findByEmailId(retailer.getEmailId());
		if (dbUser == null) {
			retailer.setPassword("Root@123");
			retailer.setFirstName("Nikhil");
			retailer.setLastName("Ranbhare");
			retailer.setContactNumber("9049043211");
			retailer.setCreatedDate(new Date());
			retailer.setIsLocked(false);
			retailer.setUnsuccessfulAttemps(0);
			retailer.setIsActive(true);
			updateAuditFields(retailer);
			encodePassword(retailer);
			retailer.setIsHavingAnyRecords(false);
			retailer.setIsPasswordUpdate(true);
			retailer.setIsOrgAdminUser(true);
			retailer.getRoles().add(retailerRole);
			retailer = userRepository.saveAndFlush(retailer);
			return retailer;
		}
		return dbUser;

	}

	private SubRetailer createSampleSubRetailer(Org retailerOrg) {

		SubRetailer subRetailerOrg = null;
		Long orgCount = subRetailerRepository.countByBusinessNameContainingIgnoreCase("Sub-retailer firm");

		if (orgCount == 0) {
			subRetailerOrg = new SubRetailer();
			subRetailerOrg.setBusinessName("Sub-retailer firm");
			subRetailerOrg.setPocFirstName("Badal");
			subRetailerOrg.setPocLastName("Kumar");
			subRetailerOrg.setEmailId("badal.kumar@amelioratesolutions.com");
			subRetailerOrg.setMobileNumber("7620070629");
			subRetailerOrg.setIsActive(true);
			subRetailerOrg.setOrgTypeEnum(OrgTypeEnum.SUB_RETAILER);
			subRetailerOrg.setParent(retailerOrg);
			subRetailerOrg.setOrgCode("OID-SUBRETAILER");
			subRetailerOrg = orgRepo.save(subRetailerOrg);
		}
		return subRetailerOrg;

	}

	private User createSubRetailerUser(User subRetailer, Role subRetailerRole) {
		User dbUser = userRepository.findByEmailId(subRetailer.getEmailId());
		if (dbUser == null) {
			subRetailer.setPassword("Root@123");
			subRetailer.setFirstName("Badal");
			subRetailer.setLastName("Kumar");
			subRetailer.setContactNumber("7620070629");
			subRetailer.setCreatedDate(new Date());
			subRetailer.setIsLocked(false);
			subRetailer.setUnsuccessfulAttemps(0);
			subRetailer.setIsActive(true);
			updateAuditFields(subRetailer);
			encodePassword(subRetailer);
			subRetailer.setIsHavingAnyRecords(false);
			subRetailer.setIsPasswordUpdate(true);
			subRetailer.setIsOrgAdminUser(true);
			subRetailer.getRoles().add(subRetailerRole);
			subRetailer = userRepository.saveAndFlush(subRetailer);
			return subRetailer;
		}
		return dbUser;

	}

	private void updateAuditFields(User user) {

		user.setCreatedDate(new Date());
		int expiryDays = getPasswordExpiryDays();
		Date expiryDate = calcualteExpiryDate(Calendar.DATE, expiryDays);
		user.setExpiryDate(expiryDate);
		user.setIsLocked(false);
	}

	private int getPasswordExpiryDays() {
		// CompanyConfig companyConfig = configService.getCompanyConfig();
		// int passwordExpiryDays = companyConfig.getPasswordExpiryDays();
		int passwordExpiryDays = 365;
		passwordExpiryDays = passwordExpiryDays == 0 ? 60 : passwordExpiryDays;
		return passwordExpiryDays;
	}

	private Date calcualteExpiryDate(int field, int amount) {
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(field, amount);
		currentDate = c.getTime();
		return currentDate;
	}

	private void encodePassword(User user) {
		final String plainPassword = user.getPassword();
		user.setPassword(new BCryptPasswordEncoder(12).encode(plainPassword));
	}

	private void createMessageTemplateIfNotFound() {

		Long count = messageTemplateRepository.count();
		if (count == 0) {

			// String userRegistrationOtp = "This is system generated message for user
			// authentication. Your One Time Password is <b>%s</b>";

			String ForgotPaswordOtpTemplate = "This is system generated message. Your One Time Password is <b>%s</b>";

			String onboardingTemplate = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n"
					+ "<title>Welcome to Root32 Platform</title>\n" + "</head>\n" + "<body>\n"
					+ "<h3 align=\"center\">Welcome to Root32 Platform</h3><hr>\n"
					+ "<p>You are onboarded as a retailer on Root32 platform also you are registered as a user to Root32 Platform. Kindly use below login credentials to access Root32 platform</p>\n"
					+ "<p>Email : %s <br>Mobile No. %s <br> Password : %s  \n"
					+ "<p>For any query kindly contact to Admin</p><br>Admin name :%s <br>Admin's contact number :%s <br>Admin's email :%s</p>\n"
					+ "<p>Thanks,<br>The Root32 account team\n" + "</p>\n" + "</body>\n" + "</html>";

			String purchaseOrderTemplate = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<meta charset=\"UTF-8\">\n"
					+ "<title>Purchase Order for products </title>\n" + "</head>\n" + "<body>\n"
					+ "<h3 align=\"center\">Purchase Order for products</h3><hr>\n"
					+ "<p>New purchase order has been created. Kindly check attachment pdf of barcode and provide the list of product mentioned in it. \n"
					+ "<p>Thanks,<br>The Root-32 account team\n" + "</p>	\n" + "</body>\n" + "</html>";

			List<MessageTemplate> allMessageTemplate = Arrays.asList(
					new MessageTemplate("FORGOT_PASSWORD_OTP", ForgotPaswordOtpTemplate, "1001", getCreatedDate()),
					// new MessageTemplate("USER_AUTHENTICATION_OTP", userRegistrationOtp, "1002",
					// getCreatedDate()),
					new MessageTemplate("ONBOARDING", onboardingTemplate, "1003", getCreatedDate()),
					new MessageTemplate("PURCHASE_ORDER", purchaseOrderTemplate, "1004", getCreatedDate()));

			messageTemplateRepository.saveAll(allMessageTemplate);

		}
	}

	private Date getCreatedDate() {
		Date createdDate = new Date();
		return createdDate;
	}

	private void createPermissionIfNotFound() {
		if (permissionCategoryRepo.count() == 0) {
			PermissionCategory dashboard = permissionCategoryRepo.save(new PermissionCategory("DSB", "Dashboard"));
			PermissionCategory onboarding = permissionCategoryRepo.save(new PermissionCategory("ONB", "Onboarding"));
			PermissionCategory master = permissionCategoryRepo.save(new PermissionCategory("MST", "Master table"));
			PermissionCategory inventory = permissionCategoryRepo.save(new PermissionCategory("INV", "Inventory"));
			PermissionCategory userManagement = permissionCategoryRepo
					.save(new PermissionCategory("USM", "User Management"));
			PermissionCategory orderManagement = permissionCategoryRepo
					.save(new PermissionCategory("ODM", "Order Management"));
			PermissionCategory transaction = permissionCategoryRepo.save(new PermissionCategory("TRN", "Transaction"));
			PermissionCategory template = permissionCategoryRepo.save(new PermissionCategory("TMP", "Template"));

			if (permissionRepo.count() == 0) {
				List<Permission> permissions = Arrays.asList(
						// --------------------------------User management
						// pemissions--------------------------------------------
						new Permission("UMC0001", "Users List", userManagement),
						new Permission("UMC0002", "Create User", userManagement),
						new Permission("UMC0004", "Update User", userManagement),
						new Permission("UMC0005", "View User", userManagement),
						new Permission("PRC0001", "Update Password", userManagement),
						new Permission("UMC0006", "Get Roles", userManagement),
						new Permission("UMC0017", "Create Role", userManagement),
						new Permission("UMC0013", "Update Role", userManagement),
						new Permission("UMC0012", "View Role", userManagement),
						new Permission("UMC0015", "Delete Role", userManagement),
						// --------------------------------Message template
						// pemissions--------------------------------------------
						new Permission("TMP0001", "View Message Template", template),
						new Permission("TMP0002", "Message Template List", template),
						new Permission("TMP0003", "Update message Template", template),
						// --------------------------------Retailer
						// pemissions--------------------------------------------
						new Permission("RET0001", "Save Retailer", onboarding),
						new Permission("RET0002", "Retailers List", onboarding),
						new Permission("RET0003", "View Retailer", onboarding),
						new Permission("RET0004", "Update Retailer", onboarding),
						new Permission("ORG0001", "Delete Organization", onboarding),
						new Permission("ORG0002", "Activate/deactivate Organization", onboarding),
						// --------------------------------Product master
						// pemissions--------------------------------------------
						new Permission("PRD0001", "Save Product", master),
						new Permission("PRD0002", "View Product", master),
						new Permission("PRD0003", "Products List", master),
						new Permission("PRD0004", "Update Product", master),
						new Permission("PRD0005", "Change Product Status", master),
						new Permission("PRD0006", "Delete Product", master),
						// --------------------------------category master
						// pemissions--------------------------------------------
						new Permission("CAT0001", "Save Category", master),
						new Permission("CAT0002", "Update Category", master),
						new Permission("CAT0003", "View Category", master),
						new Permission("CAT0004", "Change Category Status", master),
						new Permission("CAT0005", "Delete Category", master),
						new Permission("CAT0006", "Category List", master),
						// --------------------------------UOM master
						// pemissions--------------------------------------------
						new Permission("UOM0001", "Save Uom", master), new Permission("UOM0002", "Update Uom", master),
						new Permission("UOM0003", "View Uom", master), new Permission("UOM0004", "Delete Uom", master),
						new Permission("UOM0005", "Uom List", master),
						// --------------------------------Manufacturer master
						// pemissions--------------------------------------------
						new Permission("MAN0001", "Save Manufacturer", master),
						new Permission("MAN0002", "Update Manufacturer", master),
						new Permission("MAN0003", "View Manufacturer", master),
						new Permission("MAN0004", "Manufacturer List", master),
						// --------------------------------Purchase order pemissions
						// --------------------------------------------

						new Permission("PRO0001", "Create Purchase Order", inventory),
						new Permission("PRO0002", "Update Purchase Order ", inventory),
						new Permission("PRO0003", "View Purchase Order ", inventory),
						new Permission("PRO0004", "Purchase Order List", inventory),
						// --------------------------------Product Inward( for Admin inventory )
						// pemissions --------------------------------------------
						new Permission("INW0001", "product Inward(Scanning)", inventory),
						new Permission("ADS0001", "Admin Stock List", inventory),

						// --------------------------------Retailer related pemissions
						// --------------------------------------------
						new Permission("SRT0001", "Save Sub-Retailer", onboarding),
						new Permission("SRT0002", "Update Sub-Retailer", onboarding),
						new Permission("SRT0003", "Sub-Retailer List", onboarding),
						new Permission("SRT0004", "View Sub-Retailer", onboarding)

				);
				permissionRepo.saveAll(permissions);
			}

		}
	}

	private void createRolesIfNotFound() {
		if (roleRepo.count() == 0) {
			List<Permission> allPermissions = permissionRepo.findAll();

			// --------------------------Role-(Root 32 Admin)'s
			// permission--------------------------------
			Set<Permission> adminPermissions = new HashSet<>();
			adminPermissions.addAll(allPermissions.stream()
					.filter(u -> u.getCode().equalsIgnoreCase("RET0001") || u.getCode().equalsIgnoreCase("RET0002")
							|| u.getCode().equalsIgnoreCase("RET0003") || u.getCode().equalsIgnoreCase("RET0004")
							|| u.getCode().equalsIgnoreCase("ORG0001") || u.getCode().equalsIgnoreCase("ORG0002")
							|| u.getCode().equalsIgnoreCase("UMC0001") || u.getCode().equalsIgnoreCase("UMC0002")
							|| u.getCode().equalsIgnoreCase("UMC0004") || u.getCode().equalsIgnoreCase("UMC0005")
							|| u.getCode().equalsIgnoreCase("PRC0001") || u.getCode().equalsIgnoreCase("UMC0006")
							|| u.getCode().equalsIgnoreCase("UMC0017") || u.getCode().equalsIgnoreCase("UMC0013")
							|| u.getCode().equalsIgnoreCase("UMC0012") || u.getCode().equalsIgnoreCase("UMC0015")
							|| u.getCode().equalsIgnoreCase("TMP0001") || u.getCode().equalsIgnoreCase("TMP0002")
							|| u.getCode().equalsIgnoreCase("TMP0003")
							|| u.getPermissionCategory().getCode().equals("MST")
							|| u.getPermissionCategory().getCode().equals("DSB")
							|| u.getCode().equalsIgnoreCase("PRO0001") || u.getCode().equalsIgnoreCase("PRO0002")
							|| u.getCode().equalsIgnoreCase("PRO0003") || u.getCode().equalsIgnoreCase("PRO0004")
							|| u.getCode().equalsIgnoreCase("INW0001") || u.getCode().equalsIgnoreCase("ADS0001"))
					.collect(Collectors.toSet()));

			// --------------------------Role-(Retailer)'s
			// permission-----------------------------------------------

			Set<Permission> retailerPermissions = new HashSet<>();
			retailerPermissions.addAll(allPermissions.stream()
					.filter(u -> u.getPermissionCategory().getCode().equals("USM")
							|| u.getCode().equalsIgnoreCase("SRT0001") || u.getCode().equalsIgnoreCase("SRT0002")
							|| u.getCode().equalsIgnoreCase("SRT0003") || u.getCode().equalsIgnoreCase("SRT0004")
							|| u.getCode().equalsIgnoreCase("ORG0001") || u.getCode().equalsIgnoreCase("ORG0002"))
					.collect(Collectors.toSet()));

			// --------------------------Role-(Retailer)'s
			// permission-----------------------------------------------

			Set<Permission> subRetailerPermissions = new HashSet<>();
			subRetailerPermissions.addAll(allPermissions.stream()
					.filter(u -> u.getPermissionCategory().getCode().equals("USM")).collect(Collectors.toSet()));
			// -----------------------------------------------------------------------------------------

			List<Role> roles = Arrays.asList(new Role("ADM", "Root-32 Admin", adminPermissions, false),
					new Role("RET", "Retailer Admin", retailerPermissions, true),
					new Role("SRT", "Sub-Retailer Admin", subRetailerPermissions, true)

			);
			roleRepo.saveAll(roles);
		}
	}

	private void assignRoleToDefaultOrgUser(Role adminRole, Role retailerRole, Role subRetailerRole) {

		User adminUser = userRepository.findByEmailIdAndOrg("gaurav.joshi@amelioratesolutions.com",
				orgRepo.findByOrgCode("OID-ROOT32"));

		User reatailerUser = userRepository.findByEmailIdAndOrg("nikhil.ranbhare@amelioratesolutions.com",
				orgRepo.findByOrgCode("OID-RETAILER"));

		User subReatailerUser = userRepository.findByEmailIdAndOrg("badal.kumar@amelioratesolutions.com",
				orgRepo.findByOrgCode("OID-SUBRETAILER"));

		if (userRepository.getCountOfUserRoles(adminUser.getId()) == 0) {
			adminUser.addRoles(adminRole);
			userRepository.save(adminUser);
		}
		if (userRepository.getCountOfUserRoles(reatailerUser.getId()) == 0l) {
			reatailerUser.addRoles(retailerRole);
			userRepository.save(reatailerUser);
		}
		if (userRepository.getCountOfUserRoles(subReatailerUser.getId()) == 0) {
			subReatailerUser.addRoles(subRetailerRole);
			userRepository.save(subReatailerUser);
		}

	}

	private void intigrationSetting() {
		long count = platformParamRepository.count();
		if (count == 0) {

			List<PlatformParam> platformParams = Arrays.asList(
					new PlatformParam("emailId", "amelioratesolutionsbs@gmail.com", "Official Email-id", "EMAILSEND",
							true, getCreatedDate()),
					new PlatformParam("AppPassword", "lbavfphxhzyyaist", "App Password", "EMAILSEND", true,
							getCreatedDate()));
			platformParamRepository.saveAll(platformParams);
		}
	}

}
