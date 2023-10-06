package com.root32.dentalproductservice.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.mysql.cj.util.StringUtils;
import com.root32.dentalproductservice.exception.UserAuthenticationException;
import com.root32.dentalproductservice.repository.AdminStockInwardRepository;
import com.root32.dentalproductservice.repository.AdminStockOutwardDataRepository;
import com.root32.dentalproductservice.repository.AdminStockOutwardRepository;
import com.root32.dentalproductservice.repository.AdminStockRepository;
import com.root32.dentalproductservice.repository.BatchRepository;
import com.root32.dentalproductservice.repository.CaseRepository;
import com.root32.dentalproductservice.repository.OrgRepository;
import com.root32.dentalproductservice.repository.ProductBarcodeRepository;
import com.root32.dentalproductservice.repository.ProductMasterRepository;
import com.root32.dentalproductservice.repository.RequestStockRepository;
import com.root32.dentalproductservice.repository.RetailerCaseRepository;
import com.root32.dentalproductservice.repository.RetailerInwardRepository;
import com.root32.dentalproductservice.repository.RetailerRepository;
import com.root32.dentalproductservice.repository.RetailerStockOutwardRepository;
import com.root32.dentalproductservice.repository.RetailerStockRepository;
import com.root32.dentalproductservice.repository.SubRetailerInwardRepository;
import com.root32.dentalproductservice.repository.SubRetailerRepository;
import com.root32.dentalproductservice.repository.SubRetailerStockRepository;
import com.root32.dentalproductservice.repository.UserRepository;
import com.root32.dentalproductservice.service.InventoryService;
import com.root32.dto.AdminInventoryDto;
import com.root32.dto.CaseCodeDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.ProductAndQuantityClassDto;
import com.root32.dto.RetailerInventoryDto;
import com.root32.dto.RetailerWiseInventoryDto;
import com.root32.dto.RetailerWiseStockClassDto;
import com.root32.entity.AdminCase;
import com.root32.entity.AdminStock;
import com.root32.entity.AdminStockInward;
import com.root32.entity.AdminStockOutward;
import com.root32.entity.AdminStockOutwardData;
import com.root32.entity.Batch;
import com.root32.entity.CaseStatus;
import com.root32.entity.Org;
import com.root32.entity.OrgTypeEnum;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.entity.RequestStatus;
import com.root32.entity.RequestStock;
import com.root32.entity.RequestStockData;
import com.root32.entity.Retailer;
import com.root32.entity.RetailerCase;
import com.root32.entity.RetailerInward;
import com.root32.entity.RetailerStock;
import com.root32.entity.RetailerStockOutward;
import com.root32.entity.RetailerStockOutwardData;
import com.root32.entity.SubRetailer;
import com.root32.entity.SubRetailerInward;
import com.root32.entity.SubRetailerStock;
import com.root32.entity.User;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private SubRetailerInwardRepository subRetailerInwardRepository;

	@Autowired
	private SubRetailerStockRepository subRetailerStockRepository;

	@Autowired
	private RetailerStockOutwardRepository retailerStockOutwardRepository;

	@Autowired
	private RetailerCaseRepository retailerCaseRepository;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ProductBarcodeRepository productBarcodeRepository;

	@Autowired
	private AdminStockInwardRepository adminStockInwardRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private RetailerInwardRepository retailerInwardRepository;

	@Autowired
	private RetailerStockRepository retailerStockRepository;

	@Autowired
	private AdminStockOutwardDataRepository adminStockOutwardDataRepository;

	@Autowired
	private AdminStockOutwardRepository adminStockOutwardRepository;

	@Autowired
	private CaseRepository caseRepository;

	@Autowired
	private SubRetailerRepository subRetailerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RetailerRepository retailerRepository;

	@Autowired
	private RequestStockRepository requestStockRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private AdminStockRepository adminStockRepository;

	@Autowired
	private BarcodePdfGenerate barcodePdfGenerate;

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
	public Page<AdminInventoryDto> getAdminInventoryPageable(User user, int page, int size, String search) {

		Pageable pageRequest = createPageRequestUsing(page, size);

		List<AdminInventoryDto> adminInventoryDtoList = new ArrayList<>();

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			adminInventoryDtoList = adminStockRepository.findProductWiseQuantityContainingSearch(search);
			return adminInventoryListToPage(pageRequest, adminInventoryDtoList);

		}

		adminInventoryDtoList = adminStockRepository.findProductWiseQuantityForInventory();
		return adminInventoryListToPage(pageRequest, adminInventoryDtoList);
	}

	private Page<AdminInventoryDto> adminInventoryListToPage(Pageable pageRequest,
			List<AdminInventoryDto> adminInventoryDtoList) {
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), adminInventoryDtoList.size());

		List<AdminInventoryDto> pageContent = adminInventoryDtoList.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, adminInventoryDtoList.size());
	}

	@Override
	public List<AdminInventoryDto> getAdminInventoryList(User user) {

		return adminStockRepository.findProductWiseQuantity();

	}

	@Override
	public Page<AdminStock> getAllAdminStockInwardPagination(User user, int page, int size) {
		Pageable pageable = buildPagable(page, size);
		Page<AdminStock> adminStock = null;

		adminStock = adminStockRepository.findAll(pageable);
		return adminStock;
	}

	// ------------------------- RequestStock APIs ---------------------------------
	@Override
	public GenericResponseEntity createRequestStock(RequestStock requestStock) {

		validateRequestStock(requestStock);

		if (requestStock.getFromOrg().getOrgTypeEnum() == OrgTypeEnum.RETAILER) {

			Retailer retailer = retailerRepository.findById(requestStock.getFromOrg().getId()).get();

			requestStock.setToOrg(retailer.getParent());

		}

		else if (requestStock.getFromOrg().getOrgTypeEnum() == OrgTypeEnum.SUB_RETAILER) {

			SubRetailer subRetailer = subRetailerRepository.findById(requestStock.getFromOrg().getId()).get();
			requestStock.setToOrg(subRetailer.getParent());
		}

		else {
			throw new UserAuthenticationException("To add stock visit purchase order screen!");
		}
		requestStock.setCreatedAt(new Date());
		requestStock.setRequestStatus(RequestStatus.INCOMPLETE);

		requestStockRepository.save(requestStock);

		return new GenericResponseEntity(201, "Request for stock created successfully!");
	}

	private void validateRequestStock(RequestStock requestStock) {
		if (requestStock.getRequestDatas() == null || requestStock.getRequestDatas().size() == 0) {
			throw new UserAuthenticationException("Please add some product and quantity!");
		}

		List<Long> productIds = new ArrayList<>();
		for (RequestStockData data : requestStock.getRequestDatas()) {
			if (data.getProduct() == null || data.getProduct().getId() == null) {
				throw new UserAuthenticationException("Please select product!");
			}

			if (productIds.contains(data.getProduct().getId())) {
				throw new UserAuthenticationException("Product repeated!");
			}

			if (data.getQuantity() == null || data.getQuantity() <= 0) {
				throw new UserAuthenticationException("Please add some quantity!");
			}
			productIds.add(data.getProduct().getId());
		}
	}

	@Override
	public Page<RequestStock> getSentRequestStockPageable(Org org, int page, int size, String search,
			RequestStatus status) {

		Pageable pageable = buildPagable(page, size);
		Page<RequestStock> requestStock = null;

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			requestStock = requestStockRepository.findAllDistinctByFromOrgAndRequestDatasProductNameContaining(pageable,
					org, search);

			return requestStock;
		}

		if (status != null) {
			requestStock = requestStockRepository.findAllByFromOrgAndRequestStatus(pageable, org, status);
			return requestStock;
		}

		requestStock = requestStockRepository.findAllByFromOrg(pageable, org);
		return requestStock;

	}

	@Override
	public List<RequestStock> getSentRequestStockList(Org org) {
		List<RequestStock> requestStocks = requestStockRepository.findAllByFromOrgId(org.getId());
		Collections.reverse(requestStocks);
		return requestStocks;
	}

	@Override
	public Page<RequestStock> getRecievedRequestStockPageable(Org org, int page, int size, String search,
			RequestStatus status) {
		Pageable pageable = buildPagable(page, size);
		Page<RequestStock> requestStock = null;

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			requestStock = requestStockRepository.findAllDistinctByToOrgAndRequestDatasProductNameContaining(pageable,
					org, search);

			if (requestStock.isEmpty()) {
				requestStock = requestStockRepository.findAllDistinctByToOrgAndFromOrgBusinessNameContaining(pageable,
						org, search);

			}
			return requestStock;
		}
		if (status != null) {
			requestStock = requestStockRepository.findAllByToOrgAndRequestStatus(pageable, org, status);
			return requestStock;

		}

		requestStock = requestStockRepository.findAllByToOrgId(org.getId(), pageable);
		return requestStock;
	}

	@Override
	public List<RequestStock> getRecievedRequestStockList(Org org) {

		List<RequestStock> requestStocks = requestStockRepository.findAllByToOrgId(org.getId());
		Collections.reverse(requestStocks);
		return requestStocks;
	}

	private RequestStock fetchRequestStockById(Long id) {
		Optional<RequestStock> requestStockOptional = requestStockRepository.findById(id);
		if (requestStockOptional.isEmpty()) {
			throw new UserAuthenticationException("Stock request not available!");
		}
		return requestStockOptional.get();
	}

	@Override
	public GenericResponseEntity changeRequestStatus(Long id, RequestStatus status) {

		RequestStock requestStockDB = fetchRequestStockById(id);

		requestStockDB.setRequestStatus(status);

		requestStockRepository.save(requestStockDB);
		return new GenericResponseEntity(202, "Request status changed successfully!");
	}

	@Override
	public RequestStock getRequestStockById(Long id) {

		return fetchRequestStockById(id);
	}

	@Override
	public byte[] gererateCaseCode(User user) throws MalformedURLException, DocumentException, IOException {
		List<AdminCase> adminCaseList = new ArrayList<>(30);
		for (int i = 0; i < 30; i++) {
			AdminCase ac = new AdminCase();
			ac.setCaseCode(generateAdminCaseCodes());
			ac.setCreatedAt(new Date());
			ac.setIsSealed(false);
			ac.setRecievedBy(null);

			ac.setCreatedBy(user);

			ac = caseRepository.save(ac);
			adminCaseList.add(ac);
		}
		String createdBy = user.getFirstName() + " " + user.getLastName();
		Date CreatedDate = new Date();

		byte[] pdfBytes = barcodePdfGenerate.generateBatchBarcodePdfDocument(adminCaseList, createdBy, CreatedDate);
		return pdfBytes;
	}

	private String generateAdminCaseCodes() {
		// Fetch the latest order code from the database
		AdminCase laseCase = caseRepository.findTopByOrderByIdDesc();

		long newOrderNumber = 1; // Default starting order number if no previous orders
		if (laseCase != null) {
			String lastCaseCode = laseCase.getCaseCode();
			String numericPart = lastCaseCode.substring(2); // Extract numeric part after "PO"
			newOrderNumber = Long.parseLong(numericPart) + 1;
		}

		String formattedOrderNumber = String.format("%012d", newOrderNumber); // Format with leading zeros
		return "AC" + formattedOrderNumber;
	}

	private AdminCase fetchCaseById(Long id) {
		Optional<AdminCase> caseOptional = caseRepository.findById(id);
		if (caseOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not available!");
		}
		AdminCase adminCase = caseOptional.get();
		if (adminCase.getIsSealed() == true) {
			throw new UserAuthenticationException("This case is already used!");
		}
		return adminCase;
	}

	@Override
	public AdminCase getCaseById(Long id) {
		return fetchCaseById(id);
	}

	@Override
	public List<AdminCase> getUnsealedCases() {
		return caseRepository.findAllByIsSealed(false);
	}

	// -----------------------------------------------------------------------------------
	@Override
	public GenericResponseEntity sealAdminStockOutward(AdminStockOutward adminStockOutward, User user) {

		validateAdminStockOutward(adminStockOutward);

		AdminCase cs = fetchCaseById(adminStockOutward.getAdminCase().getId());
		cs.setIsSealed(true);
		cs = caseRepository.save(cs);

		adminStockOutward.setAdminCase(cs);
		adminStockOutward.setCreatedAt(new Date());

		Long totalUnits = (long) 0;
		for (AdminStockOutwardData data : adminStockOutward.getDatas()) {

			for (ProductBarcode barcode : data.getProductBarcodes()) {
				AdminStock adminStock = fetchAdminStockByProductBarcodeId(barcode.getId());
				adminStock.setIsOutward(true);
				adminStock.setOutwardDate(new Date());
				adminStockRepository.save(adminStock);
			}
			data.setQuantity((long) data.getProductBarcodes().size());
			totalUnits = totalUnits + data.getQuantity();
		}

		adminStockOutward.setCaseStatus(CaseStatus.PENDING);
		adminStockOutward.setTotalUnits(totalUnits);
		adminStockOutward.setCreatedBy(user);
		adminStockOutward = adminStockOutwardRepository.save(adminStockOutward);
		return new GenericResponseEntity(201, "Stock outward created sucessfully!", adminStockOutward.getId());
	}

	private AdminStock fetchAdminStockByProductBarcodeId(Long id) {
		Optional<AdminStock> adminStockOptional = adminStockRepository.findByProductBarcodeId(id);
		if (adminStockOptional.isEmpty()) {
			throw new UserAuthenticationException("Product barcode not available in stock!");
		}
		return adminStockOptional.get();
	}

	private Org fetchOrgById(Long id) {
		Optional<Org> orgOptional = orgRepository.findById(id);
		if (orgOptional.isEmpty()) {
			throw new UserAuthenticationException("Organization not found!");
		}
		return orgOptional.get();
	}

	private SubRetailer fetchSubRetailerById(Long id) {
		Optional<SubRetailer> subRetailerOptional = subRetailerRepository.findById(id);
		if (subRetailerOptional.isEmpty()) {
			throw new UserAuthenticationException("Sub-retailer not available!");
		}
		return subRetailerOptional.get();
	}

	private void validateAdminStockOutward(AdminStockOutward adminStockOutward) {
		if (adminStockOutward.getRetailer() == null) {
			throw new UserAuthenticationException("Please select retailer!");
		}

		Org retailerOrg = fetchOrgById(adminStockOutward.getRetailer().getId());
		if (retailerOrg.getOrgTypeEnum() != OrgTypeEnum.RETAILER) {
			throw new UserAuthenticationException("Inappropriate organization selected!");
		}

		if (adminStockOutward.getAdminCase() == null || adminStockOutward.getAdminCase().getId() == null) {
			throw new UserAuthenticationException("Please select case!");
		}

		AdminCase cs = fetchCaseById(adminStockOutward.getAdminCase().getId());
		if (cs.getIsSealed() == true) {
			throw new UserAuthenticationException("Case is already sealed! Please select another case.");
		}
		if (adminStockOutward.getDatas() == null || adminStockOutward.getDatas().size() == 0) {
			throw new UserAuthenticationException("Please add some product and quantity!");
		}

		List<Long> productIds = new ArrayList<>();
		for (AdminStockOutwardData data : adminStockOutward.getDatas()) {
			if (data.getProduct() == null || data.getProduct().getId() == null) {
				throw new UserAuthenticationException("Please select a product!");
			}
			Long productId = data.getProduct().getId();
			if (data.getProductBarcodes() == null || data.getProductBarcodes().size() == 0) {
				throw new UserAuthenticationException("Please add some products for selected product!");
			}

			List<Long> barcodeIds = new ArrayList<>();
			for (ProductBarcode barcode : data.getProductBarcodes()) {

				if (barcode.getId() == null) {
					throw new UserAuthenticationException("Product barcode cannot be null!");
				}

				Long barcodeId = barcode.getId();
				if (barcodeIds.contains(barcodeId)) {
					throw new UserAuthenticationException("Product barcode repeated!");
				}

				AdminStock adminStockDB = fetchAdminStockByProductBarcodeId(barcode.getId());
				if (adminStockDB.getProduct().getId() != productId) {
					throw new UserAuthenticationException("Product type and product barcode mismatch!");
				}
				if (adminStockDB.getIsSold() == true) {
					throw new UserAuthenticationException("A product already sold!");
				}
				if (adminStockDB.getIsOutward() == true) {
					throw new UserAuthenticationException("A product already outwarded!");
				}
				if (!validateProductBarcodeExpirydate(barcodeId)) {
					throw new UserAuthenticationException("Product expired!");
				}

				barcodeIds.add(barcodeId);
			}

			productIds.add(productId);
		}

	}

	private Boolean validateProductBarcodeExpirydate(Long id) {
		ProductBarcode productBarcode = fetchProductBarcodeById(id);

		Batch batch = fetchBatchById(productBarcode.getBelongsToBatch().getId());

		if (batch.getExpiryDate().before(new Date()) || batch.getExpiryDate().equals(new Date())) {
			return false;
		}
		return true;
	}

	private Batch fetchBatchById(Long id) {
		Optional<Batch> batchOptional = batchRepository.findById(id);
		if (batchOptional.isEmpty()) {
			throw new UserAuthenticationException("Batch not available!");
		}
		return batchOptional.get();

	}

	private ProductBarcode fetchProductBarcodeById(Long id) {
		Optional<ProductBarcode> barcodeOptional = productBarcodeRepository.findById(id);
		if (barcodeOptional.isEmpty()) {
			throw new UserAuthenticationException("Product barcode not available!");
		}
		return barcodeOptional.get();
	}

	private AdminStockOutward fetchAdminStockOutwardById(Long id) {
		Optional<AdminStockOutward> adminStockOutwardOptional = adminStockOutwardRepository.findById(id);
		if (adminStockOutwardOptional.isEmpty()) {
			throw new UserAuthenticationException("Admin stock outward not found!");
		}

		return adminStockOutwardOptional.get();
	}

	@Override
	public AdminStockOutward getAdminStockOutwardById(Long id) {

		return fetchAdminStockOutwardById(id);
	}

	@Override
	public Page<AdminStockOutward> getAdminStockOutwardPageable(int page, int size, String search) {
		Pageable pageable = buildPagable(page, size);
		Page<AdminStockOutward> adminStockOutward = null;

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			adminStockOutward = adminStockOutwardRepository.findAllByRetailerBusinessNameContaining(pageable, search);

			if (adminStockOutward.isEmpty()) {
				adminStockOutward = adminStockOutwardRepository.findAllByAdminCaseCaseCodeContaining(pageable, search);
			}
			if (adminStockOutward.isEmpty()) {
				adminStockOutward = adminStockOutwardRepository.findAllDistinctByDatasProductNameContaining(pageable,
						search);
			}

			return adminStockOutward;
		}

		adminStockOutward = adminStockOutwardRepository.getAllAdminStock(pageable);
		return adminStockOutward;
	}

	@Override
	public List<AdminStockOutward> getAdminStockOutwardlisting() {
		List<AdminStockOutward> adminStockOutwards = adminStockOutwardRepository.findAll();

		Collections.reverse(adminStockOutwards);

		return adminStockOutwards;
	}

