package com.root32.dentalproductservice.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.dto.AdminInventoryDto;
import com.root32.entity.AdminStock;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.pojo.DashBoardProductStock;

@Repository
public interface AdminStockRepository extends JpaRepository<AdminStock, Long> {

	AdminStock findByProductId(Long id);

	// gives product name and quantity
//	@Query(value="SELECT p.name as productName ,(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id) as quantity FROM product_master p",nativeQuery=true)

//	// gives product name , thumbnail image, quantity
//	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id AND a.is_sold = false) as quantity FROM product_master p", nativeQuery = true)
//	Page<AdminInventoryDto> findProductWiseQuantity(Pageable pageable);

	// gives product name , thumbnail image, quantity
	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false) as quantity FROM product_master p", nativeQuery = true)
	List<AdminInventoryDto> findProductWiseQuantityForInventory();

	Long countByProductId(Long id);

	Optional<AdminStock> findByProductBarcodeId(Long id);

	Optional<AdminStock> findByProductBarcodeIdAndProductId(Long id, Long id2);

	Optional<AdminStock> findByProductBarcodeIdAndProductIdAndIsSold(Long id, Long id2, boolean b);

	Optional<AdminStock> findByProductBarcodeIdAndIsSold(Long id, boolean b);

	List<AdminStock> findAllByProductBarcodeIn(List<ProductBarcode> barcodeList);

	Long countDistinctByIsSold(boolean b);

	Long countDistinctByIsSoldAndSaleDateBetween(boolean b, Date dateBefore, Date date);

	@Query(value = "SELECT p.name as productName ,(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward=false) as count FROM product_master p WHERE (\n"
			+ "    SELECT COUNT(*) \n" + "    FROM admin_stock a \n" + "    WHERE a.product_id = p.id \n"
			+ "    AND a.is_sold = false AND a.is_outward = false\n" + ") > 0", nativeQuery = true)
	List<DashBoardProductStock> getProductStock();

	AdminStock findByProduct(ProductMaster product);

	Long countByProduct(ProductMaster product);

	@Query(value = "SELECT count(1) FROM admin_stock where product_id = :productId AND product_barcode_id = :barcodeId AND (is_sold=true or is_outward = true)", nativeQuery = true)
	Long getCountOfAdminStockByProduct(Long productId, Long barcodeId);

	@Query(value = "SELECT * FROM admin_stock where product_barcode_id = :barcodeId AND product_id = :productId", nativeQuery = true)
	AdminStock getAdminStockByProductBarcode(Long barcodeId, Long productId);

	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false) as quantity FROM product_master p", nativeQuery = true)
	List<AdminInventoryDto> findProductWiseQuantity();

	@Query(value = "SELECT p.name as productName, p.thumbnail_image as thumbnailImage, "
			+ "(SELECT COUNT(*) FROM admin_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false) as quantity "
			+ "FROM product_master p " + "WHERE (p.name LIKE %:search%)", nativeQuery = true)
	List<AdminInventoryDto> findProductWiseQuantityContainingSearch(String search);

}
