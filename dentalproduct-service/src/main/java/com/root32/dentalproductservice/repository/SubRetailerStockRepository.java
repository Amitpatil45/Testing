package com.root32.dentalproductservice.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.root32.entity.AdminStock;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.entity.RetailerStock;
import com.root32.entity.SubRetailerStock;
import com.root32.pojo.DashBoardProductStock;

public interface SubRetailerStockRepository extends JpaRepository<SubRetailerStock, Long> {

	RetailerStock findByProduct(ProductMaster product);

	List<SubRetailerStock> findAllByProductBarcodeIn(List<ProductBarcode> barcodeList);

	Optional<SubRetailerStock> findByProductBarcodeIdAndProductIdAndIsSold(Long id, Long id2, boolean b);

	Long countDistinctByIsSoldAndSaleDateBetween(boolean b, Date dateBefore, Date date);

	@Query(value = "SELECT p.name as productName ,(SELECT COUNT(*) FROM sub_retailer_stock a WHERE a.product_id = p.id AND a.is_sold = false) as count FROM product_master p WHERE (\n"
			+ "    SELECT COUNT(*) \n" + "    FROM sub_retailer_stock a \n" + "    WHERE a.product_id = p.id \n"
			+ "    AND a.is_sold = false\n" + ") > 0", nativeQuery = true)
	List<DashBoardProductStock> getProductStock();

	@Query(value = "SELECT count(1) FROM sub_retailer_stock where product_id = :productId AND product_barcode_id = :barcodeId AND (is_sold=true or is_outward = true)", nativeQuery = true)
	Long getCountOfSubRetailerStockByProduct(Long productId,Long barcodeId);

	@Query(value = "SELECT * FROM sub_retailer_stock where product_barcode_id = :barcodeId AND product_id = :productId", nativeQuery = true)
	SubRetailerStock getSubRetailerStockByProductBarcode(Long barcodeId,Long productId);

}