//-------------------------------------------------------------------------------------------
	@Override
	public GenericResponseEntity scanCaseToInward(String caseCode, User user, Org org) {

		// validations

		if (caseCode == null || StringUtils.isEmptyOrWhitespaceOnly(caseCode)) {
			throw new UserAuthenticationException("Please scan a case!");
		}
		Optional<AdminCase> csOptional = caseRepository.findByCaseCode(caseCode);
		if (csOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not found in system!");
		}

		AdminCase cs = csOptional.get();
		AdminStockOutward adminStockOutward = adminStockOutwardRepository.findByAdminCaseId(cs.getId());

		if (adminStockOutward.getRetailer().getId() != org.getId()) {

			throw new UserAuthenticationException("This case is not supposed to recieve by your organization!");
		}

		if (cs.getIsSealed() == false) {
			throw new UserAuthenticationException("This case is not sealed yet!");
		}

		if (adminStockOutward.getCaseStatus() == CaseStatus.COMPLETE) {
			throw new UserAuthenticationException("This case is already recieved!");
		}

		// Add stock in retailer inventory

		List<RetailerStock> retailerStockList = new ArrayList<>();

		for (AdminStockOutwardData data : adminStockOutward.getDatas()) {
			for (ProductBarcode barcode : data.getProductBarcodes()) {
				RetailerStock retailerStock = new RetailerStock();

				retailerStock.setProduct(data.getProduct());
				retailerStock.setBelongsToCase(cs);
				retailerStock.setIsSold(false);
				retailerStock.setOrg(org);
				retailerStock.setIsOutward(false);
				retailerStock.setProductBarcode(barcode);
				retailerStock.setCreatedAt(new Date());
				retailerStockList.add(retailerStock);
			}
		}

		adminStockOutward.setCaseStatus(CaseStatus.COMPLETE);
		adminStockOutward.setRecievedDate(new Date());
		adminStockOutwardRepository.save(adminStockOutward);
//		cs.setRecievedBy(user);
		caseRepository.save(cs);
		retailerStockRepository.saveAll(retailerStockList);

		RetailerInward retailerInward = new RetailerInward();
		retailerInward.setAdminCase(cs);
		retailerInward.setAdminStockOutward(adminStockOutward);
		retailerInward.setCreatedBy(user);
		retailerInward.setCreatedDate(new Date());
		retailerInward.setOrg(org);

		retailerInwardRepository.save(retailerInward);
		return new GenericResponseEntity(201, "Stock refilled successfully!");
	}

	private AdminCase fetchAdminCaseByCaseCode(String caseCode) {
		Optional<AdminCase> caseOptional = caseRepository.findByCaseCode(caseCode);
		if (caseOptional.isEmpty()) {
			throw new UserAuthenticationException("Case unavailable!");
		}
		return caseOptional.get();
	}

	@Override
	public AdminCase getAdminCaseByCaseCode(String caseCode) {
		return fetchAdminCaseByCaseCode(caseCode);
	}

	@Override
	public Page<RetailerInventoryDto> getRetailerInventoryPageable(User user, Org org, int page, int size,
			String search) {

		Pageable pageRequest = createPageRequestUsing(page, size);

		List<RetailerInventoryDto> retailerInventoryDtoList = new ArrayList<>();

		Long orgId = org.getId();

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			retailerInventoryDtoList = retailerStockRepository.findProductWiseQuantityContainingSearch(orgId, search);
//			return new PageImpl<>(retailerInventoryDtoList, pageRequest, retailerInventoryDtoList.size());
			return retailerInventoryListToPage(pageRequest, retailerInventoryDtoList);
		}

		retailerInventoryDtoList = retailerStockRepository.findProductWiseQuantityList(orgId);

		return retailerInventoryListToPage(pageRequest, retailerInventoryDtoList);

	}

	private Page<RetailerInventoryDto> retailerInventoryListToPage(Pageable pageRequest,
			List<RetailerInventoryDto> retailerInventoryDtoList) {
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), retailerInventoryDtoList.size());

		List<RetailerInventoryDto> pageContent = retailerInventoryDtoList.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, retailerInventoryDtoList.size());
	}

	@Override
	public List<RetailerInventoryDto> getRetailerInventoryList(User user, Org org) {
		Long orgId = org.getId();

		return retailerStockRepository.findProductWiseQuantity(orgId);
	}

	@Override
	public Page<RetailerInward> getRetailerInwardPageable(User user, Org org, int page, int size, String search) {

		Pageable pageable = buildPagable(page, size);
		Page<RetailerInward> retailerInward = null;

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			retailerInward = retailerInwardRepository.findAllByOrgAndAdminCaseCaseCodeContaining(pageable, org, search);

			if (retailerInward.isEmpty()) {
				retailerInward = retailerInwardRepository
						.findAllDistinctByOrgAndAdminStockOutward_Datas_Product_NameContaining(pageable, org, search);
			}

			return retailerInward;
		}

		retailerInward = retailerInwardRepository.findAllByOrg(pageable, org);
		return retailerInward;

	}

	@Override
	public List<RetailerInward> getRetailerInwardList(User user, Org org) {
		List<RetailerInward> retailerInwards = retailerInwardRepository.findAllByOrg(org);

		Collections.reverse(retailerInwards);
		return retailerInwards;
	}

	private Pageable createPageRequestUsing(int page, int size) {
		return PageRequest.of(page, size);
	}

	@Override
	public Page<RetailerWiseStockClassDto> getRetailerWiseInventoryDtoPageable(User user, Org org, int page, int size,
			String search) {

		List<RetailerWiseInventoryDto> allRetailerWiseInventoryDtoList = new ArrayList<>();

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			allRetailerWiseInventoryDtoList = retailerStockRepository
					.findRetailerWiseInventoryContainingBusinessName(search);
		} else {
			allRetailerWiseInventoryDtoList = retailerStockRepository.findRetailerWiseInventory();

		}

		Pageable pageRequest = createPageRequestUsing(page, size);

		Map<Long, RetailerWiseStockClassDto> orgProductInfoMap = new HashMap<>();

		int countofinteration = 0;
		for (RetailerWiseInventoryDto singleRecord : allRetailerWiseInventoryDtoList) {

			Long orgId = singleRecord.getOrgId();
			if (!orgProductInfoMap.containsKey(orgId)) {
				RetailerWiseStockClassDto retailerWiseDto = new RetailerWiseStockClassDto();

				Optional<Org> orgNew = orgRepository.findById(orgId);

				retailerWiseDto.setRetailer(orgNew.get());
				retailerWiseDto.setLastRefillDate(singleRecord.getLastRefillDate());
				retailerWiseDto.setLastSaleDate(singleRecord.getLastSaleDate());

				// Create a list of products for this orgId
				List<ProductAndQuantityClassDto> productsList = new ArrayList<>();
				ProductAndQuantityClassDto productDto = new ProductAndQuantityClassDto();
				Optional<ProductMaster> product = productMasterRepository.findById(singleRecord.getProductId());

				productDto.setProduct(product.get());
				productDto.setQuantity(singleRecord.getQuantity());

				// add non zero quantity products only
				if (productDto.getQuantity() != 0) {
					productsList.add(productDto);

				}

				// Set the list of products in the retailerWiseDto
				retailerWiseDto.setProducts(productsList);

				// Put the retailerWiseDto in the map
				orgProductInfoMap.put(orgId, retailerWiseDto);

			} else {
				// If orgId is already in the map, retrieve the existing retailerWiseDto
				RetailerWiseStockClassDto retailerWiseDto = orgProductInfoMap.get(orgId);

				// Create a new ProductAndQuantityClassDto for the current product
				ProductAndQuantityClassDto productDto = new ProductAndQuantityClassDto();
				Optional<ProductMaster> product = productMasterRepository.findById(singleRecord.getProductId());
				productDto.setProduct(product.get());
				productDto.setQuantity(singleRecord.getQuantity());

				// Add the productDto to the existing list of products
				// add non zero quantity products only
				if (productDto.getQuantity() != 0) {
					retailerWiseDto.getProducts().add(productDto);

				}
			}

		}

		List<RetailerWiseStockClassDto> finalList = new ArrayList(orgProductInfoMap.values());

		List<RetailerWiseStockClassDto> finalList1 = getListOdRWIDtos1(page, size, pageRequest, finalList);

		return new PageImpl<>(finalList1, pageRequest, finalList1.size());
