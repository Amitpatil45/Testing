package com.root32.dentalproductservice.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.root32.entity.ProductMaster;

@Repository
public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

	int countByName(String name);

	int countByProductCode(String productCode);

	Page<ProductMaster> findAll(Pageable pageable);

	Long countByCategoryIdAndIsActive(Long id, boolean b);

	int countByNameIgnoreCase(String name);

	List<ProductMaster> findAllByIsActive(boolean b);

	Long countByCategoryId(Long id);

	Long countByUomId(Long id);

	ProductMaster findByProductCode(String productCode);
	
//	@Query(value = "SELECT p.*, c.name FROM product_master p " +
//		       "JOIN category_master c ON p.category_id = c.id " +
//		       "WHERE LOWER(p.name) LIKE %:searchItem% OR LOWER(c.name) LIKE %:searchItem%",
//		       nativeQuery = true)
//
//	Page<ProductMaster> searchProductsByNameOrCategoryIgnoreCase(@Param("searchItem")
//            String searchItem, Pageable pageable);

	Page<ProductMaster> findAllByNameContainingOrCategoryNameContainingIgnoreCase(String searchItem, String searchItem2,
			Pageable pageable);

//	Page<ProductMaster> findAllByNameOrCategory_NameContainingIgnoreCase(String searchItem, String searchItem2,
//			Pageable pageable);

//	Page<ProductMaster> findAllByNameOrCategoryNameContainingIgnoreCase(String searchItem, String searchItem2,
//			Pageable pageable);


}
