package com.root32.dentalproductservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dentalproductservice.service.InventoryService;
import com.root32.dto.AdminInventoryDto;
import com.root32.dto.CaseCodeDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.RetailerInventoryDto;
import com.root32.dto.RetailerWiseStockClassDto;
import com.root32.entity.AdminCase;
import com.root32.entity.AdminStock;
import com.root32.entity.AdminStockInward;
import com.root32.entity.AdminStockOutward;
import com.root32.entity.Org;
import com.root32.entity.RequestStatus;
import com.root32.entity.RequestStock;
import com.root32.entity.RetailerCase;
import com.root32.entity.RetailerInward;
import com.root32.entity.RetailerStockOutward;
import com.root32.entity.RetailerStockOutwardData;
import com.root32.entity.User;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@GetMapping("/admin")
	public Page<AdminInventoryDto> getAdminInventoryPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getAdminInventoryPageable(user, page, size, search);

	}

	@GetMapping("/adminStockList")
	public List<AdminInventoryDto> getAdminInventoryList(HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getAdminInventoryList(user);
	}

	// UNUSED
	@GetMapping("/admin/all-inward")
	public Page<AdminStock> getAllAdminStockInwardPagination(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getAllAdminStockInwardPagination(user, page, size);
	}
	// ----------------------
	// RequestStock APIS

	@PostMapping("/request")
	public ResponseEntity<GenericResponseEntity> createRequestStock(HttpServletRequest httpServletRequest,
			@RequestBody RequestStock requestStock) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		requestStock.setCreatedBy(user);
		requestStock.setFromOrg(org);

		GenericResponseEntity gre = inventoryService.createRequestStock(requestStock);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	// sent requests
	@GetMapping("/sentRequests")
	public Page<RequestStock> getSentRequestStockPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search, @RequestParam(required = false) RequestStatus status) {

		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getSentRequestStockPageable(org, page, size, search, status);
	}

	@GetMapping("/sentRequestsList")
	List<RequestStock> getSentRequestStockList(HttpServletRequest httpServletRequest) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getSentRequestStockList(org);
	}

	// recieved requests
	@GetMapping("/recievedRequests")
	public Page<RequestStock> getRecievedRequestStockPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search, @RequestParam(required = false) RequestStatus status) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getRecievedRequestStockPageable(org, page, size, search, status);

	}

	@GetMapping("/recievedRequestsList")
	public List<RequestStock> getRecievedRequestStockList(HttpServletRequest httpServletRequest) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getRecievedRequestStockList(org);
	}

	@PutMapping("/changeStatus/{id}/{status}")
	public ResponseEntity<GenericResponseEntity> changeRequestStatus(HttpServletRequest HttpServletRequest,
			@PathVariable Long id, @PathVariable RequestStatus status) {
		GenericResponseEntity gre = inventoryService.changeRequestStatus(id, status);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@GetMapping("/request/{id}")
	public RequestStock getRequestStockById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
		return inventoryService.getRequestStockById(id);
	}

	@GetMapping("/case/{id}")
	public AdminCase getCaseById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return inventoryService.getCaseById(id);
	}

	@GetMapping("/caseList")
	public List<AdminCase> getUnpackedCases(HttpServletRequest httpServletRequest) {
		return inventoryService.getUnsealedCases();
	}

	@GetMapping("/caseByCaseCode/{caseCode}")
	public AdminCase getAdminCaseByCaseCode(HttpServletRequest httpServletRequest, @PathVariable String caseCode) {
		return inventoryService.getAdminCaseByCaseCode(caseCode);
	}

	// -------------------- AdminStockOutward --------------

	@PostMapping("/case")
	public ResponseEntity<byte[]> generateBarcodePdf(HttpServletRequest httpServletRequest) {
		try {
			User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

			byte[] pdfBytes = inventoryService.gererateCaseCode(user);

			return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_PDF)
					.header("Content-Disposition", "attachment; filename=barcodes.pdf").body(pdfBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	// -------------------- Seal AdminStockOutward --------------

	@PostMapping("/admin/outward")
	public ResponseEntity<GenericResponseEntity> sealAdminStockOutward(HttpServletRequest httpServletRequest,
			@RequestBody AdminStockOutward adminStockOutward) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = inventoryService.sealAdminStockOutward(adminStockOutward, user);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("/admin/outward/{id}")
	public AdminStockOutward getAdminStockOutwardById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
		return inventoryService.getAdminStockOutwardById(id);
	}

	@GetMapping("/ratailer/getCaseDetails/{caseCode}")
	public AdminStockOutward caseDetailsByCaseCode(HttpServletRequest httpServletRequest,
			@PathVariable String caseCode) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.caseDetailsByCaseCode(caseCode, org);

	}

	@GetMapping("/admin/all-outward")
	public Page<AdminStockOutward> getAdminStockOutwardPageable(HttpServletRequest httpServletRequest,

			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		return inventoryService.getAdminStockOutwardPageable(page, size, search);

	}

	@GetMapping("/admin/allOutwardList")
	public List<AdminStockOutward> getAdminStockOutwardlisting(HttpServletRequest httpServletRequest) {
		return inventoryService.getAdminStockOutwardlisting();
	}

	// Retailer scan case

	@PostMapping("/retailer/scanCase/{caseCode}")
	public ResponseEntity<GenericResponseEntity> scanCaseToInward(HttpServletRequest httpServletRequest,
			@PathVariable String caseCode) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		GenericResponseEntity gre = inventoryService.scanCaseToInward(caseCode, user, org);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("/retailer")
	public Page<RetailerInventoryDto> getRetailerInventoryPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getRetailerInventoryPageable(user, org, page, size, search);

	}

	@GetMapping("/retailerStockList")
	public List<RetailerInventoryDto> getRetailerInventoryList(HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return inventoryService.getRetailerInventoryList(user, org);
	}

	// --------------------------------------------------------------

	@GetMapping("/retailer/inwardList")
	public Page<RetailerInward> getRetailerInwardPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getRetailerInwardPageable(user, org, page, size, search);
	}

	@GetMapping("/retailer/inward/{id}")
	public RetailerInward getRetailerInwardById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
		return inventoryService.getRetailerInwardById(id);
	}

	@GetMapping("/retailer/allInwardList")
	public List<RetailerInward> getRetailerInwardList(HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getRetailerInwardList(user, org);
	}

	// ------ Retailer Wise Inventory --------

	@GetMapping("/admin/retailerWiseInventory")
	public Page<RetailerWiseStockClassDto> getRetailerWiseInventoryDtoPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getRetailerWiseInventoryDtoPageable(user, org, page, size, search);
	}

	// ---------------------------
	@GetMapping("/admin/all-inwards")
	public Page<AdminStockInward> getAdminStockInwardPageable(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size,
			@RequestParam(required = false) String search) {
		return inventoryService.getAdminStockInwardPageable(page, size, search);
	}

	@GetMapping("/admin/allInwardList")
	public List<AdminStockInward> getAdminStockInwardList(HttpServletRequest httpServletRequest) {
		return inventoryService.getAdminStockInwardList();
	}

