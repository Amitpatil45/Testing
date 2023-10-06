package com.root32.dentalproductservice.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.dto.RetailerInventoryDto;
import com.root32.dto.RetailerWiseInventoryDto;
import com.root32.entity.Org;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.entity.RetailerStock;
import com.root32.pojo.DashBoardProductStock;

@Repository
public interface RetailerStockRepository extends JpaRepository<RetailerStock, Long> {
// rough
//	String query1 = "FROM retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false AND a.org_id = :id";
//
//	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT *  + "query1")as quantity FROM product_master p", nativeQuery = true)
//	Page<RetailerInventoryDto> findProductWiseQuantity(Pageable pageable, Long id);

//	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false AND a.org_id = :id) as quantity FROM product_master p", nativeQuery = true)
//	Page<RetailerInventoryDto> findProductWiseQuantity(Pageable pageable, Long id);

	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false AND a.org_id = :id) as quantity FROM product_master p", nativeQuery = true)
	List<RetailerInventoryDto> findProductWiseQuantityList(Long id);

	@Query("SELECT  o.id as orgId,o.businessName as businessName,p.id as productId,p.name as productName, SUM(CASE WHEN rs.isSold = false AND rs.isOutward = false THEN 1 ELSE 0 END) as quantity, MAX(CASE WHEN rs.isOutward = false THEN rs.createdAt ELSE NULL END) as lastRefillDate, MAX(CASE WHEN rs.isSold = true THEN rs.saleDate ELSE NULL END) as lastSaleDate FROM RetailerStock rs JOIN rs.org o JOIN rs.product p GROUP BY o.id, o.businessName, p.id, p.name ORDER BY o.id DESC ")
	List<RetailerWiseInventoryDto> findRetailerWiseInventory();

	RetailerStock findByProduct(ProductMaster product);

	List<RetailerStock> findAllByProductBarcodeIn(List<ProductBarcode> barcodeList);

	Optional<RetailerStock> findByProductBarcodeIdAndProductIdAndIsSold(Long id, Long id2, boolean b);

	Long countDistinctByIsSoldAndSaleDateBetween(boolean b, Date dateBefore, Date date);

	@Query(value = "SELECT p.name as productName ,(SELECT COUNT(*) FROM retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false) as count FROM product_master p WHERE (\n"
			+ "    SELECT COUNT(*) \n" + "    FROM retailer_stock a \n" + "    WHERE a.product_id = p.id \n"
			+ "    AND a.is_sold = false\n" + ") > 0", nativeQuery = true)
	List<DashBoardProductStock> getProductStock();

	@Query(value = "SELECT count(1) FROM retailer_stock where product_id = :productId AND product_barcode_id = :barcodeId AND (is_sold=true or is_outward = true)", nativeQuery = true)
	Long getCountOfRetailerStockByProduct(Long productId, Long barcodeId);

	@Query(value = "SELECT * FROM retailer_stock where product_barcode_id = :barcodeId AND product_id = :productId", nativeQuery = true)
	RetailerStock getRetailerStockByProductBarcode(Long barcodeId, Long productId);

	@Query(value = "SELECT p.name as productName , p.thumbnail_image as thumbnailImage,(SELECT COUNT(*) FROM retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false AND a.is_outward = false AND a.org_id = :id) as quantity FROM product_master p", nativeQuery = true)
	List<RetailerInventoryDto> findProductWiseQuantity(Long id);

	@Query(value = "SELECT p.name as productName, p.thumbnail_image as thumbnailImage, "
			+ "(SELECT COUNT(*) FROM retailer_stock a WHERE a.product_id = p.id AND "
			+ "a.is_sold = false AND a.is_outward = false AND a.org_id = :id) as quantity " + "FROM product_master p "
			+ "WHERE (p.name LIKE %:search%)", nativeQuery = true)
	List<RetailerInventoryDto> findProductWiseQuantityContainingSearch(Long id, String search);

	@Query("SELECT  o.id as orgId,o.businessName as businessName,p.id as productId,p.name as productName, SUM(CASE WHEN rs.isSold = false AND rs.isOutward = false THEN 1 ELSE 0 END) as quantity, MAX(CASE WHEN rs.isOutward = false THEN rs.createdAt ELSE NULL END) as lastRefillDate, MAX(CASE WHEN rs.isSold = true THEN rs.saleDate ELSE NULL END) as lastSaleDate FROM RetailerStock rs JOIN rs.org o JOIN rs.product p WHERE (o.businessName LIKE %:search%) GROUP BY o.id, o.businessName, p.id, p.name ORDER BY o.id DESC ")
	List<RetailerWiseInventoryDto> findRetailerWiseInventoryContainingBusinessName(String search);

	Optional<RetailerStock> findByProductBarcodeId(Long id);

	Optional<RetailerStock> findByProductBarcodeAndOrg(ProductBarcode productBarcode, Org org);

}
