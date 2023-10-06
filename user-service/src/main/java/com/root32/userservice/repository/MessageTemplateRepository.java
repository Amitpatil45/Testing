package com.root32.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.MessageTemplate;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {

	MessageTemplate findByTemplateKey(String templateKey);

	Page<MessageTemplate> findByTemplateKeyContainingIgnoreCaseOrTemplateCodeContainingIgnoreCase(String templateKey,
			String templateCode, Pageable pageable);

}
