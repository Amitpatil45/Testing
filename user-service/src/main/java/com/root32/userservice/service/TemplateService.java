package com.root32.userservice.service;

import org.springframework.data.domain.Page;

import com.root32.dto.GenericResponseEntity;
import com.root32.entity.MessageTemplate;

public interface TemplateService {

	MessageTemplate fetchTemplateById(Long id);

	GenericResponseEntity updateTemplateById(Long id, MessageTemplate messageTemplate);

	Page<MessageTemplate> getAlltemplates(String searchItem, int page, int size);

}