//		return retailerWiseInventoryListToPage(pageRequest, finalList);

	}

	private Page<RetailerWiseStockClassDto> retailerWiseInventoryListToPage(Pageable pageRequest,
			List<RetailerWiseStockClassDto> finalList1) {
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), finalList1.size());

		List<RetailerWiseStockClassDto> pageContent = finalList1.subList(start, end);
		return new PageImpl<>(pageContent, pageRequest, finalList1.size());
	}

	private List<RetailerWiseStockClassDto> getListOdRWIDtos1(int page, int size, Pageable pageRequest,
			List<RetailerWiseStockClassDto> newClassDtoList) {
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), newClassDtoList.size());
		List<RetailerWiseStockClassDto> pageContent = newClassDtoList.subList(start, end);
		return pageContent;
	}

	@Override
	public Page<AdminStockInward> getAdminStockInwardPageable(int page, int size, String search) {
		Pageable pageable = buildPagable(page, size);
		Page<AdminStockInward> adminStockInward = null;

		if (search != null && !StringUtils.isEmptyOrWhitespaceOnly(search)) {
			search = search.trim();

			adminStockInward = adminStockInwardRepository.findAllByProductNameContaining(pageable, search);

			if (adminStockInward.isEmpty()) {
				adminStockInward = adminStockInwardRepository.findAllByBatchBatchCodeContaining(pageable, search);
			}
			if (adminStockInward.isEmpty()) {
				adminStockInward = adminStockInwardRepository.findAllByManufacturerBusinessNameContaining(pageable,
						search);
			}

			try {
				Long quantity = Long.parseLong(search);
				adminStockInward = adminStockInwardRepository.findAllByQuantity(pageable, quantity);
			} catch (NumberFormatException e) {
			}

			return adminStockInward;

		}

		adminStockInward = adminStockInwardRepository.findAll(pageable);
		return adminStockInward;
	}

	@Override
	public List<AdminStockInward> getAdminStockInwardList() {

		List<AdminStockInward> adminStockInwards = adminStockInwardRepository.findAll();
		Collections.reverse(adminStockInwards);

		return adminStockInwards;
	}

	@Override
	public AdminStockOutward caseDetailsByCaseCode(String caseCode, Org org) {

		if (caseCode == null || StringUtils.isEmptyOrWhitespaceOnly(caseCode)) {
			throw new UserAuthenticationException("Please scan a case!");
		}

		Optional<AdminStockOutward> adminStockOutwardOptional = adminStockOutwardRepository
				.findByAdminCaseCaseCode(caseCode);

		if (adminStockOutwardOptional.isEmpty()) {
			throw new UserAuthenticationException("No such case available!");
		}

		AdminStockOutward adminStockOutward = adminStockOutwardOptional.get();

		if (adminStockOutward.getRetailer().getId() != org.getId()) {
			throw new UserAuthenticationException("This case is not supposed to recieve by your organization!");
		}

		return adminStockOutward;
	}

	private RetailerInward fetchRetailerInwardById(Long id) {
		Optional<RetailerInward> retailerInwardOptional = retailerInwardRepository.findById(id);
		if (retailerInwardOptional.isEmpty()) {
			throw new UserAuthenticationException("Retailer inward not available!");
		}
		return retailerInwardOptional.get();
	}

	@Override
	public RetailerInward getRetailerInwardById(Long id) {

		return fetchRetailerInwardById(id);
	}

	@Override
	public byte[] gererateRetailerCases(User user, Org org)
			throws MalformedURLException, DocumentException, IOException {

		List<RetailerCase> retailerCaseList = new ArrayList<>();

		RetailerCase lastCase = retailerCaseRepository.findTopByOrgOrderByIdDesc(org);

		long newOrderNumber = 1; // Default starting order number if no previous orders

		if (lastCase != null) {
			String lastCaseCode = lastCase.getCode();
			String numericPart = lastCaseCode.substring(2); // Extract numeric part after "RC"
			newOrderNumber = Long.parseLong(numericPart) + 1;

		}

		for (int i = 0; i < 30; i++) {

			RetailerCase retailerCase = new RetailerCase();
			String formattedOrderNumber = String.format("%012d", newOrderNumber); // Format with leading zeros
			formattedOrderNumber = "RC" + formattedOrderNumber;

			retailerCase.setCode(formattedOrderNumber);
			retailerCase.setIsSealed(false);
			retailerCase.setCreatedBy(user);
			retailerCase.setCreatedAt(new Date());
			retailerCase.setOrg(org);

			retailerCaseList.add(retailerCase);
			newOrderNumber++;
		}

		retailerCaseRepository.saveAll(retailerCaseList);

		String createdBy = user.getFirstName() + " " + user.getLastName();
		Date CreatedDate = new Date();

		byte[] pdfBytes = barcodePdfGenerate.generateRetailerCaseBarcodePdfDocument(retailerCaseList, createdBy,
				CreatedDate);
		return pdfBytes;

	}

	@Override
	public RetailerCase getRetailerCaseByCaseCode(CaseCodeDto caseCodeDto, Org org) {

		if (caseCodeDto.getCode() == null || StringUtils.isEmptyOrWhitespaceOnly(caseCodeDto.getCode())) {
			throw new UserAuthenticationException("Please scan a case!");
		}
		Optional<RetailerCase> retailerCaseOptional = retailerCaseRepository.findByCodeAndOrg(caseCodeDto.getCode(),
				org);
		if (retailerCaseOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not available!");
		}

		return retailerCaseOptional.get();
	}

	private RetailerCase fetchRetailerCaseById(Long id) {
		Optional<RetailerCase> retailerCaseOptional = retailerCaseRepository.findById(id);
		if (retailerCaseOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not available!");
		}
		return retailerCaseOptional.get();
	}

	private void validateRetailerStockOutward(RetailerStockOutward retailerStockOutward, Org userOrg) {
		if (retailerStockOutward.getToSubRetailer() == null) {
			throw new UserAuthenticationException("Please select a sub-retailer!");
		}

		SubRetailer subRetailerOrg = fetchSubRetailerById(retailerStockOutward.getToSubRetailer().getId());
		if (subRetailerOrg.getOrgTypeEnum() != OrgTypeEnum.SUB_RETAILER) {
			throw new UserAuthenticationException("Inappropriate organization selected!");
		}

		if (userOrg.getId() != subRetailerOrg.getParent().getId()) {
			throw new UserAuthenticationException("This sub-retailer does not belong to your organization!");
		}

		if (retailerStockOutward.getRetailerCase() == null || retailerStockOutward.getRetailerCase().getId() == null) {
			throw new UserAuthenticationException("Please select case!");
		}

		RetailerCase cs = fetchRetailerCaseById(retailerStockOutward.getRetailerCase().getId());
		if (cs.getOrg().getId() != userOrg.getId()) {
			throw new UserAuthenticationException("This case does not belong to your organization!");
		}
		if (cs.getIsSealed() == true) {
			throw new UserAuthenticationException("Case is already sealed! Please select another case.");
		}
		if (retailerStockOutward.getDatas() == null || retailerStockOutward.getDatas().size() == 0) {
			throw new UserAuthenticationException("Please add some product and quantity!");
		}

		List<Long> productIds = new ArrayList<>();
		for (RetailerStockOutwardData data : retailerStockOutward.getDatas()) {
			if (data.getProduct() == null || data.getProduct().getId() == null) {
				throw new UserAuthenticationException("Please select a product!");
			}
			Long productId = data.getProduct().getId();
			if (data.getProductBarcodes() == null || data.getProductBarcodes().size() == 0) {
				throw new UserAuthenticationException("Please add some products for selected product!");
			}

			List<Long> barcodeIds = new ArrayList<>();
			for (ProductBarcode barcode : data.getProductBarcodes()) {

				if (barcode.getId() == null) {
					throw new UserAuthenticationException("Product barcode cannot be null!");
				}

				Long barcodeId = barcode.getId();
				if (barcodeIds.contains(barcodeId)) {
					throw new UserAuthenticationException("Product barcode repeated!");
				}

				RetailerStock retailerStockDB = fetchRetailerStockByProductBarcodeId(barcode.getId());
				if (retailerStockDB.getProduct().getId() != productId) {
					throw new UserAuthenticationException("Product type and product barcode mismatch!");
				}
				if (retailerStockDB.getIsSold() == true) {
					throw new UserAuthenticationException("A product already sold!");
				}
				if (retailerStockDB.getIsOutward() == true) {
					throw new UserAuthenticationException("A product already outwarded!");
				}
				if (!validateProductBarcodeExpirydate(barcodeId)) {
					throw new UserAuthenticationException("Product expired!");
				}

				barcodeIds.add(barcodeId);
			}

			productIds.add(productId);
		}

	}

	private RetailerStock fetchRetailerStockByProductBarcodeId(Long id) {
		Optional<RetailerStock> retailerStockOptional = retailerStockRepository.findByProductBarcodeId(id);
		if (retailerStockOptional.isEmpty()) {
			throw new UserAuthenticationException("Product not available in stock!");
		}
		return retailerStockOptional.get();
	}

	@Override
	public GenericResponseEntity sealRetailerStockOutward(RetailerStockOutward ratailerStockOutward, User user,
			Org userOrg) {

		validateRetailerStockOutward(ratailerStockOutward, userOrg);

		RetailerCase cs = fetchRetailerCaseById(ratailerStockOutward.getRetailerCase().getId());
		cs.setIsSealed(true);
		cs = retailerCaseRepository.save(cs);

		ratailerStockOutward.setRetailerCase(cs);
		ratailerStockOutward.setCreatedAt(new Date());

		Long totalUnits = (long) 0;
		for (RetailerStockOutwardData data : ratailerStockOutward.getDatas()) {

			for (ProductBarcode barcode : data.getProductBarcodes()) {
				RetailerStock retailerStock = fetchRetailerStockByProductBarcodeId(barcode.getId());
				retailerStock.setIsOutward(true);
				retailerStock.setOutwardDate(new Date());
				retailerStockRepository.save(retailerStock);
			}
			data.setQuantity((long) data.getProductBarcodes().size());
			totalUnits = totalUnits + data.getQuantity();
		}

		ratailerStockOutward.setCaseStatus(CaseStatus.PENDING);
		ratailerStockOutward.setTotalUnits(totalUnits);
		ratailerStockOutward.setCreatedBy(user);
		ratailerStockOutward = retailerStockOutwardRepository.save(ratailerStockOutward);
		return new GenericResponseEntity(201, "Stock outward created sucessfully!", ratailerStockOutward.getId());

	}

	private RetailerStockOutward fetchRetailerStockOutwardByCase(RetailerCase retailerCase) {
		Optional<RetailerStockOutward> retailerStockOutwardOptional = retailerStockOutwardRepository
				.findByRetailerCase(retailerCase);
		if (retailerStockOutwardOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not available!");
		}
		return retailerStockOutwardOptional.get();
	}

	@Override
	public GenericResponseEntity retailerScanCaseToInward(CaseCodeDto caseCodeDto, User user, Org org) {

		// validations
		SubRetailer sr = fetchSubRetailerById(org.getId());

		if (caseCodeDto.getCode() == null || StringUtils.isEmptyOrWhitespaceOnly(caseCodeDto.getCode())) {
			throw new UserAuthenticationException("Please scan a case!");
		}
		Optional<RetailerCase> rcOptional = retailerCaseRepository.findByCodeAndOrg(caseCodeDto.getCode(),
				sr.getParent());
		if (rcOptional.isEmpty()) {
			throw new UserAuthenticationException("Case not available in system!");
		}
		RetailerCase rc = rcOptional.get();
		if (rc.getIsSealed() == false) {
			throw new UserAuthenticationException("This case is not sealed yet!");
		}

		RetailerStockOutward retailerStockOutward = fetchRetailerStockOutwardByCase(rc);

		if (retailerStockOutward.getToSubRetailer().getId() != org.getId()) {
			throw new UserAuthenticationException("This case is not supposed to recieve by your organization");
		}

		if (retailerStockOutward.getCaseStatus() == CaseStatus.COMPLETE) {
			throw new UserAuthenticationException("This case is already recieved!");
		}

		// add stock in sub-retailer inventory

		List<SubRetailerStock> subRetailerStockList = new ArrayList<>();

		for (RetailerStockOutwardData data : retailerStockOutward.getDatas()) {
			for (ProductBarcode barcode : data.getProductBarcodes()) {
				SubRetailerStock subRetailerStock = new SubRetailerStock();

				subRetailerStock.setScannedBy(user);
				subRetailerStock.setProduct(data.getProduct());
				subRetailerStock.setRetailerCase(rc);
				subRetailerStock.setIsSold(false);
				subRetailerStock.setSubRetailer(sr);
				subRetailerStock.setProductBarcode(barcode);
				subRetailerStock.setCreatedAt(new Date());
				subRetailerStockList.add(subRetailerStock);
			}
		}

		retailerStockOutward.setCaseStatus(CaseStatus.COMPLETE);
		retailerStockOutward.setRecievedDate(new Date());
		retailerStockOutwardRepository.save(retailerStockOutward);

		subRetailerStockRepository.saveAll(subRetailerStockList);

		SubRetailerInward subRetailerInward = new SubRetailerInward();
		subRetailerInward.setRetailerCase(rc);
		subRetailerInward.setRetailerStockOutward(retailerStockOutward);
		subRetailerInward.setCreatedBy(user);
		subRetailerInward.setCreatedDate(new Date());
		subRetailerInward.setSubRetailer(sr);

		subRetailerInwardRepository.save(subRetailerInward);

		return new GenericResponseEntity(201, "Stock refilled successfully!");
	}

	
	private RetailerStockOutward fetchRetailerOutward(Long id){
		Optional<RetailerStockOutward> retailerOutwardOptional = retailerStockOutwardRepository.findById(id);
		if (retailerOutwardOptional.isEmpty()) {
			throw new UserAuthenticationException("Retailer OutWord not available!");
		}
		return retailerOutwardOptional.get();
	}
	

	@Override
	public RetailerStockOutward getretailerStockOutwardById(Long id) {
		return fetchRetailerOutward(id);
	}

	private SubRetailerInward fetchRetailerInword(Long Id) {
		Optional<SubRetailerInward> subRetailerInwordOptional = subRetailerRepository
		
		
		return null;
		
	}
	
	
	@Override
	public SubRetailer subRetailerById(Long Id) {
		
		return null;
	}

}
