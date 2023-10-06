package com.root32.userservice.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.root32.dto.PlatformParamTypeDto;
import com.root32.entity.PlatformParam;
import com.root32.entity.User;
import com.root32.userservice.service.PlatformParamService;

@RestController
@RequestMapping("/api/")
public class ParamController {

	private PlatformParamService platformParamService;

	public ParamController(PlatformParamService paramService) {
		this.platformParamService = paramService;
	}

	// no need
	@PostMapping("/platformParam")
	public ResponseEntity<GenericResponseEntity> addPlatformParam(HttpServletRequest httpServletRequest,
			@RequestBody PlatformParam platformParam) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = platformParamService.addPlatformParam(platformParam, user);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}


	@GetMapping("platformParam/{id}")
	public PlatformParam fetchPlatformParamById(@PathVariable("id") Long id) {
		return platformParamService.fetchPlatformParamById(id);
	}

	@DeleteMapping("platformParam/{id}")
	public ResponseEntity<GenericResponseEntity> deletePlatformParamById(@PathVariable("id") Long id) {
		GenericResponseEntity gre = platformParamService.deletePlatformParamById(id);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@PutMapping("platformParam/{id}")
	public ResponseEntity<GenericResponseEntity> updatePlatformParam(HttpServletRequest httpServletRequest,
			@PathVariable("id") Long id, @RequestBody PlatformParam platformParam) {
		User updatedBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = platformParamService.updatePlatformParam(id, platformParam, updatedBy);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	// type wise
	@GetMapping("platformParamByType/{type}")
	public List<PlatformParam> fetchPlatformParamByType(@PathVariable("type") String type) {
		return platformParamService.fetchPlatformParamByType(type);
	}

	@GetMapping("/get-All-platformParams")
	public Map<String, List<PlatformParam>> findAllGroupedByType() {
		return platformParamService.findAllGroupedByType();
	}

	@GetMapping("/getTypeOfplatformParams")
	public Page<PlatformParamTypeDto> getAllPlatformParam(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		return platformParamService.getAllPlatformParam(page, size);
	}

	@PutMapping("updatePlatformParamsByType/{type}")
	public ResponseEntity<GenericResponseEntity> updatePlatformParamsByType(HttpServletRequest httpServletRequest,
			@PathVariable("type") String type, @RequestBody List<PlatformParam> updatedParams) {
		User updatedBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = platformParamService.updatePlatformParamsByType(type,
				updatedParams, updatedBy);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

}
