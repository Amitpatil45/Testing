package com.root32.dentalproductservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.root32.entity.CategoryMaster;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryMaster, Long> {

	boolean existsByName(String name);

	int countByName(String name);

	int countByCategoryCode(String categoryCode);

	List<CategoryMaster> findAllByIsActive(boolean b);

	int countByNameIgnoreCase(String name);

	Page<CategoryMaster> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name,
			String description, Pageable pageable);
}
