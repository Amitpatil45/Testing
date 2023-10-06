package com.root32.userservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dto.GenericResponseEntity;
import com.root32.entity.MessageTemplate;
import com.root32.entity.User;
import com.root32.userservice.service.TemplateService;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/")
public class TemplateController {
	@Autowired
	private TemplateService templateService;

	@GetMapping("/messageTemplate/{id}")
	public MessageTemplate fetchTemplateById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return templateService.fetchTemplateById(id);
	}

	@GetMapping("/all-messageTemplates")
	public Page<MessageTemplate> getAllproducts(HttpServletRequest httpServletRequest,
			@RequestParam (required = false)String searchItem,	@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
		return templateService.getAlltemplates(searchItem,page, size);
	}

	@PutMapping("/messageTemplate/{id}")
	public ResponseEntity<GenericResponseEntity> updateTemplate(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody MessageTemplate messageTemplate) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		messageTemplate.setUpdatedBy(user);
		GenericResponseEntity gre = templateService.updateTemplateById(id, messageTemplate);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}
}
