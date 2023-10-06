package com.root32.userservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysql.cj.util.StringUtils;
import com.root32.configsvc.repository.PlatformParamRepository;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.PdfAttachment;
import com.root32.dto.PlatformParamTypeDto;
import com.root32.entity.PlatformParam;
import com.root32.entity.User;
import com.root32.userservice.exception.DataValidationException;
import com.root32.userservice.service.PlatformParamService;

@Service
public class PlatformParamServiceImpl implements PlatformParamService {
	@Autowired
	private PlatformParamRepository platformParamRepository;

	@Override
	public GenericResponseEntity addPlatformParam(PlatformParam platformParam, User user) {
		// validateDuplicateRecords(platformParam);
		platformParam.setCreatedBy(user);
		platformParam.setCreatedDate(new Date());
		platformParamRepository.save(platformParam);
		return new GenericResponseEntity(201, "Platform param saved successfully");
	}

	@Override
	public Page<PlatformParamTypeDto> getAllPlatformParam(int page, int size) {
		// Pageable pageable = buildPagable(page, size);
		// Page<PlatformParamTypeDto> platformParam =
		// platformParamRepository.findAllGroupedByType(pageable);
		// return platformParam;

		List<PlatformParam> allParams = platformParamRepository.findAll();
		List<PlatformParamTypeDto> groupedParams = new ArrayList<>();
		for (PlatformParam param : allParams) {
			PlatformParamTypeDto AddedPlatformParamTypeDto = new PlatformParamTypeDto();
			AddedPlatformParamTypeDto.setType(param.getType());
			AddedPlatformParamTypeDto.setUpdatedDate(param.getUpdatedDate());
			groupedParams.add(AddedPlatformParamTypeDto);
		}

		Set<PlatformParamTypeDto> uniqueObjects = new HashSet<>();
		Set<String> uniqueString = new HashSet<>();

		for (PlatformParamTypeDto obj : groupedParams) {
			if (!uniqueString.contains(obj.getType())) {
				uniqueObjects.add(obj);
				uniqueString.add(obj.getType());
			}
		}

		List<PlatformParamTypeDto> uniqueObjectList = new ArrayList<>(uniqueObjects);
		// pagination
		Pageable pageRequest = createPageRequestUsing(page, size);

		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), uniqueObjectList.size());

		List<PlatformParamTypeDto> pageContent = uniqueObjectList.subList(start, end);

		return new PageImpl<>(pageContent, pageRequest, uniqueObjectList.size());

	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
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
	public PlatformParam fetchPlatformParamById(Long id) {
		return getPlatformParamById(id);
	}

	private PlatformParam getPlatformParamById(Long id) {
		Optional<PlatformParam> platformParam = platformParamRepository.findById(id);
		if (!platformParam.isPresent()) {
			throw new DataValidationException("Platform param not found!");
		}
		return platformParam.get();
	}

	@Override
	public GenericResponseEntity deletePlatformParamById(Long id) {
		Optional<PlatformParam> platformParam = platformParamRepository.findById(id);
		if (!platformParam.isPresent()) {
			throw new DataValidationException("Platform param is not present");
		}
		platformParamRepository.deleteById(id);
		return new GenericResponseEntity(201, "Platform param deleted successfully!");
	}

	@Override
	public GenericResponseEntity updatePlatformParam(Long id, PlatformParam platformParam, User updatedBy) {
		Optional<PlatformParam> platformParamOptional = platformParamRepository.findById(id);
		if (!platformParamOptional.isPresent()) {
			throw new DataValidationException("Platform param is not present");
		}

		PlatformParam platformParamDB = platformParamRepository.findById(id).get();
		platformParamDB.setUpdatedDate(new Date());
		platformParamDB.setUpdatedBy(updatedBy);
		platformParamDB.setParamValue(platformParam.getParamValue());
		platformParamDB.setName(platformParamDB.getName());
		platformParamDB.setType(platformParamDB.getType());
		platformParamDB.setActive(true);
		platformParamRepository.save(platformParamDB);
		return new GenericResponseEntity(201, "Platform param updated successfully");
	}

	@Override
	public List<PlatformParam> fetchPlatformParamByType(String type) {
		List<PlatformParam> platformParam = platformParamRepository.findByType(type);
		return platformParam;
	}

	public Map<String, List<PlatformParam>> findAllGroupedByType() {

		List<PlatformParam> allParams = platformParamRepository.findAllByOrderByType();

		Map<String, List<PlatformParam>> groupedParams = new HashMap<>();

		for (PlatformParam param : allParams) {
			String type = param.getType();
			groupedParams.computeIfAbsent(type, k -> new ArrayList<>()).add(param);
		}
		return groupedParams;
	}

	@Override
	public GenericResponseEntity updatePlatformParamsByType(String type, List<PlatformParam> updatedParams,
			User updatedBy) {
		List<PlatformParam> allKeyvalue = platformParamRepository.findByType(type);
		if (allKeyvalue.size() == 0) {
			throw new DataValidationException("This type of Integration not found.");
		}
		if (updatedParams.size() != allKeyvalue.size()) {
			throw new DataValidationException("required keys not match.");
		}
		for (PlatformParam platformParam2 : updatedParams) {

			if (platformParam2.getParamValue() == null
					|| StringUtils.isEmptyOrWhitespaceOnly(platformParam2.getParamValue())) {
				throw new DataValidationException("Param value is required.");
			}
			PlatformParam platformParamDB = platformParamRepository.findByParamKey(platformParam2.getParamKey());
			if (platformParamDB.getParamKey() == null) {
				throw new DataValidationException("This key param not match.");
			}
			platformParamDB.setUpdatedBy(updatedBy);
			platformParamDB.setParamValue(platformParam2.getParamValue());
			platformParamDB.setName(platformParamDB.getName());
			platformParamDB.setType(platformParamDB.getType());
			platformParamDB.setActive(true);
			platformParamDB.setUpdatedDate(new Date());
			platformParamRepository.save(platformParamDB);
		}
		return new GenericResponseEntity(202, "Platform param updated successfully");
	}

}
