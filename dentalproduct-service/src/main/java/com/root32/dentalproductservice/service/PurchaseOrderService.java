package com.root32.dentalproductservice.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.data.domain.Page;

import com.itextpdf.text.DocumentException;
import com.root32.dto.AdminStockInwardDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.PoDto;
import com.root32.dto.TwoDatesDto;
import com.root32.entity.Batch;
import com.root32.entity.PurchaseOrder;
import com.root32.entity.User;

public interface PurchaseOrderService {

	GenericResponseEntity addPurchaseOrder(PurchaseOrder purchaseOrder) throws MessagingException, DocumentException, MalformedURLException, IOException;

	PurchaseOrder getPurchaseOrder(Long id);

	Page<PurchaseOrder> getAllPurchaseOrders(int page, int size);

	List<PurchaseOrder> getPurchaseOrderByIsRecieved(boolean isRecieved);

	List<Batch> getBatchesByPoCodeAndIsRecieved(String poCode);

//	List<Batch> getIncompleteBatchesByPoId(Long id);

	List<PoDto> getListOfPoDtoNotComplete();
/////////////////////////////////////////////////////////////////////////////////////////////////

	GenericResponseEntity addDatesIntoBatch(TwoDatesDto twoDatesDto,User user);

	GenericResponseEntity scanProductForBatchInward(String batchCode, String productBarcode,User user);

	Batch getBatchByBatchCode(String batchCode);

	GenericResponseEntity saveAdminStockInward(AdminStockInwardDto adminStockInwardDto, User user);

	
}
