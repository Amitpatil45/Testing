package com.root32.onboarding.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.root32.dto.GenericResponseEntity;
import com.root32.entity.Org;
import com.root32.entity.Retailer;
import com.root32.entity.SubRetailer;
import com.root32.entity.User;

public interface OnboardingService {

	GenericResponseEntity createRetailer(Retailer retailer, User loginUser);

	Page<Retailer> findAllRetailers(int page, int size, Org userOrg);

	Retailer getRetailerById(Long id);

	GenericResponseEntity updateRetailer(Long id, Retailer retailer, User user);

	GenericResponseEntity deleteOrg(Long id);

	GenericResponseEntity allowOrgOrNot(Long id, Boolean isActive,User user);

	List<Retailer> allRetailerList();

	GenericResponseEntity createSubRetailer(SubRetailer subRetailer, User loginUser);

	Page<SubRetailer> findAllSubRetailers(int page, int size, Org userOrg);

	SubRetailer getSubRetailerById(Long id);

	List<SubRetailer> allSubRetailerList(Org org);

	GenericResponseEntity updateSubRetailer(Long id, SubRetailer subRetailer, User user);


}
