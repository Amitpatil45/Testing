package com.root32.userservice.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.root32.dto.OtpDto;
import com.root32.dto.UserDto;
import com.root32.dto.UserPrincipal;
import com.root32.entity.Org;
import com.root32.entity.Permission;
import com.root32.entity.PermissionCategory;
import com.root32.entity.Role;
import com.root32.entity.User;
import com.root32.userservice.dto.LoginResponse;
import com.root32.userservice.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	/* Pre login API for user creation */

	@PostMapping("/addUser")
	public ResponseEntity<GenericResponseEntity> create(@RequestBody User user, HttpServletRequest request) {
		User loginUser = (User) request.getAttribute(User.LOGIN_USER);
		Org org = (Org) request.getAttribute(User.USER_ORG);
		GenericResponseEntity gre = userService.create(user, loginUser, org);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@PostMapping("/user-session")
	@Transactional
	public ResponseEntity<LoginResponse> createUserSession(@Valid @RequestBody UserPrincipal userPrincipal,
			HttpServletRequest request) {

		String remoteAddress = request.getRemoteAddr();
		HashMap<String, Object> requestHeaders = extractHeaders(request);
		LoginResponse lr = userService.createUserSession(userPrincipal, remoteAddress, requestHeaders);
		return new ResponseEntity<>(lr, HttpStatus.CREATED);
	}

	private HashMap<String, Object> extractHeaders(HttpServletRequest request) {
		HashMap<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> headerNamesEnum = request.getHeaderNames();
		while (headerNamesEnum.hasMoreElements()) {
			String key = headerNamesEnum.nextElement();
			headers.put(key, request.getHeader(key));
		}

		return headers;
	}

	@PostMapping("/user/otp")
	@Transactional
	public ResponseEntity<GenericResponseEntity> createOtp(@RequestBody OtpDto otpDto, HttpServletRequest request) {
		String remoteAddress = request.getRemoteAddr();
		HashMap<String, Object> requestHeaders = extractHeaders(request);
		GenericResponseEntity gre = userService.createOtp(otpDto, remoteAddress, requestHeaders);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@PostMapping("/user/otp/verify")
	@Transactional
	public ResponseEntity<GenericResponseEntity> validateOtp(@RequestBody OtpDto otpDto) {
		GenericResponseEntity gre = userService.validateOtp(otpDto);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PutMapping("/reset-password")
	@Transactional
	public ResponseEntity<GenericResponseEntity> update(HttpServletRequest request, @RequestBody OtpDto otpDto) {
		GenericResponseEntity gre = userService.update(otpDto);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@GetMapping("users")
	@Transactional
	public Page<UserDto> getAllUsers(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		User loginUser = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		return userService.getAllUsers(page, size, userOrg, loginUser);
	}

	@GetMapping("/user/{id}")
	public User fetchUserById(@PathVariable() Long id) {
		return userService.fetchUserById(id);
	}

	@PutMapping("/user-password")
	@Transactional
	public ResponseEntity<GenericResponseEntity> updatePassword(HttpServletRequest request,
			@RequestBody UserPrincipal userPrincipal) {
		Object loggedInUserObj = request.getAttribute(User.LOGIN_USER);
		User loggedUser = loggedInUserObj == null ? null : (User) loggedInUserObj;
		GenericResponseEntity gre = userService.updatePassword(userPrincipal, loggedUser);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PostMapping("/userAuthentication/otp")
	public ResponseEntity<GenericResponseEntity> createAuthenticationOtp(@RequestBody String contactNumber,
			HttpServletRequest request) {
		String remoteAddress = request.getRemoteAddr();
		HashMap<String, Object> requestHeaders = extractHeaders(request);
		GenericResponseEntity gre = userService.createAuthenticationOtp(contactNumber, remoteAddress, requestHeaders);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@PostMapping("/userAuthentication/otp/{contactNumber}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> validateAuthenticationOtp(@PathVariable String contactNumber,
			@RequestBody String otp) {
		GenericResponseEntity gre = userService.validateAuthenticationOtp(contactNumber, otp);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PutMapping("/updateUserProfile")
	@Transactional
	public ResponseEntity<GenericResponseEntity> updateUserProfile(HttpServletRequest request, @RequestBody User user) {
		Object loggedInUserObj = request.getAttribute(User.LOGIN_USER);
		User loggedUser = loggedInUserObj == null ? null : (User) loggedInUserObj;
		GenericResponseEntity gre = userService.updateUserProfile(user, loggedUser);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PutMapping("user/isActivate/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> allowUserOrNot(@PathVariable Long id, @RequestParam Boolean isActive) {
		GenericResponseEntity gre = userService.allowUserOrNot(id, isActive);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteUser/{id}")
	@Transactional
	public ResponseEntity<GenericResponseEntity> deleteUser(@PathVariable Long id) {
		GenericResponseEntity gre = userService.deleteUser(id);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	// -----------------------Role API ---------------------------------------------
	@GetMapping("/all-roles")
	public Page<Role> fetchAllRoles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, HttpServletRequest request,
			@RequestParam(required = false) String name

	) {
		User loginUser = (User) request.getAttribute(User.LOGIN_USER);
		Org loginOrg = (Org) request.getAttribute(User.USER_ORG);
		return userService.fetchAllRoles(page, size, loginUser, loginOrg, name);
	}

	@GetMapping("/roles")
	public List<Role> getAllRoles(HttpServletRequest request, @RequestParam(required = false) String name) {
		Org userOrg = (Org) request.getAttribute(User.USER_ORG);
		return userService.getAllRoles(userOrg, name);
	}

	@PutMapping("/role")
	public ResponseEntity<GenericResponseEntity> updateRole(@RequestBody Role role,HttpServletRequest request) {
		
		Org userOrg = (Org) request.getAttribute(User.USER_ORG);
		User loginUser = (User) request.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = userService.updateRole(role,userOrg,loginUser);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@GetMapping("/role/{id}")
	public Role getRoleById(@PathVariable Long id) {
		return userService.getRoleById(id);
	}

	@DeleteMapping("/role/{id}")
	public ResponseEntity<GenericResponseEntity> deleteRole(@PathVariable Long id) {
		GenericResponseEntity gre = userService.deleteRole(id);
		return new ResponseEntity<>(gre, HttpStatus.OK);
	}

	@PostMapping("/role")
	public ResponseEntity<GenericResponseEntity> createRole(@RequestBody Role role, HttpServletRequest request) {
		Org org = (Org) request.getAttribute(User.USER_ORG);
		User loginUser = (User) request.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = userService.createRole(role, org,loginUser);

		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/is-rolecode-exist/{code}")
	public boolean isRoleCodeExist(@PathVariable String code) {

		long userAdmin = userService.isRoleCodeExist(code);

		if (userAdmin == 0) {
			return false;
		}
		return true;
	}

	@PutMapping("role/isDeActivate/{id}")
	public ResponseEntity<GenericResponseEntity> isDeActivateRole(@PathVariable Long id,
			@RequestParam Boolean isActive) {
		GenericResponseEntity gre = userService.isDeActivateRole(id, isActive);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

//-----------------------Permission API ---------------------------------------------
	@GetMapping("/all-permissions")
	public Page<Permission> fetchAllPermissions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return userService.fetchAllPermissions(page, size);
	}

	@GetMapping("/permissions")
	public List<Permission> getAllPermissions(HttpServletRequest request) {
		User loginUser = (User) request.getAttribute(User.LOGIN_USER);
		return userService.getAllPermissions(loginUser);
	}

	@GetMapping("/permissionCategories")
	public List<PermissionCategory> getAllPermissionCategories() {
		return userService.getAllPermissionCategories();
	}

	@GetMapping("/permission/{id}")
	public Permission getPermissionById(@PathVariable Long id) {
		return userService.getPermissionById(id);
	}

	@PutMapping("/permission")
	public ResponseEntity<GenericResponseEntity> updatePermission(@RequestBody Permission permission) {
		GenericResponseEntity gre = userService.updatePermission(permission);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@PostMapping("/permission")
	public ResponseEntity<GenericResponseEntity> createPermission(@RequestBody Permission permission) {
		GenericResponseEntity gre = userService.createPermission(permission);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

}
