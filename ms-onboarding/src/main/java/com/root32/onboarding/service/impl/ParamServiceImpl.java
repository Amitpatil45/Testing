//package com.root32.onboarding.service.impl;
//
//import java.util.Date;
//import java.util.Objects;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import com.ameliorate.configsvc.repository.PlatformParamRepository;
//import com.ameliorate.entity.ClientParam;
//import com.ameliorate.entity.MessageTemplate;
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.PlatformParam;
//import com.ameliorate.entity.User;
//import com.ameliorate.onboarding.exception.OnboardingServiceException;
//import com.ameliorate.onboarding.exception.ParamServiceException;
//import com.ameliorate.onboarding.repository.ClientParamRepository;
//import com.ameliorate.onboarding.repository.MessageTemplateRepository;
//import com.ameliorate.onboarding.service.ParamService;
//import com.ameliorate.onboarding.util.StringUtil;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//@Service
//public class ParamServiceImpl implements ParamService {
//	private ClientParamRepository clientParamRepository;
//	private PlatformParamRepository platformParamRepository;
//	private MessageTemplateRepository messageTemplateRepository;
//
//	public ParamServiceImpl(ClientParamRepository clientParamRepository,
//			PlatformParamRepository platformParamRepository, MessageTemplateRepository messageTemplateRepository) {
//		this.clientParamRepository = clientParamRepository;
//		this.platformParamRepository = platformParamRepository;
//		this.messageTemplateRepository = messageTemplateRepository;
//	}
//
//	@Override
//	public Page<ClientParam> getAllClientParam(int page, int size, Org userOrg) {
//		Pageable pageable = getPageable(page, size);
//		if (userOrg != null && userOrg.getDtype() == 2) {
//			return clientParamRepository.findAllByOrg(userOrg, pageable);
//		}
//		return clientParamRepository.findAll(pageable);
//	}
//
//	private Pageable getPageable(int page, int size) {
//		if (page < 0) {
//			page = 0;
//		}
//		if (size < 0 || size > 100) {
//			size = 25;
//		}
//		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
//		return pageable;
//	}
//
//	@Override
//	public GenericResponseEntity saveClientParam(ClientParam clientParam, User createdBy) {
//		clientParam.setCreatedBy(createdBy);
//		clientParam.setCreatedDate(new Date());
//		clientParamRepository.save(clientParam);
//		return new GenericResponseEntity(201, "Client param saved successfully");
//	}
//
//	@Override
//	public GenericResponseEntity savePlatformParam(PlatformParam platformParam, User createdBy) {
//		validateDuplicateRecords(platformParam);
//		platformParam.setCreatedBy(createdBy);
//		platformParam.setCreatedDate(new Date());
//		platformParamRepository.save(platformParam);
//		return new GenericResponseEntity(201, "Platform param saved successfully");
//	}
//
//	private void validateDuplicateRecords(PlatformParam platformParam) {
//		String paramKey = platformParam.getParamKey();
//		if (StringUtil.isEmpty(paramKey)) {
//			throw new ParamServiceException("Invalid Param Key");
//		}
//		PlatformParam obj = platformParamRepository.findByParamKey(paramKey);
//
//		if (obj != null) {
//			throw new ParamServiceException("Duplicate record found - Param Key");
//		}
//	}
//
//	@Override
//	public GenericResponseEntity saveMessageTemplate(MessageTemplate messageTemplate, User createdBy) {
//		validateDuplicateRecords(messageTemplate);
//		messageTemplate.setCreatedBy(createdBy);
//		messageTemplate.setCreatedDate(new Date());
//		messageTemplateRepository.save(messageTemplate);
//
//		return new GenericResponseEntity(201, "Message template created successfully");
//	}
//
//	private void validateDuplicateRecords(MessageTemplate messageTemplate) {
//		String templateKey = messageTemplate.getTemplateKey();
//		if (StringUtil.isEmpty(templateKey)) {
//			throw new ParamServiceException("Invalid Template Key");
//		}
//		MessageTemplate obj = messageTemplateRepository.findByTemplateKey(templateKey);
//
//		if (obj != null) {
//			throw new ParamServiceException("Duplicate record found - Template Key");
//		}
//	}
//
//	@Override
//	public Page<MessageTemplate> getAllMessageTemplate(int page, int size) {
//		Pageable pageable = getPageable(page, size);
//		return messageTemplateRepository.findAll(pageable);
//	}
//
//	@Override
//	public Page<PlatformParam> getAllPlatformParam(int page, int size) {
//		Pageable pageable = getPageable(page, size);
//		return platformParamRepository.findAll(pageable);
//	}
//
//	@Override
//	public GenericResponseEntity deleteClientParamById(Long id) {
//		Optional<ClientParam> clientParam = clientParamRepository.findById(id);
//		if (!clientParam.isPresent()) {
//			throw new OnboardingServiceException("clientParam is Not Available");
//		}
//		clientParamRepository.deleteById(id);
//		return new GenericResponseEntity(201, "Client param deleted successfully!");
//	}
//
//	@Override
//	public GenericResponseEntity deletePlatformParamById(Long id) {
//		Optional<PlatformParam> platformParam = platformParamRepository.findById(id);
//		if (!platformParam.isPresent()) {
//			throw new OnboardingServiceException("Platform param is not present");
//		}
//		platformParamRepository.deleteById(id);
//		return new GenericResponseEntity(201, "Platform param deleted successfully!");
//	}
//
//	@Override
//	public GenericResponseEntity updateClientParam(Long id, ClientParam clientParam, User updatedBy) {
//		Optional<ClientParam> clientParamOptional = clientParamRepository.findById(id);
//		if (!clientParamOptional.isPresent()) {
//			throw new OnboardingServiceException("Client param is not available");
//		}
//		ClientParam clientParamDB = clientParamRepository.findById(id).get();
//		if (Objects.nonNull(clientParam.getMaxExtensionsAllowed()) && (clientParam.getMaxExtensionsAllowed() != 0)) {
//			clientParamDB.setMaxExtensionsAllowed(clientParam.getMaxExtensionsAllowed());
//		}
//
//		if (Objects.nonNull(clientParam.getMaxBidTimeExtensions()) && (clientParam.getMaxBidTimeExtensions() != 0)) {
//			clientParamDB.setMaxBidTimeExtensions(clientParam.getMaxBidTimeExtensions());
//		}
//
//		if (Objects.nonNull(clientParam.getSoftExpiryDurationInDays())
//				&& (clientParam.getSoftExpiryDurationInDays() != 0)) {
//			clientParamDB.setSoftExpiryDurationInDays(clientParam.getSoftExpiryDurationInDays());
//		}
//		if (Objects.nonNull(clientParam.getHardExpiryDurationInDays())
//				&& (clientParam.getHardExpiryDurationInDays() != 0)) {
//			clientParamDB.setHardExpiryDurationInDays(clientParam.getHardExpiryDurationInDays());
//		}
//		if (Objects.nonNull(clientParam.getMaxUnsuccessfulAttempts())
//				&& (clientParam.getMaxUnsuccessfulAttempts() != 0)) {
//			clientParamDB.setMaxUnsuccessfulAttempts(clientParam.getMaxUnsuccessfulAttempts());
//		}
//		if (Objects.nonNull(clientParam.getOtpExpiryInMinutes()) && (clientParam.getOtpExpiryInMinutes() != 0)) {
//			clientParamDB.setOtpExpiryInMinutes(clientParam.getOtpExpiryInMinutes());
//		}
//		if (Objects.nonNull(clientParam.getPasswordExpiryDays()) && (clientParam.getPasswordExpiryDays() != 0)) {
//			clientParamDB.setPasswordExpiryDays(clientParam.getPasswordExpiryDays());
//		}
//		if (Objects.nonNull(clientParam.getRfqDueReminersBeforeDueDate())
//				&& (clientParam.getRfqDueReminersBeforeDueDate() != 0)) {
//			clientParamDB.setRfqDueReminersBeforeDueDate(clientParam.getRfqDueReminersBeforeDueDate());
//		}
//		if (Objects.nonNull(clientParam.isLockAfterMaxUnsuccessfulAttempts())) {
//			clientParamDB.setLockAfterMaxUnsuccessfulAttempts(true);
//		}
//
//		if ((clientParam.getDefaultCurrency() != null)) {
//			clientParamDB.setDefaultCurrency(clientParam.getDefaultCurrency());
//		}
//		clientParamDB.setUpdatedDate(new Date());
//		clientParamDB.setUpdatedBy(updatedBy);
//
//		clientParamRepository.save(clientParamDB);
//		return new GenericResponseEntity(201, "Client param updated successfully");
//	}
//
//	@Override
//	public GenericResponseEntity updatePlatformParam(Long id, PlatformParam platformParam, User updatedBy) {
//
//		Optional<PlatformParam> platformParamOptional = platformParamRepository.findById(id);
//		if (!platformParamOptional.isPresent()) {
//			throw new OnboardingServiceException("Platform param is not present");
//		}
//
//		PlatformParam platformParamDB = platformParamRepository.findById(id).get();
//		platformParamDB.setUpdatedDate(new Date());
//		platformParamDB.setUpdatedBy(updatedBy);
//		platformParamDB.setParamValue(platformParam.getParamValue());
//		platformParamRepository.save(platformParamDB);
//		return new GenericResponseEntity(201, "Platform param updated successfully");
//	}
//
//	@Override
//	public PlatformParam fetchPlatformParamById(Long id) {
//		Optional<PlatformParam> platformParam = platformParamRepository.findById(id);
//		if (!platformParam.isPresent()) {
//			throw new OnboardingServiceException("Platform param is not present");
//		}
//		return platformParamRepository.findById(id).get();
//	}
//
//	@Override
//	public ClientParam fetchClientParamById(Long id) {
//		Optional<ClientParam> clientParam = clientParamRepository.findById(id);
//		if (!clientParam.isPresent()) {
//			throw new OnboardingServiceException("Client param is not available");
//		}
//		return clientParamRepository.findById(id).get();
//	}
//
//	@Override
//	public MessageTemplate fetchMessageTemplateById(Long id) {
//		Optional<MessageTemplate> messageOptional = messageTemplateRepository.findById(id);
//		if (!messageOptional.isPresent()) {
//			throw new ParamServiceException("Message template is not available");
//		}
//		return messageTemplateRepository.findById(id).get();
//	}
//
//	@Override
//	public GenericResponseEntity updateMessageTemplate(Long id, MessageTemplate messageTemplate, User updatedBy)
//			throws ParamServiceException {
//		Optional<MessageTemplate> messageOptional = messageTemplateRepository.findById(id);
//		if (!messageOptional.isPresent()) {
//			throw new ParamServiceException("Message template is not available");
//		}
//		MessageTemplate messageTemplateDB = messageTemplateRepository.findById(id).get();
//		if ((messageTemplate.getTemplateKey() != null)) {
//			messageTemplateDB.setTemplateKey(messageTemplate.getTemplateKey());
//		}
//
//		if (messageTemplate.getTemplateValue() != null) {
//			messageTemplateDB.setTemplateValue(messageTemplate.getTemplateValue());
//		}
//
//		if (messageTemplate.getTemplateCode() != null) {
//			messageTemplateDB.setTemplateCode(messageTemplate.getTemplateCode());
//		}
//
//		messageTemplateDB.setUpdatedDate(new Date());
//		messageTemplateDB.setUpdatedBy(updatedBy);
//
//		messageTemplateRepository.save(messageTemplateDB);
//		return new GenericResponseEntity(201, "Message template updated successfully");
//
//	}
//
//	@Override
//	public GenericResponseEntity deleteMessageTemplateById(Long id) {
//		messageTemplateRepository.deleteById(id);
//		return new GenericResponseEntity(201, "Message template deleted successfully!");
//	}
//}
