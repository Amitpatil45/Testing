package com.root32.dentalproductservice.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysql.cj.util.StringUtils;
import com.root32.dentalproductservice.exception.UserAuthenticationException;
import com.root32.dentalproductservice.repository.AdminStockRepository;
import com.root32.dentalproductservice.repository.BatchRepository;
import com.root32.dentalproductservice.repository.ProductBarcodeRepository;
import com.root32.dentalproductservice.repository.ProductMasterRepository;
import com.root32.dentalproductservice.repository.RetailerStockRepository;
import com.root32.dentalproductservice.repository.SaleRepository;
import com.root32.dentalproductservice.repository.SubRetailerStockRepository;
import com.root32.dentalproductservice.service.SaleService;
import com.root32.dentalproductservice.util.EntityNumberUtil;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.InitiateSaleResponseDto;
import com.root32.entity.AdminStock;
import com.root32.entity.Batch;
import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.entity.RetailerStock;
import com.root32.entity.Sale;
import com.root32.entity.SaleData;
import com.root32.entity.SubRetailerStock;
import com.root32.entity.User;

@Service
public class SaleServiceImpl implements SaleService {

	@Autowired
	private ProductBarcodeRepository productBarcodeRepository;

	@Autowired
	private AdminStockRepository adminStockRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private RetailerStockRepository retailerStockRepository;

	@Autowired
	private SubRetailerStockRepository subRetailerStockRepository;

	@Autowired
	private BatchRepository batchRepository;

	private final EntityNumberUtil entityNumberUtil;

	public SaleServiceImpl(EntityNumberUtil entityNumberUtil) {
		this.entityNumberUtil = entityNumberUtil;
	}

	private ProductBarcode fetchProdutBarcodeByBarcodeString(String productBarcodeString) {
		Optional<ProductBarcode> productBarcodeOptional = productBarcodeRepository
				.findByProductBarcode(productBarcodeString);

		if (productBarcodeOptional.isEmpty()) {
			throw new UserAuthenticationException("Product not available!");
		}
		return productBarcodeOptional.get();
	}

	private AdminStock fetchPAdminStockByProductBarcodeId(Long id) {
		Optional<AdminStock> adminStockOptional = adminStockRepository.findByProductBarcodeId(id);

		if (adminStockOptional.isEmpty()) {
			throw new UserAuthenticationException("Product not present in stock!");
		}
		return adminStockOptional.get();
	}

	private ProductMaster fetchProductMasterById(Long id) {
		Optional<ProductMaster> productMasterOptional = productMasterRepository.findById(id);
		if (productMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Product not available!");
		}
		return productMasterOptional.get();
	}

	private RetailerStock fetchRetailerStockByProductBarcodeAndOrg(ProductBarcode productBarcode, Org org) {
		Optional<RetailerStock> retailerStockOptional = retailerStockRepository
				.findByProductBarcodeAndOrg(productBarcode, org);
		if (retailerStockOptional.isEmpty()) {
			throw new UserAuthenticationException("Product not present in stock");
		}
		return retailerStockOptional.get();
	}

	@Override
	public InitiateSaleResponseDto getinitiateSaleResponseDto(String productBarcode, Org org) {

		InitiateSaleResponseDto initiateSaleResponseDto = new InitiateSaleResponseDto();

		ProductBarcode productBarcodeDB = fetchProdutBarcodeByBarcodeString(productBarcode);

		if (org.getOrgTypeEnum() == OrgTypeEnum.ADMIN) {

			AdminStock adminStockDB = fetchPAdminStockByProductBarcodeId(productBarcodeDB.getId());

			if (adminStockDB.getIsSold() == true) {
				throw new UserAuthenticationException("This product is already soled!");
			}
			if (adminStockDB.getIsOutward() == true) {
				throw new UserAuthenticationException("This product is already outwarded!");
			}

			Batch batchDB = batchRepository.findById(adminStockDB.getBatch().getId()).get();
			if (batchDB.getExpiryDate().before(new Date()) || batchDB.getExpiryDate().equals(new Date())) {

				throw new UserAuthenticationException("Product expired!");
			}

			ProductMaster productMasterDB = fetchProductMasterById(adminStockDB.getProduct().getId());

			initiateSaleResponseDto.setProduct(productMasterDB);
			initiateSaleResponseDto.setProductBarcode(productBarcodeDB);

		} else if (org.getOrgTypeEnum() == OrgTypeEnum.RETAILER) {
			RetailerStock retailerDtockDB = fetchRetailerStockByProductBarcodeAndOrg(productBarcodeDB, org);
			if (retailerDtockDB.getIsSold() == true) {
				throw new UserAuthenticationException("This product is already soled!");
			}
			if (retailerDtockDB.getIsOutward() == true) {
				throw new UserAuthenticationException("This product is already outwarded!");
			}

			Batch batchDB = batchRepository.findById(productBarcodeDB.getBelongsToBatch().getId()).get();
			if (batchDB.getExpiryDate().before(new Date()) || batchDB.getExpiryDate().equals(new Date())) {

				throw new UserAuthenticationException("Product expired!");
			}
			ProductMaster productMasterDB = fetchProductMasterById(retailerDtockDB.getProduct().getId());

			initiateSaleResponseDto.setProduct(productMasterDB);
			initiateSaleResponseDto.setProductBarcode(productBarcodeDB);
		}

		return initiateSaleResponseDto;

	}

