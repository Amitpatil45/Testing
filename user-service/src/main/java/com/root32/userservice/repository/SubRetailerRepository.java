package com.root32.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root32.entity.SubRetailer;

public interface SubRetailerRepository extends JpaRepository<SubRetailer, Long> {

	Long countByBusinessNameContainingIgnoreCase(String string);

}
