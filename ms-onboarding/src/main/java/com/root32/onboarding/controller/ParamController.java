//package com.root32.onboarding.controller;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ameliorate.entity.ClientParam;
//import com.ameliorate.entity.MessageTemplate;
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.PlatformParam;
//import com.ameliorate.entity.User;
//import com.ameliorate.onboarding.service.ParamService;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//@RequestMapping("/api/v1/parameter")
//@RestController
//public class ParamController {
//	private ParamService paramService;
//
//	public ParamController(ParamService paramService) {
//		this.paramService = paramService;
//	}
//
//	@PostMapping("clientParam")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> saveClientParam(HttpServletRequest httpServletRequest,
//			@Valid @RequestBody ClientParam clientParam) {
//		User createdBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
//		clientParam.setOrg(userOrg);
//		GenericResponseEntity gre = paramService.saveClientParam(clientParam, createdBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@PostMapping("platformParam")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> savePlatformParam(HttpServletRequest httpServletRequest,
//			@Valid @RequestBody PlatformParam platformParam) {
//		User createdBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		GenericResponseEntity gre = paramService.savePlatformParam(platformParam, createdBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//	@PreAuthorize("hasAnyAuthority('RB0000206','RB0000200')")
//	@GetMapping("clientParams")
//	@Transactional
//	public Page<ClientParam> getAllClientParam(HttpServletRequest httpServletRequest,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
//		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);
//		return paramService.getAllClientParam(page, size, userOrg);
//	}
//
//	@GetMapping("platformParams")
//	@Transactional
//	public Page<PlatformParam> getAllPlatformParam(@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "25") int size) {
//		return paramService.getAllPlatformParam(page, size);
//	}
//
//	@GetMapping("platformParam/{id}")
//	@Transactional
//	public PlatformParam fetchPlatformParamById(@PathVariable("id") Long id) {
//		return paramService.fetchPlatformParamById(id);
//	}
//
//	@GetMapping("clientParam/{id}")
//	@Transactional
//	public ClientParam fetchClientParamById(@PathVariable("id") Long id) {
//		return paramService.fetchClientParamById(id);
//	}
//
//	@DeleteMapping("clientParam/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> deleteClientParamById(@PathVariable("id") Long id) {
//		GenericResponseEntity gre = paramService.deleteClientParamById(id);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@DeleteMapping("platformParam/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> deletePlatformParamById(@PathVariable("id") Long id) {
//		GenericResponseEntity gre = paramService.deletePlatformParamById(id);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@PreAuthorize("hasAuthority('RB0000206')")
//	@PutMapping("clientParam/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> updateClientParam(HttpServletRequest httpServletRequest,
//			@PathVariable("id") Long id, @RequestBody ClientParam clientParam) {
//		User updatedBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		GenericResponseEntity gre = paramService.updateClientParam(id, clientParam, updatedBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@PutMapping("platformParam/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> updatePlatformParam(HttpServletRequest httpServletRequest,
//			@PathVariable("id") Long id, @RequestBody PlatformParam platformParam) {
//		User updatedBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		GenericResponseEntity gre = paramService.updatePlatformParam(id, platformParam, updatedBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@PostMapping("messageTemplate")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> saveMessageTemplate(HttpServletRequest httpServletRequest,
//			@Valid @RequestBody MessageTemplate messageTemplate) {
//		User createdBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		GenericResponseEntity gre = paramService.saveMessageTemplate(messageTemplate, createdBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//	}
//
//	@GetMapping("messageTemplates")
//	@Transactional
//	public Page<MessageTemplate> getAllMessageTemplate(@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "25") int size) {
//		return paramService.getAllMessageTemplate(page, size);
//	}
//
//	@GetMapping("messageTemplate/{id}")
//	@Transactional
//	public MessageTemplate fetchMessageTemplateById(@PathVariable("id") Long id) {
//		return paramService.fetchMessageTemplateById(id);
//	}
//
//	@PutMapping("messageTemplate/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> updateMessageTemplate(HttpServletRequest httpServletRequest,
//			@PathVariable("id") Long id, @RequestBody MessageTemplate messageTemplate) {
//		User updatedBy = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
//		GenericResponseEntity gre = paramService.updateMessageTemplate(id, messageTemplate, updatedBy);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//
//	}
//
//	@DeleteMapping("messageTemplate/{id}")
//	@Transactional
//	public ResponseEntity<GenericResponseEntity> deleteMessageTemplateById(@PathVariable("id") Long id) {
//		GenericResponseEntity gre = paramService.deleteMessageTemplateById(id);
//		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
//	}
//}
