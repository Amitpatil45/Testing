//package com.root32.onboarding.service;
//
//import org.springframework.data.domain.Page;
//
//import com.ameliorate.entity.ClientParam;
//import com.ameliorate.entity.MessageTemplate;
//import com.ameliorate.entity.Org;
//import com.ameliorate.entity.PlatformParam;
//import com.ameliorate.entity.User;
//import com.ameliorate.pojo.GenericResponseEntity;
//
//public interface ParamService {
//
//	GenericResponseEntity saveClientParam(ClientParam clientParam, User createdBy);
//
//	GenericResponseEntity savePlatformParam(PlatformParam platformParam, User createdBy);
//
//	Page<ClientParam> getAllClientParam(int page, int size, Org userOrg);
//
//	Page<PlatformParam> getAllPlatformParam(int page, int size);
//
//	GenericResponseEntity deleteClientParamById(Long id);
//
//	GenericResponseEntity deletePlatformParamById(Long id);
//
//	GenericResponseEntity updateClientParam(Long id, ClientParam clientParam, User updatedBy);
//
//	GenericResponseEntity updatePlatformParam(Long id, PlatformParam platformParam, User updatedBy);
//
//	PlatformParam fetchPlatformParamById(Long id);
//
//	ClientParam fetchClientParamById(Long id);
//
//	GenericResponseEntity saveMessageTemplate(MessageTemplate messageTemplate, User createdBy);
//
//	Page<MessageTemplate> getAllMessageTemplate(int page, int size);
//
//	MessageTemplate fetchMessageTemplateById(Long id);
//
//	GenericResponseEntity updateMessageTemplate(Long id, MessageTemplate messageTemplate, User updatedBy);
//
//	GenericResponseEntity deleteMessageTemplateById(Long id);
//
//}
