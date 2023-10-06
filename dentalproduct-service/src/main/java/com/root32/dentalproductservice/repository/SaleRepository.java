package com.root32.dentalproductservice.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.root32.entity.Org;
import com.root32.entity.Sale;
import com.root32.pojo.DashboardSales;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(value = "select sum(admin_revenue) as revenueGenerated  from sale \n"
			+ "where org_id= :userOrgId AND created_at between :fromDate AND :toDate", nativeQuery = true)
	BigDecimal sumOfWeeklyRevenueOfAdmin(Long userOrgId, Date fromDate, Date toDate);

	@Query(value = "select sum(retailer_revenue) as revenueGenerated  from sale \n"
			+ "where org_id= :userOrgId AND created_at between :fromDate AND :toDate", nativeQuery = true)
	BigDecimal sumOfWeeklyRevenueOfRetailer(Long userOrgId, Date fromDate, Date toDate);

	@Query(value = "select sum(sub_retailer_revenue) as revenueGenerated  from sale \n"
			+ "where org_id= :userOrgId AND created_at between :fromDate AND :toDate", nativeQuery = true)
	BigDecimal sumOfWeeklyRevenueOfSubRetailer(Long userOrgId, Date fromDate, Date toDate);

	@Query(value = "SELECT distinct(count(pb.id)) as productSold FROM sale s join sale_sale_datas ssd on s.id = ssd.sale_id	join sale_data sd on ssd.sale_datas_id = sd.id join product_master pm on sd.product_id = pm.id join sale_data_product_barcodes sdpb on sd.id = sdpb.sale_data_id join product_barcode pb on sdpb.product_barcodes_id = pb.id where s.org_id = :userOrgId and s.created_at between :fromDate AND :toDate", nativeQuery = true)
	Long countOfSoldProduct(Long userOrgId, Date fromDate, Date toDate);

	@Query(value = "select * from sale s where org_id= :userOrgId AND created_at between :fromDate AND :date", nativeQuery = true)
	List<DashboardSales> getAllRecentSales(Long userOrgId, Date fromDate, Date date);

	Page<Sale> findAllByOrg(Pageable pageable, Org org);

	List<Sale> findAllByOrg(Org org);

	List<Sale> findAllByOrgAndCreatedAtBetween(Org userOrg, Date dateBefore, Date date);

//	Long countByProduct(ProductMaster product);

//	@Query(value="",nativeQuery=true)
//	Long countByProductBarcodes(Long barcodeId);

}
