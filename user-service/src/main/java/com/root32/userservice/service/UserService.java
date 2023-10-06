package com.root32.userservice.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;

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

public interface UserService {

	GenericResponseEntity create(User user, User loginUser, Org org);

	LoginResponse createUserSession(UserPrincipal userPrincipal, String remoteAddress,
			HashMap<String, Object> requestHeaders);

	GenericResponseEntity createOtp(OtpDto otpDto, String remoteAddress, HashMap<String, Object> requestHeaders);

	GenericResponseEntity validateOtp(OtpDto otpDto);

	GenericResponseEntity update(OtpDto otpDto);

	Page<UserDto> getAllUsers(int page, int size, Org userOrg, User loginUser);

	User fetchUserById(Long id);

	GenericResponseEntity updatePassword(UserPrincipal userPrincipal, User loggedUser);

	GenericResponseEntity createAuthenticationOtp(String emailId, String remoteAddress,
			HashMap<String, Object> requestHeaders);

	GenericResponseEntity validateAuthenticationOtp(String emailId, String otp);

	GenericResponseEntity updateUserProfile(User user, User loggedUser);

	GenericResponseEntity allowUserOrNot(Long id, Boolean isActive);

	GenericResponseEntity deleteUser(Long id);

	Page<Role> fetchAllRoles(int page, int size, User loginUser, Org loginOrg, String name);

	List<Role> getAllRoles(Org userOrg, String name);

	Page<Permission> fetchAllPermissions(int page, int size);

	List<Permission> getAllPermissions(User loginUser);

	List<PermissionCategory> getAllPermissionCategories();

	Permission getPermissionById(Long id);

	Role getRoleById(Long id);

	GenericResponseEntity updateRole(Role role, Org userOrg, User loginUser);

	GenericResponseEntity updatePermission(Permission permission);

	GenericResponseEntity deleteRole(Long id);

	GenericResponseEntity createRole(Role role, Org org, User loginUser);

	GenericResponseEntity createPermission(Permission permission);

	long isRoleCodeExist(String code);

	GenericResponseEntity isDeActivateRole(Long id, Boolean isActive);

}
