package com.root32.dentalproductservice.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.itextpdf.text.DocumentException;
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
import com.root32.entity.SubRetailer;
import com.root32.entity.User;

public interface InventoryService {

	Page<AdminInventoryDto> getAdminInventoryPageable(User user, int page, int size, String search);

	GenericResponseEntity createRequestStock(RequestStock requestStock);

	Page<RequestStock> getSentRequestStockPageable(Org org, int page, int size, String search, RequestStatus status);

	Page<RequestStock> getRecievedRequestStockPageable(Org org, int page, int size, String search,
			RequestStatus status);

	GenericResponseEntity changeRequestStatus(Long id, RequestStatus status);

	RequestStock getRequestStockById(Long id);

	byte[] gererateCaseCode(User user) throws MalformedURLException, DocumentException, IOException;

	AdminCase getCaseById(Long id);

	List<AdminCase> getUnsealedCases();

	GenericResponseEntity sealAdminStockOutward(AdminStockOutward adminStockOutward, User user);

	AdminStockOutward getAdminStockOutwardById(Long id);

	Page<AdminStockOutward> getAdminStockOutwardPageable(int page, int size, String search);

	GenericResponseEntity scanCaseToInward(String caseCode, User user, Org org);

	AdminCase getAdminCaseByCaseCode(String caseCode);

	Page<RetailerInventoryDto> getRetailerInventoryPageable(User user, Org org, int page, int size, String search);

	Page<RetailerInward> getRetailerInwardPageable(User user, Org org, int page, int size, String search);

	Page<AdminStock> getAllAdminStockInwardPagination(User user, int page, int size);

	Page<RetailerWiseStockClassDto> getRetailerWiseInventoryDtoPageable(User user, Org org, int page, int size,
			String search);

	Page<AdminStockInward> getAdminStockInwardPageable(int page, int size, String search);

	List<AdminInventoryDto> getAdminInventoryList(User user);

	List<RetailerInventoryDto> getRetailerInventoryList(User user, Org org);

	List<AdminStockInward> getAdminStockInwardList();

	List<AdminStockOutward> getAdminStockOutwardlisting();

	List<RetailerInward> getRetailerInwardList(User user, Org org);

	List<RequestStock> getSentRequestStockList(Org org);

	List<RequestStock> getRecievedRequestStockList(Org org);

	AdminStockOutward caseDetailsByCaseCode(String caseCode, Org org);

	RetailerInward getRetailerInwardById(Long id);

	byte[] gererateRetailerCases(User user, Org org) throws MalformedURLException, DocumentException, IOException;

	RetailerCase getRetailerCaseByCaseCode(CaseCodeDto caseCodeDto, Org org);

	GenericResponseEntity sealRetailerStockOutward(RetailerStockOutward ratailerStockOutward, User user, Org userOrg);

	GenericResponseEntity retailerScanCaseToInward(CaseCodeDto caseCodeDto, User user, Org org);

	RetailerStockOutward getretailerStockOutwardById(Long id);
	
	SubRetailer subRetailerById(Long Id);
}
