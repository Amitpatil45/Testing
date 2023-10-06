package com.root32.dentalproductservice.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.root32.configsvc.util.MessageTemplateEnum;
import com.root32.dentalproductservice.exception.UserAuthenticationException;
import com.root32.dentalproductservice.repository.AdminStockInwardRepository;
import com.root32.dentalproductservice.repository.AdminStockRepository;
import com.root32.dentalproductservice.repository.BatchRepository;
import com.root32.dentalproductservice.repository.ManufacturerMasterRepository;
import com.root32.dentalproductservice.repository.MessageTemplateRepository;
import com.root32.dentalproductservice.repository.ProductBarcodeRepository;
import com.root32.dentalproductservice.repository.ProductMasterRepository;
import com.root32.dentalproductservice.repository.PurchaseOrderRepository;
import com.root32.dentalproductservice.service.PurchaseOrderService;
import com.root32.dto.AdminStockInwardDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.PdfAttachment;
import com.root32.dto.PoDto;
import com.root32.dto.TwoDatesDto;
import com.root32.entity.AdminStock;
import com.root32.entity.AdminStockInward;
import com.root32.entity.Batch;
import com.root32.entity.ManufacturerMaster;
import com.root32.entity.MessageTemplate;
import com.root32.entity.PoData;
import com.root32.entity.ProductBarcode;
import com.root32.entity.ProductMaster;
import com.root32.entity.PurchaseOrder;
import com.root32.entity.User;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private AdminStockInwardRepository adminStockInwardRepository;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private ManufacturerMasterRepository manufacturerMasterRepository;

	@Autowired
	private BatchRepository batchRepository;

	@Autowired
	private ProductBarcodeRepository productBarcodeRepository;

	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private AdminStockRepository adminStockRepository;

	@Autowired
	private BarcodePdfGenerate barcodePdfGenerate;

	@Autowired
	private EmailService emailService;

	@Autowired
	private MessageTemplateRepository messageTemplateRepo;

	@Override
	public GenericResponseEntity addPurchaseOrder(PurchaseOrder purchaseOrder)
			throws MessagingException, DocumentException, MalformedURLException, IOException {

		validatePurchaseOrder(purchaseOrder);

		List<Long> productIds = new ArrayList<>();
		for (PoData poData : purchaseOrder.getPoData()) {
			validatePoData(poData);
			if (productIds.contains(poData.getProduct().getId())) {
				throw new UserAuthenticationException("Cannot repeat product!");
			}
			productIds.add(poData.getProduct().getId());
		}

		purchaseOrder.setPoCode(generatePOCode());
		purchaseOrder.setCreatedAt(new Date());

		purchaseOrder.setIsRecieved(false);
		purchaseOrder.setCreatedAt(new Date());
		PurchaseOrder purchaseOrderr = purchaseOrderRepository.save(purchaseOrder);

		List<Batch> batches = new ArrayList<>();

		int newOrderNumber = 1;
		for (PoData poData : purchaseOrder.getPoData()) {

			Batch batch = new Batch();
			ProductMaster product = productMasterRepository.findById(poData.getProduct().getId()).get();

			batch.setBatchCode(
					purchaseOrderr.getPoCode() + "BC" + generateBatchCode(newOrderNumber) + product.getProductCode());

			batch.setIsRecieved(false);
			batch.setBelongsToPo(purchaseOrderr);
			batch.setRecievedQantity((long) 0);
			batch.setProduct(poData.getProduct());
			batch.setQuantity(poData.getQuantity());
			batch.setRemainingQuantity(poData.getQuantity());
			batchRepository.save(batch);
			newOrderNumber++;
		}
		List<PdfAttachment> pdfAttachments = new ArrayList<>();
		List<Batch> batchess = batchRepository.findByBelongsToPo(purchaseOrderr);

		for (Batch batch : batchess) {
			List<ProductBarcode> productBarcodes = new ArrayList<>();

			for (int i = 1; i <= batch.getQuantity(); i++) {

				ProductBarcode barcode = new ProductBarcode();

				String batchcodeString = batch.getBatchCode();

				barcode.setProductBarcode(batchcodeString.substring(2, 6) + batchcodeString.substring(8, 11)
						+ batchcodeString.substring(13, 16) + generateProductBarCode(i));
				barcode.setIsRecieved(false);
				barcode.setBelongsToBatch(batch);
				productBarcodes.add(barcode);
			}

			// Create pdf and send mail
			Long productID = batch.getProduct().getId();
			Optional<ProductMaster> productDetailsOp = productMasterRepository.findById(productID);
			String productName = productDetailsOp.get().getName();

			String POCODE = purchaseOrder.getPoCode();
			Long NumbersOfbarcodes = batch.getQuantity();

			List<ProductBarcode> proProductBarcodes = productBarcodes;
			pdfAttachments.addAll(barcodePdfGenerate.generateProductBarCode(POCODE, productName, NumbersOfbarcodes,
					proProductBarcodes));
			productBarcodeRepository.saveAll(productBarcodes);

		}
		// send mail with multiple attachment
		Long mfgId = purchaseOrder.getManufacturer().getId();
		Optional<ManufacturerMaster> manufacturerMasterOptional = manufacturerMasterRepository.findById(mfgId);
		String manufectureEmail = manufacturerMasterOptional.get().getEmail();
		String to = manufectureEmail;
		String templateKey = MessageTemplateEnum.PURCHASE_ORDER.toString();
		MessageTemplate messageTemplate = messageTemplateRepo.findByTemplateKey(templateKey);
		String subject = " Purchase order and Barcode-PDF Attachment";
//		String text = "Please find the attached PDF file.";
		String text = String.format(messageTemplate.getTemplateValue());
		emailService.sendEmailWithAttachments(to, subject, text, pdfAttachments);

		return new GenericResponseEntity(201, "Purchase order created successfully!");
	}

	private String generateProductBarCode(int newOrderNumber) {
		String formattedNumber = String.format("%06d", newOrderNumber);
		return formattedNumber;
	}

	private String generateBatchCode(int newOrderNumber) {

		String formattedNumber = String.format("%03d", newOrderNumber);
		return formattedNumber;

	}

	private String generatePOCode() {
		// Fetch the latest order code from the database
		PurchaseOrder lastPurchaseOrder = purchaseOrderRepository.findTopByOrderByIdDesc();

		int newOrderNumber = 1; // Default starting order number if no previous orders
		if (lastPurchaseOrder != null) {
			String lastOrderCode = lastPurchaseOrder.getPoCode();
			String numericPart = lastOrderCode.substring(2); // Extract numeric part after "PO"
			newOrderNumber = Integer.parseInt(numericPart) + 1;
		}

		String formattedOrderNumber = String.format("%04d", newOrderNumber); // Format with leading zeros
		return "PO" + formattedOrderNumber;
	}

	private void validatePoData(PoData poData) {

		if (poData.getProduct() == null || poData.getProduct().getId() == null) {
			throw new UserAuthenticationException("Please select product!");
		}
		if (poData.getQuantity() == null || poData.getQuantity() <= 0) {
			throw new UserAuthenticationException("Please add some quantity!");
		}
	}

	private void validatePurchaseOrder(PurchaseOrder purchaseOrder) {

		// valid manufacturer
		if (purchaseOrder.getManufacturer() == null || purchaseOrder.getManufacturer().getId() == null) {
			throw new UserAuthenticationException("Please select manufacturer!");
		}
		Optional<ManufacturerMaster> manufacturerMasterOptional = manufacturerMasterRepository
				.findById(purchaseOrder.getManufacturer().getId());
		if (manufacturerMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Invalid manufacturer selected!");
		}

		// atleast one batch
		if (purchaseOrder.getPoData() == null || purchaseOrder.getPoData().size() == 0) {
			throw new UserAuthenticationException("Please select atleast one product!");
		}
		if (purchaseOrder.getExpectedDelivery() == null) {
			throw new UserAuthenticationException("Please select expected delivery date!");
		}

		if (purchaseOrder.getExpectedDelivery().before(new Date())) {
			throw new UserAuthenticationException("Please select valid date!");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	private PurchaseOrder fetchPurchaseOrderById(Long id) {
		Optional<PurchaseOrder> purchaseOrderOptional = purchaseOrderRepository.findById(id);
		if (purchaseOrderOptional.isEmpty()) {
			throw new UserAuthenticationException("Purchase order unavailable!");
		}
		return purchaseOrderOptional.get();
	}

	@Override
	public PurchaseOrder getPurchaseOrder(Long id) {

		return fetchPurchaseOrderById(id);
	}
	// -----------------------------------------------------------------------------------------------

	@Override
	public Page<PurchaseOrder> getAllPurchaseOrders(int page, int size) {
		Pageable pageable = buildPagable(page, size);
		Page<PurchaseOrder> purchaseOrder = null;

		purchaseOrder = purchaseOrderRepository.findAll(pageable);
		return purchaseOrder;
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
	// --------------------------------------------------------------------------------------------------------

	@Override
	public List<PurchaseOrder> getPurchaseOrderByIsRecieved(boolean isRecieved) {

		return purchaseOrderRepository.findAllByisRecieved(isRecieved);

	}
	// ------------------

	@Override
	public List<PoDto> getListOfPoDtoNotComplete() {

		List<PurchaseOrder> poList = purchaseOrderRepository.findAllByIsRecieved(false);
		List<PoDto> poDtoList = new ArrayList<>();
		for (PurchaseOrder purchaseOrder : poList) {
			PoDto poDto = new PoDto(purchaseOrder.getId(), purchaseOrder.getPoCode());
			poDtoList.add(poDto);
		}
		return poDtoList;
	}

	// --------------------------------------------------------------------------------------------------------
	@Override
	public List<Batch> getBatchesByPoCodeAndIsRecieved(String poCode) {

		Optional<PurchaseOrder> poOptional = purchaseOrderRepository.findByPoCode(poCode);
		if (poOptional.isEmpty()) {
			throw new UserAuthenticationException("PO not available!");
		}

		PurchaseOrder purchaseOrder = poOptional.get();
		Long poId = purchaseOrder.getId();
		return batchRepository.findAllByBelongsToPoIdAndIsRecieved(poId, false);

	}
	//////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public GenericResponseEntity addDatesIntoBatch(TwoDatesDto twoDatesDto, User user) {

		if (twoDatesDto.getBatchCode() == null || twoDatesDto.getBatchCode().isBlank()) {
			throw new UserAuthenticationException("Please select batch!");
		}
		if (twoDatesDto.getManufacturedDate() == null) {
			throw new UserAuthenticationException("Please select manufactured date!");
		}
		if (twoDatesDto.getExpiryDate() == null) {
			throw new UserAuthenticationException("Please select expiry date!");
		}

		if (twoDatesDto.getManufacturedDate().after(new Date())) {
			throw new UserAuthenticationException("Invalid manufactured date selected!");
		}
		if (twoDatesDto.getExpiryDate().before(new Date())) {
			throw new UserAuthenticationException("Invalid expiry date selected!");
		}
		Optional<Batch> batchOptional = batchRepository.findByBatchCode(twoDatesDto.getBatchCode());
		if (batchOptional.isEmpty()) {
			throw new UserAuthenticationException("Batch not available!");
		}
		Batch batch = batchOptional.get();
		batch.setManufacturedDate(twoDatesDto.getManufacturedDate());
		batch.setExpiryDate(twoDatesDto.getExpiryDate());
		batch.setUpdatedBy(user);
		batch.setUpdatedDate(new Date());
		batchRepository.save(batch);

		return new GenericResponseEntity(202, "Batch updated successfully!");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public GenericResponseEntity scanProductForBatchInward(String batchCode, String productBarcode, User user) {

		Optional<Batch> batchOptional = batchRepository.findByBatchCode(batchCode);

		if (batchOptional.isEmpty()) {
			throw new UserAuthenticationException("Invalid batch selected!");
		}

		Batch batch = batchOptional.get();

		if (batch.getManufacturedDate() == null || batch.getExpiryDate() == null) {
			throw new UserAuthenticationException("Please fill dates in the batch first!");
		}
		if (batch.getIsRecieved() == true) {
			throw new UserAuthenticationException("This batch already recieved!");
		}

		Optional<ProductBarcode> productBarcodeOptional = productBarcodeRepository.findByProductBarcode(productBarcode);

		if (productBarcodeOptional.isEmpty()) {
			throw new UserAuthenticationException("Product barcode not available!");
		}
		ProductBarcode productBarcodeDB = productBarcodeOptional.get();

		if (productBarcodeDB.getBelongsToBatch().getId() != batch.getId()) {
			throw new UserAuthenticationException("Product does not belong to this batch!");
		}

		if (productBarcodeDB.getIsRecieved() == true) {
			throw new UserAuthenticationException("This product already scanned!");
		}

		if (batch.getExpiryDate().before(new Date()) || batch.getExpiryDate().equals(new Date())) {
			throw new UserAuthenticationException("Product expired!");
		}

		productBarcodeDB.setIsRecieved(true);
		productBarcodeRepository.save(productBarcodeDB);
		batch.setRecievedQantity(batch.getRecievedQantity() + 1);
		batch.setRemainingQuantity(batch.getRemainingQuantity() - 1);
		batch.setUpdatedBy(user);
		batch.setUpdatedDate(new Date());
		if (batch.getQuantity() == batch.getRecievedQantity()) {
			batch.setIsRecieved(true);
			batch.setRecievedDate(new Date());
		}
		batchRepository.save(batch);

		PurchaseOrder purechaseOrder = fetchPurchaseOrderById(batch.getBelongsToPo().getId());

		Long countOfIncompleteBatches = batchRepository.countByIsRecievedAndBelongsToPoId(false,
				batch.getBelongsToPo().getId());
		if (countOfIncompleteBatches == 0) {
			purechaseOrder.setIsRecieved(true);
			purechaseOrder.setCompletedDate(new Date());
			purchaseOrderRepository.save(purechaseOrder);
		}

		ManufacturerMaster manufacturer = purechaseOrder.getManufacturer();

		Date createdAt = new Date();
		AdminStock adminStock = new AdminStock(manufacturer, batch.getProduct(), productBarcodeDB, user.getOrg(), batch,
				user, createdAt, false, null, false, null);

		adminStockRepository.save(adminStock);
		return new GenericResponseEntity(200, "Success!", batch.getId());
	}

	private Batch fetchBatchByBatchCode(String batchCode) {
		Optional<Batch> batchOptional = batchRepository.findByBatchCode(batchCode);

		if (batchOptional.isEmpty()) {
			throw new UserAuthenticationException("Invalid batch selected!");
		}
		return batchOptional.get();
	}

	@Override
	public Batch getBatchByBatchCode(String batchCode) {
		return fetchBatchByBatchCode(batchCode);
	}

	private Batch fetchBatchById(Long id) {
		Optional<Batch> batchOptional = batchRepository.findById(id);
		if (batchOptional.isEmpty()) {
			throw new UserAuthenticationException("Batch not available!");
		}
		return batchOptional.get();
	}

	private PurchaseOrder fetchPurchaseOrderByBatch(Batch batch) {
		Optional<PurchaseOrder> poOptional = purchaseOrderRepository.findById(batch.getBelongsToPo().getId());
		if (poOptional.isEmpty()) {
			throw new UserAuthenticationException("Purchase Order not available!");
		}
		return poOptional.get();
	}

	@Override
	public GenericResponseEntity saveAdminStockInward(AdminStockInwardDto adminStockInwardDto, User user) {

		if (adminStockInwardDto.getBatchId() == null) {
			throw new UserAuthenticationException("Batch id cannot be null!");
		}
		if (adminStockInwardDto.getQuantity() == null || adminStockInwardDto.getQuantity() <= 0) {
			throw new UserAuthenticationException("Quantiy cannot be null!");
		}

		AdminStockInward adminStockInward = new AdminStockInward();

		Batch batch = fetchBatchById(adminStockInwardDto.getBatchId());

		adminStockInward.setProduct(batch.getProduct());
		adminStockInward.setBatch(batch);
		adminStockInward.setCreatedBy(user);
		adminStockInward.setCreatedDate(new Date());
		adminStockInward.setQuantity(adminStockInwardDto.getQuantity());

		PurchaseOrder purchaseOrder = fetchPurchaseOrderByBatch(batch);
		adminStockInward.setManufacturer(purchaseOrder.getManufacturer());
		adminStockInwardRepository.save(adminStockInward);
		return new GenericResponseEntity(201, "Products inwarded successfully!");
	}

}