	private void validateSale(Sale sale) {
		if (sale.getConsumerName() == null || StringUtils.isEmptyOrWhitespaceOnly(sale.getConsumerName())) {
			throw new UserAuthenticationException("Please fill consumer name!");
		}
		if (sale.getConsumerMobile() == null || StringUtils.isEmptyOrWhitespaceOnly(sale.getConsumerMobile())) {
			throw new UserAuthenticationException("Please fill consumer mobile number!");
		}
		if (sale.getConsumerMobile().length() != 10) {
			throw new UserAuthenticationException("Invalid mobile length!");
		}
		if (sale.getTotal() == null || sale.getTotal().doubleValue() == 0) {
			throw new UserAuthenticationException("Invalid total!");
		}

		if (sale.getIsCash() == null) {
			throw new UserAuthenticationException("Please select payment mode!");
		}
//------------------------------------------------------------------------------------------------

		if (sale.getSaleDatas() == null || sale.getSaleDatas().size() == 0) {
			throw new UserAuthenticationException("Please select some product");
		}

		List<Long> productIds = new ArrayList<>();
		for (SaleData saleData : sale.getSaleDatas()) {

			if (saleData.getProductBarcodes() == null || saleData.getProductBarcodes().size() == 0) {
				throw new UserAuthenticationException("Please select an item for selected product!");
			}
			if (productIds.contains(saleData.getProduct().getId())) {
				throw new UserAuthenticationException("Produt Repeated!");
			}
			validateProductAndBarcodes(sale, saleData);

			productIds.add(saleData.getProduct().getId());
		}
	}

