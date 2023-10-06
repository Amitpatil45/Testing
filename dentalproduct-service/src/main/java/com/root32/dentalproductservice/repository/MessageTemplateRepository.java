package com.root32.dentalproductservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.MessageTemplate;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long>{

	MessageTemplate findByTemplateKey(String templateKey);

}
