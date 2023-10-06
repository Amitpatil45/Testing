package com.root32.dentalproductservice.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.itextpdf.text.DocumentException;
import com.root32.dentalproductservice.service.PurchaseOrderService;
import com.root32.dto.AdminStockInwardDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.PoDto;
import com.root32.dto.ProductInwardScanDto;
import com.root32.dto.TwoDatesDto;
import com.root32.entity.Batch;
import com.root32.entity.PurchaseOrder;
import com.root32.entity.User;

@RestController
@RequestMapping("/api/purchaseOrder")
public class PurchaseOrderController {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@PostMapping("/create")
	public ResponseEntity<GenericResponseEntity> addPurchaseOrder(HttpServletRequest httpServletRequest,
			@RequestBody PurchaseOrder purchaseOrder)
			throws MessagingException, DocumentException, MalformedURLException, IOException {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		purchaseOrder.setCreatedBy(user);
		GenericResponseEntity gre = purchaseOrderService.addPurchaseOrder(purchaseOrder);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public PurchaseOrder getPurchaseOrder(HttpServletRequest httpServletRequest, @PathVariable Long id) {
		return purchaseOrderService.getPurchaseOrder(id);
	}

	@GetMapping("/all-purchaseOrders")
	public Page<PurchaseOrder> getAllPurchaseOrders(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {
		return purchaseOrderService.getAllPurchaseOrders(page, size);
	}

	@GetMapping("/list") // OMKAR
	public List<PurchaseOrder> getPurchaseOrderByIsRecieved(HttpServletRequest httpServletRequest,
			@RequestParam(defaultValue = "true") boolean isRecieved) {
		return purchaseOrderService.getPurchaseOrderByIsRecieved(isRecieved);
	}
	// ------------------ GET List<PoDto>

	@GetMapping("/listPoDto")
	public List<PoDto> getListOfPoDtoNotComplete(HttpServletRequest httpServletRequest) {
		return purchaseOrderService.getListOfPoDtoNotComplete();
	}

	// --------------------------------------------------------------------------------------------------------------

	@GetMapping("/batches/{poCode}")
	public List<Batch> getBatchesByPoCodeAndIsRecieved(HttpServletRequest httpServletRequest,
			@PathVariable String poCode) {
		return purchaseOrderService.getBatchesByPoCodeAndIsRecieved(poCode);
	}

/////////////////////////////////////////////////PO DONE////////////////////////////////////////////////////////////////////////

	// API to add dates in Batch , while inwarding batch

	@PutMapping("/inward/updateBatch")
	public ResponseEntity<GenericResponseEntity> addDatesIntoBatch(HttpServletRequest httpServletRequest,
			@RequestBody TwoDatesDto twoDatesDto) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = purchaseOrderService.addDatesIntoBatch(twoDatesDto, user);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	// Inward Batch Scan Product API

	@PostMapping("/inward")
	public ResponseEntity<GenericResponseEntity> scanProductForBatchInward(HttpServletRequest httpServletRequest,
			@RequestBody ProductInwardScanDto productInwardScanDto) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		String batchCode = productInwardScanDto.getBatchCode();
		String productBarcode = productInwardScanDto.getProductBarcode();

		GenericResponseEntity gre = purchaseOrderService.scanProductForBatchInward(batchCode, productBarcode, user);
		return new ResponseEntity<>(gre, HttpStatus.OK);
	}

	@GetMapping("/batch/{batchCode}")
	public Batch getBatchByBatchCode(HttpServletRequest httpServletRequest, @PathVariable String batchCode) {
		return purchaseOrderService.getBatchByBatchCode(batchCode);
	}

	@PostMapping("/saveInward")
	public ResponseEntity<GenericResponseEntity> saveAdminStockInward(HttpServletRequest httpServletRequest,
			@RequestBody AdminStockInwardDto adminStockInwardDto) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = purchaseOrderService.saveAdminStockInward(adminStockInwardDto, user);

		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	
	
}