	private void validateProductAndBarcodes(Sale sale, SaleData saleData) {

//		if (saleRepository.countByProduct(saleData.getProduct()) > 0) {
//		throw new UserAuthenticationException("This product had  already sold");
//	}

		/* validate product bar-codes */

		List<Long> productBarcodeIds = new ArrayList<>();

		for (ProductBarcode productBarcode : saleData.getProductBarcodes()) {

			if (productBarcodeIds.contains(productBarcode.getId())) {
				throw new UserAuthenticationException("Product item repeated!");
			}
//			if (saleRepository.countByProductBarcodes(productBarcode.getId()) > 0) {
//				throw new UserAuthenticationException("Product with this barcode had already sold");
//			}

			if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.ADMIN)) {

				Long countAdminStock = adminStockRepository.getCountOfAdminStockByProduct(saleData.getProduct().getId(),
						productBarcode.getId());
				if (countAdminStock == 1) {
					throw new UserAuthenticationException("Stock has no quantity for this product!");
				}
				AdminStock adminStock = adminStockRepository.getAdminStockByProductBarcode(productBarcode.getId(),
						saleData.getProduct().getId());

				if (adminStock != null) {
					if (adminStock.getIsSold() == true) {
						throw new UserAuthenticationException("Scanned item is already sold.");
					} else if (adminStock.getIsOutward() == true) {
						throw new UserAuthenticationException(
								"Scanned item is already outwarded to retailer from inventory!");
					}
				}

			} else if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.RETAILER)) {

				Long countOfRetailerStock = retailerStockRepository
						.getCountOfRetailerStockByProduct(saleData.getProduct().getId(), productBarcode.getId());
				if (countOfRetailerStock == 1) {
					throw new UserAuthenticationException("Stock has no quantity for this product!");
				}
				RetailerStock retailerStock = retailerStockRepository
						.getRetailerStockByProductBarcode(productBarcode.getId(), saleData.getProduct().getId());
				if (retailerStock != null) {
					if (retailerStock.getIsSold() == true) {
						throw new UserAuthenticationException("Scanned item is already sold.");
					} else if (retailerStock.getIsOutward() == true) {
						throw new UserAuthenticationException(
								"Scanned item is already outwarded to sub-retailer from inventory!");
					}
				}
			} else if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.SUB_RETAILER)) {

				Long retailerStock = subRetailerStockRepository
						.getCountOfSubRetailerStockByProduct(saleData.getProduct().getId(), productBarcode.getId());
				if (retailerStock == 1) {
					throw new UserAuthenticationException("Stock has no quantity for this product!");
				}
				SubRetailerStock subRetailerStock = subRetailerStockRepository
						.getSubRetailerStockByProductBarcode(productBarcode.getId(), saleData.getProduct().getId());
				if (subRetailerStock != null) {
					if (subRetailerStock.getIsSold() == true) {
						throw new UserAuthenticationException("Scanned item is already sold.");
					}
				}

			}
			productBarcodeIds.add(productBarcode.getId());
		}

	}

	@Override
	public GenericResponseEntity addSale(Sale sale, User user, Org org) {
		sale.setOrg(org);
		validateSale(sale);
		String number = entityNumberUtil.generateSaleReferenceNumber(org);
		sale.setCreatedBy(user);
		sale.setSaleReferenceNumber(number);
		sale.setCreatedAt(new Date());
		setSalePriceInEachSoldBarcode(sale);
		sale = saleRepository.save(sale);
		setIsSoldInAdminStock(sale, user);
		return new GenericResponseEntity(201, "Success!");
	}

	private void setIsSoldInAdminStock(Sale sale, User user) {
		List<ProductBarcode> barcodeList = new ArrayList<>();
		for (SaleData saleData : sale.getSaleDatas()) {
			barcodeList.addAll(saleData.getProductBarcodes());
		}
		if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.ADMIN)) {
			List<AdminStock> adminStockList = adminStockRepository.findAllByProductBarcodeIn(barcodeList);
			for (AdminStock a : adminStockList) {
				a.setIsSold(true);
				a.setSaleDate(new Date());
				adminStockRepository.save(a);
			}
		} else if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.RETAILER)) {
			List<RetailerStock> retailerStockList = retailerStockRepository.findAllByProductBarcodeIn(barcodeList);
			for (RetailerStock r : retailerStockList) {
				r.setIsSold(true);
				r.setSaleDate(new Date());
				retailerStockRepository.save(r);
			}
		} else if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.SUB_RETAILER)) {
			List<SubRetailerStock> subRetailerStockList = subRetailerStockRepository
					.findAllByProductBarcodeIn(barcodeList);
			for (SubRetailerStock sr : subRetailerStockList) {
				sr.setIsSold(true);
				sr.setSaleDate(new Date());
				subRetailerStockRepository.save(sr);
			}
		}

	}

	private void setSalePriceInEachSoldBarcode(Sale sale) {

		if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.ADMIN)) {
			sale.setAdminRevenue(sale.getTotal());
			sale.setRetailerRevenue(BigDecimal.ZERO);
			sale.setSubRetailerRevenue(BigDecimal.ZERO);

		}
		if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.RETAILER)) {

			Float commission = sale.getOrg().getCommissionInPercent();
			if (commission != null) {
				BigDecimal total = sale.getTotal();
				BigDecimal commissionInPercent = new BigDecimal(sale.getOrg().getCommissionInPercent()); // Assuming
				BigDecimal revenue = commissionInPercent.divide(new BigDecimal("100")).multiply(total);
				sale.setAdminRevenue(total.subtract(revenue));
				sale.setRetailerRevenue(revenue);
				sale.setSubRetailerRevenue(BigDecimal.ZERO);
			} else {
				sale.setAdminRevenue(sale.getTotal());
				sale.setRetailerRevenue(BigDecimal.ZERO);
				sale.setSubRetailerRevenue(BigDecimal.ZERO);
			}
		}
		if (sale.getOrg().getOrgTypeEnum().equals(OrgTypeEnum.SUB_RETAILER)) {
			Float commission = sale.getOrg().getCommissionInPercent();
			if (commission != null) {
				BigDecimal total = sale.getTotal();
				BigDecimal commissionInPercent = new BigDecimal(sale.getOrg().getCommissionInPercent()); // Assuming
				BigDecimal revenue = commissionInPercent.divide(new BigDecimal("100")).multiply(total);
				/* TODO RETAILER's revenue */
				/* TODO SUB-RETAILER's revenue */

				sale.setSubRetailerRevenue(revenue);
			} else {
				sale.setAdminRevenue(sale.getTotal());
				sale.setRetailerRevenue(BigDecimal.ZERO);
				sale.setSubRetailerRevenue(BigDecimal.ZERO);
			}

		}

		List<SaleData> saleDataList = sale.getSaleDatas();
		for (SaleData sd : saleDataList) {
			ProductMaster productMaster = fetchProductMasterById(sd.getProduct().getId());
			sd.setSalePrice(productMaster.getSalePrice());
		}

	}

	@Override
	public List<Sale> fetchAllSales(Org org) {
		return saleRepository.findAllByOrg(org);
	}

	@Override
	public Page<Sale> getAllSales(int page, int size, Org org) {
		Pageable pageable = buildPagable(page, size);
		Page<Sale> sale = null;
		sale = saleRepository.findAllByOrg(pageable, org);
		return sale;

	}

	private Pageable buildPagable(int page, int size) {
		if (page < 0) {
			page = 0;
		}
		if (size < 0 || size > 100) {
			size = 25;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
		return pageable;
	}

	@Override
	public Sale fetchSaleById(Long id) {
		return getSaleById(id);
	}

	private Sale getSaleById(Long id) {
		Optional<Sale> saleOptional = saleRepository.findById(id);
		if (!saleOptional.isPresent()) {
			throw new UserAuthenticationException("Sale not found!");
		}
		return saleOptional.get();
	}

}