//--------------------------------------- PHASE TWO --------------------------
	@PostMapping("/retailerCase")
	public ResponseEntity<byte[]> generateRetailerCases(HttpServletRequest httpServletRequest) {

		try {
			User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
			Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);

			byte[] pdfBytes = inventoryService.gererateRetailerCases(user, userOrg);

			return ResponseEntity.ok().contentType(org.springframework.http.MediaType.APPLICATION_PDF)
					.header("Content-Disposition", "attachment; filename=barcodes.pdf").body(pdfBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}

	}

	@GetMapping("/retailer/caseByCaseCode")
	public RetailerCase getRetailerCaseByCaseCode(HttpServletRequest httpServletRequest,
			@RequestBody CaseCodeDto caseCodeDto) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return inventoryService.getRetailerCaseByCaseCode(caseCodeDto, org);
	}

	@PostMapping("/retailer/outward")
	public ResponseEntity<GenericResponseEntity> sealRetailerStockOutward(HttpServletRequest httpServletRequest,
			@RequestBody RetailerStockOutward ratailerStockOutward) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org userOrg = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		GenericResponseEntity gre = inventoryService.sealRetailerStockOutward(ratailerStockOutward, user, userOrg);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@PostMapping("/subRetailer/scanCase")
	public ResponseEntity<GenericResponseEntity> scanCaseToInward(HttpServletRequest httpServletRequest,
			@RequestBody CaseCodeDto caseCodeDto) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		GenericResponseEntity gre = inventoryService.retailerScanCaseToInward(caseCodeDto, user, org);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}
	
	@GetMapping("/retailer/outword/{id}")
	public RetailerStockOutward getRetailerOutWordById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
		return inventoryService.getretailerStockOutwardById(id);
	}
	
	

}
