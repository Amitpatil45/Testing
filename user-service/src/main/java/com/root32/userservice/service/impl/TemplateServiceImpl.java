package com.root32.userservice.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysql.cj.util.StringUtils;
import com.root32.dto.GenericResponseEntity;
import com.root32.entity.MessageTemplate;
import com.root32.userservice.exception.DataValidationException;
import com.root32.userservice.repository.MessageTemplateRepository;
import com.root32.userservice.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {
	@Autowired
	private MessageTemplateRepository MessageTemplateRepository;

	@Override
	public MessageTemplate fetchTemplateById(Long id) {
		// TODO Auto-generated method stub
		return getTemplateById(id);
	}

	private MessageTemplate getTemplateById(Long id) {
		Optional<MessageTemplate> templateOptional = MessageTemplateRepository.findById(id);
		if (!templateOptional.isPresent()) {
			throw new DataValidationException("Template not found!");
		}
		return templateOptional.get();
	}

	@Override
	public Page<MessageTemplate> getAlltemplates(String searchItem,int page, int size) {
		Pageable pageable = buildPagable(page, size);
		Page<MessageTemplate> messageTemplate = null;
//		messageTemplate = MessageTemplateRepository.findAll(pageable);
		if (searchItem != null && !StringUtils.isEmptyOrWhitespaceOnly(searchItem)) {
			searchItem = searchItem.trim();
		messageTemplate=MessageTemplateRepository.findByTemplateKeyContainingIgnoreCaseOrTemplateCodeContainingIgnoreCase(searchItem,searchItem,pageable);
		return messageTemplate;
		}
		messageTemplate = MessageTemplateRepository.findAll(pageable);
		return messageTemplate;

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
	public GenericResponseEntity updateTemplateById(Long id, MessageTemplate messageTemplate) {
		MessageTemplate templateDB = getTemplateById(id);

		templateDB.setTemplateValue(messageTemplate.getTemplateValue());

		templateDB.setUpdatedDate(new Date());
		templateDB.setUpdatedBy(messageTemplate.getUpdatedBy());
		MessageTemplateRepository.save(templateDB);
		return new GenericResponseEntity(202, "Template updated successfully!");
	}

}
