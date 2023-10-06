package com.root32.userservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.root32.dto.GenericResponseEntity;
import com.root32.dto.PlatformParamTypeDto;
import com.root32.entity.PlatformParam;
import com.root32.entity.User;

public interface PlatformParamService {

	GenericResponseEntity addPlatformParam(PlatformParam platformParam, User user);

	Page<PlatformParamTypeDto> getAllPlatformParam(int page, int size);

	PlatformParam fetchPlatformParamById(Long id);

	GenericResponseEntity deletePlatformParamById(Long id);

	GenericResponseEntity updatePlatformParam(Long id, PlatformParam platformParam, User updatedBy);

    List<PlatformParam> fetchPlatformParamByType(String type);

	Map<String, List<PlatformParam>> findAllGroupedByType();

    GenericResponseEntity updatePlatformParamsByType(String type, List<PlatformParam> updatedParams, User updatedBy);

}
