package com.root32.dentalproductservice.serviceImpl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.log.SysoCounter;
import com.root32.dentalproductservice.repository.AdminStockRepository;
import com.root32.dentalproductservice.repository.RetailerStockRepository;
import com.root32.dentalproductservice.repository.SaleRepository;
import com.root32.dentalproductservice.repository.SubRetailerStockRepository;
import com.root32.dentalproductservice.service.DashboardService;
import com.root32.dto.Dashboard;
import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private SaleRepository saleRepo;
	@Autowired
	private AdminStockRepository adminStockRepo;

	@Autowired
	private RetailerStockRepository retailerStockRepository;

	@Autowired
	private SubRetailerStockRepository subRetailerStockRepository;

	@Override
	public Dashboard getDashboard(Org userOrg) {
		Dashboard dashboard = new Dashboard();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime((date));
		cal.add(Calendar.DATE, -7);
		Date dateBefore = (cal.getTime());

		if (userOrg.getOrgTypeEnum().equals(OrgTypeEnum.ADMIN)) {

			dashboard.setProductSold(saleRepo.countOfSoldProduct(userOrg.getId(), dateBefore, date));
			dashboard.setRevenueGenerated(saleRepo.sumOfWeeklyRevenueOfAdmin(userOrg.getId(), dateBefore, date));
			dashboard.setProductSold(adminStockRepo.countDistinctByIsSoldAndSaleDateBetween(true, dateBefore, date));
			dashboard.setProductStockList(adminStockRepo.getProductStock());
			dashboard.setSaleList(saleRepo.findAllByOrgAndCreatedAtBetween(userOrg, dateBefore, date));

			return dashboard;
		}
		/* here reatiler and subretailer related method is remaining */
		if (userOrg.getOrgTypeEnum().equals(OrgTypeEnum.RETAILER)) {
			
			dashboard.setProductSold(saleRepo.countOfSoldProduct(userOrg.getId(), dateBefore, date));
			dashboard.setRevenueGenerated(saleRepo.sumOfWeeklyRevenueOfRetailer(userOrg.getId(), dateBefore, date));
			dashboard.setProductSold(
					retailerStockRepository.countDistinctByIsSoldAndSaleDateBetween(true, dateBefore, date));
			dashboard.setProductStockList(retailerStockRepository.getProductStock());
			dashboard.setSaleList(saleRepo.findAllByOrgAndCreatedAtBetween(userOrg, dateBefore, date));

			return dashboard;
		}
		if (userOrg.getOrgTypeEnum().equals(OrgTypeEnum.SUB_RETAILER)) {

			dashboard.setProductSold(saleRepo.countOfSoldProduct(userOrg.getId(), dateBefore, date));
			dashboard.setRevenueGenerated(saleRepo.sumOfWeeklyRevenueOfSubRetailer(userOrg.getId(), dateBefore, date));
			dashboard.setProductSold(
					subRetailerStockRepository.countDistinctByIsSoldAndSaleDateBetween(true, dateBefore, date));
			dashboard.setProductStockList(subRetailerStockRepository.getProductStock());
			dashboard.setSaleList(saleRepo.findAllByOrgAndCreatedAtBetween(userOrg, dateBefore, date));

			return dashboard;
		}

		return dashboard;
	}

}
