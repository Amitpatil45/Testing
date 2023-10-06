package com.root32.dentalproductservice.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysql.cj.util.StringUtils;
import com.root32.dentalproductservice.exception.UserAuthenticationException;
import com.root32.dentalproductservice.repository.AddressRepository;
import com.root32.dentalproductservice.repository.AdminStockRepository;
import com.root32.dentalproductservice.repository.BatchRepository;
import com.root32.dentalproductservice.repository.CategoryRepository;
import com.root32.dentalproductservice.repository.ManufacturerMasterRepository;
import com.root32.dentalproductservice.repository.OrgRepository;
import com.root32.dentalproductservice.repository.PoDataRepository;
import com.root32.dentalproductservice.repository.ProductImageRepository;
import com.root32.dentalproductservice.repository.ProductMasterRepository;
import com.root32.dentalproductservice.repository.UomMasterRepository;
import com.root32.dentalproductservice.repository.UserRepository;
import com.root32.dentalproductservice.service.MasterTableService;
import com.root32.dto.Base64Image;
import com.root32.dto.CategoryMasterDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.ProductMasterDto;
import com.root32.entity.Address;
import com.root32.entity.CategoryMaster;
import com.root32.entity.ManufacturerMaster;
import com.root32.entity.Org;
import com.root32.entity.ProductImage;
import com.root32.entity.ProductMaster;
import com.root32.entity.UomMaster;
import com.root32.entity.User;

@Service
public class MasterTableServiceImpl implements MasterTableService {

	@Value("${endpoint-url}")
	private String baseURL;

	private static final String ROOT_FOLDER_START_PRODUCT_IMAGES = "productImages";
	private static final String ROOT_FOLDER_START_CATEGORY_IMAGES = "categoryImages";

	@Autowired
	private PoDataRepository poDataRepository;

	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private ProductMasterRepository productMasterRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UomMasterRepository uomMasterRepository;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private ManufacturerMasterRepository manufacturerMasterRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AdminStockRepository adminStockRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Override
	public GenericResponseEntity addProductMaster(User user, ProductMasterDto productMaster) throws IOException {

		if (productMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(productMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter product name!");
		}
		productMaster.setName(productMaster.getName().trim());

		int dbCountByName = productMasterRepository.countByNameIgnoreCase(productMaster.getName());
		if (dbCountByName > 0) {
			throw new UserAuthenticationException("This product name is already used!");
		}

		validateProductMaster(productMaster);

		ProductMaster pm = new ProductMaster();

		String productCode = generaterandomProductCode();

		int countByPcode = productMasterRepository.countByProductCode(productCode);
		if (countByPcode > 0) {
			productCode = generaterandomProductCode();
		}

		Float discountZero = (float) 0.0;

		if (productMaster.getDiscount() == null) {
			productMaster.setDiscount(discountZero);
		}
		if (productMaster.getImages().size() == 0) {
			throw new UserAuthenticationException("Atleast one image is required.");
		}
		List<ProductImage> imageUrls = new ArrayList<>();

		// check first index is image or not
		Base64Image bImage = productMaster.getImages().get(0);
		String firstFileExtesntion = fileNameExtension(bImage.getFileName());

		if ("png".equalsIgnoreCase(firstFileExtesntion) || "jpg".equalsIgnoreCase(firstFileExtesntion)
				|| "jpeg".equalsIgnoreCase(firstFileExtesntion) || "webp".equalsIgnoreCase(firstFileExtesntion)) {
			String data = "true";
		} else {
			throw new UserAuthenticationException("First file image required from selected files.");
		}

		for (Base64Image i : productMaster.getImages()) {
			if (i.getBase64() == null || StringUtils.isEmptyOrWhitespaceOnly(i.getBase64()) && i.getFileName() == null
					|| StringUtils.isEmptyOrWhitespaceOnly(i.getFileName())) {
				throw new UserAuthenticationException("base64 And fileName both are required.");
			}

			String fileExtesntion = fileNameExtension(i.getFileName());
			if ("gif".equalsIgnoreCase(fileExtesntion) || "mp4".equalsIgnoreCase(fileExtesntion)
					|| "png".equalsIgnoreCase(fileExtesntion) || "jpg".equalsIgnoreCase(fileExtesntion)
					|| "jpeg".equalsIgnoreCase(fileExtesntion) || "webp".equalsIgnoreCase(fileExtesntion)) {
				ProductImage pI = new ProductImage();
				String imageUrl = storageService.uploadFile(i.getBase64(), i.getFileName(),
						ROOT_FOLDER_START_PRODUCT_IMAGES);
				pI.setUrl(imageUrl);
				imageUrls.add(pI);
			} else {
				throw new UserAuthenticationException("mp4, gif, png, jpg, jpeg, and webp file allow only.");
			}
		}

		pm.setImages(imageUrls);
		pm.setName(productMaster.getName());
		pm.setProductCode(productCode);
		pm.setCategory(productMaster.getCategory());
		pm.setDescription(productMaster.getDescription());
		pm.setHighlightText(productMaster.getHighlightText());
		pm.setUom(productMaster.getUom());
		pm.setSalePrice(productMaster.getSalePrice());
		pm.setRegularPrice(productMaster.getRegularPrice());
		pm.setDiscount(productMaster.getDiscount());
		pm.setWeight(productMaster.getWeight());
		pm.setCreatedBy(user);
		pm.setIsActive(true);
		pm.setCreatedAt(new Date());
		pm.setUpdatedBy(null);
		pm.setUpdatedAt(null);
		productMasterRepository.save(pm);
		// updateUserRelatedField(user);

		return new GenericResponseEntity(201, "Product added successfully!");
	}

	private String generaterandomProductCode() {
		Random random = new Random();
		String randomProductCode = "PC" + random.nextInt(999);
		return randomProductCode;
	}

	private void validateProductMaster(ProductMasterDto productMaster) {

		if (productMaster.getDescription() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(productMaster.getDescription())) {
			throw new UserAuthenticationException("Description cannot be null!");
		}

		if (productMaster.getCategory() == null || productMaster.getCategory().getId() == null) {
			throw new UserAuthenticationException("Category cannot be null!");
		}

		Optional<CategoryMaster> categoryMasterOptional = categoryRepository
				.findById(productMaster.getCategory().getId());
		if (categoryMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Invalid Category!");
		}

		// CategoryMaster catecoryMaster =
		// categoryRepository.findById(productMaster.getCategory().getId()).get();
		CategoryMaster catecoryMaster = categoryMasterOptional.get();

		Boolean isCategoryActive = catecoryMaster.getIsActive();

		if (isCategoryActive == false) {
			throw new UserAuthenticationException("This category is disabled! Please select another category.");
		}

		if (productMaster.getUom() == null || productMaster.getUom().getId() == null) {
			throw new UserAuthenticationException("Uom cannot be null!");

		}
		Optional<UomMaster> UomMasterOptional = uomMasterRepository.findById(productMaster.getUom().getId());
		if (UomMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Invalid Uom");
		}

		// if (productMaster.getThumbnailImage() == null
		// || StringUtils.isEmptyOrWhitespaceOnly(productMaster.getThumbnailImage())) {
		// throw new UserAuthenticationException("Thumbnail image cannot be null!");
		// }
		if (productMaster.getRegularPrice() == null || productMaster.getRegularPrice().doubleValue() <= 0) {
			throw new UserAuthenticationException("Regular price cannot be null!");
		}
		if (productMaster.getSalePrice() == null || productMaster.getSalePrice().doubleValue() <= 0) {
			throw new UserAuthenticationException("Sale price cannot be null!");
		}
		if (productMaster.getSalePrice().doubleValue() > productMaster.getRegularPrice().doubleValue()) {
			throw new UserAuthenticationException("Sale price cannot be greater than regular price!");
		}
		if (productMaster.getWeight() == null || productMaster.getWeight().doubleValue() <= 0) {
			throw new UserAuthenticationException("Weight cannot be null!");
		}
		if (productMaster.getDiscount() != null && productMaster.getDiscount().doubleValue() < 0) {
			throw new UserAuthenticationException("Invalid Discount!");
		}
	}

	@Override
	public GenericResponseEntity changeProductStatus(Long id, Boolean status, User loginUser) {

		ProductMaster productMaster = getProductMasterById(id);
		productMaster.setIsActive(status);
		productMaster.setUpdatedBy(loginUser);
		productMaster.setUpdatedAt(new Date());
		productMasterRepository.save(productMaster);
		updateUserRelatedField(productMaster.getUpdatedBy());
		return new GenericResponseEntity(202, "Product status changed successfully");
	}

	private ProductMaster getProductMasterById(Long id) {
		Optional<ProductMaster> productMasterOptional = productMasterRepository.findById(id);
		if (!productMasterOptional.isPresent()) {
			throw new UserAuthenticationException("Product not found!");
		}
		return productMasterOptional.get();
	}

	@Override
	public ProductMaster fetchProductMasterById(Long id) {
		return getProductMasterById(id);
	}

	@Override
	public List<ProductMaster> getAllProductMaster() {

		return productMasterRepository.findAllByIsActive(true);
	}

	@Override
	public GenericResponseEntity updateProductMaster(Long id, User user, ProductMasterDto productMaster)
			throws IOException {
		ProductMaster productMasterDB = getProductMasterById(id);

		if (productMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(productMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter product name!");
		}
		productMaster.setName(productMaster.getName().trim());

		if (!productMasterDB.getName().equalsIgnoreCase(productMaster.getName())) {
			int dbCountByName = productMasterRepository.countByName(productMaster.getName());
			if (!productMasterDB.getName().equals(productMaster.getName())) {
				if (dbCountByName > 0) {
					throw new UserAuthenticationException("Product with this name already exists!");
				}
			}
		}

		validateProductMaster(productMaster);
		productMasterDB.setName(productMaster.getName());

		productMasterDB.setHighlightText(productMaster.getHighlightText());
		productMasterDB.setDescription(productMaster.getDescription());
		productMasterDB.setCategory(productMaster.getCategory());
		// productMasterDB.setThumbnailImage(productMaster.getThumbnailImage());
		productMasterDB.setRegularPrice(productMaster.getRegularPrice());
		productMasterDB.setSalePrice(productMaster.getSalePrice());

		Float discountZero = (float) 0.0;

		if (productMaster.getDiscount() == null) {
			productMaster.setDiscount(discountZero);
		}

		if (!productMaster.getImages().isEmpty()) {

			// check first index is image or not
			// Base64Image bImage = productMaster.getImages().get(0);
			// String firstFileExtesntion = fileNameExtension(bImage.getFileName());
			// if ("png".equalsIgnoreCase(firstFileExtesntion) ||
			// "jpg".equalsIgnoreCase(firstFileExtesntion)
			// || "jpeg".equalsIgnoreCase(firstFileExtesntion)
			// || "webp".equalsIgnoreCase(firstFileExtesntion)) {
			// String data = "true";
			// } else {
			// throw new UserAuthenticationException("First file image required from
			// selected files.");
			// }

			// List<ProductImage> pmObject = productMasterDB.getImages();
			// if (pmObject.size() != 0) {
			// for (ProductImage image : pmObject) {
			// String ImageURL = image.getUrl();
			// storageService.deleteFile(fileNameSplitbaseURL(ImageURL));
			// }

			// productMasterDB.getImages().clear();
			// productMasterDB.setImages(pmObject);
			// }

			List<ProductImage> imageUrls = new ArrayList<>();
			for (Base64Image i : productMaster.getImages()) {
				if (i.getBase64() == null
						|| StringUtils.isEmptyOrWhitespaceOnly(i.getBase64()) && i.getFileName() == null
						|| StringUtils.isEmptyOrWhitespaceOnly(i.getFileName())) {
					throw new UserAuthenticationException("base64 And fileName both are required.");
				}

				for (ProductImage oldUrls : productMasterDB.getImages()) {
					imageUrls.add(oldUrls);
				}
				String fileExtesntion = fileNameExtension(i.getFileName());
				if ("gif".equalsIgnoreCase(fileExtesntion) || "mp4".equalsIgnoreCase(fileExtesntion)
						|| "png".equalsIgnoreCase(fileExtesntion) || "jpg".equalsIgnoreCase(fileExtesntion)
						|| "jpeg".equalsIgnoreCase(fileExtesntion) || "webp".equalsIgnoreCase(fileExtesntion)) {
					ProductImage pI = new ProductImage();
					String imageUrl = storageService.uploadFile(i.getBase64(), i.getFileName(),
							ROOT_FOLDER_START_PRODUCT_IMAGES);
					pI.setUrl(imageUrl);
					imageUrls.add(pI);
				} else {
					throw new UserAuthenticationException("mp4, gif, png, jpg, jpeg, and webp file allow only.");
				}
			}

			if (productMasterDB.getImages().size() == 0) {
				if (productMaster.getImages().size() == 0) {
					throw new UserAuthenticationException("Atleast one image is required.");
				} else {

					// check first index is image or not
					Base64Image bImage = productMaster.getImages().get(0);
					String firstFileExtesntion = fileNameExtension(bImage.getFileName());
					if ("png".equalsIgnoreCase(firstFileExtesntion) || "jpg".equalsIgnoreCase(firstFileExtesntion)
							|| "jpeg".equalsIgnoreCase(firstFileExtesntion)
							|| "webp".equalsIgnoreCase(firstFileExtesntion)) {
						String data = "true";
					} else {
						throw new UserAuthenticationException("First file image required from selected files.");
					}
				}

			}

			productMasterDB.setImages(imageUrls);
		} else {
			List<ProductImage> pmObject = productMasterDB.getImages();
			if (productMasterDB.getImages().size() == 0) {
				if (productMaster.getImages().size() == 0) {
					throw new UserAuthenticationException("Atleast one image is required.");
				}
			}
			productMasterDB.setImages(pmObject);
		}

		productMasterDB.setDiscount(productMaster.getDiscount());
		productMasterDB.setWeight(productMaster.getWeight());

		productMasterDB.setUom(productMaster.getUom());
		productMasterDB.setUpdatedAt(new Date());

		productMasterDB.setUpdatedBy(user);
		updateUserRelatedField(user);
		productMasterRepository.save(productMasterDB);
		// updateUserRelatedField(productMasterDB.getUpdatedBy());
		return new GenericResponseEntity(202, "Product updated successfully!");
	}

	public String fileNameExtension(String fileName) {
		String[] parts = fileName.split("\\.");
		String lastPart = parts[parts.length - 1];
		return lastPart;
	}

	public String fileNameSplitbaseURL(String url) {
		String[] parts = url.split(baseURL);
		String lastPart = parts[parts.length - 1];
		return lastPart;
	}

	@Override
	public GenericResponseEntity deleteProductMaster(Long id) {

		ProductMaster productMaster = getProductMasterById(id);

		// need to validate where product master is used.

		Long countByAdminStock = adminStockRepository.countByProductId(id);

		Long countByBatch = batchRepository.countByProductId(id);
		Long countByPoData = poDataRepository.countByProductId(id);
		if (countByAdminStock > 0 || countByBatch > 0 || countByPoData > 0) {
			throw new UserAuthenticationException("This product is used cannot delete!");
		}

		productMasterRepository.delete(productMaster);
		return new GenericResponseEntity(200, "Product deleted successfully!");
	}

	@Override
	public Page<ProductMaster> getAllProducts(String searchItem, int page, int size) {

		Pageable pageable = buildPagable(page, size);
		Page<ProductMaster> productMaster = null;
//		productMaster = productMasterRepository.findAll(pageable);
		if (searchItem != null && !StringUtils.isEmptyOrWhitespaceOnly(searchItem)) {
			searchItem = searchItem.trim();
//			productMaster = productMasterRepository
//					.searchProductsByNameOrCategoryIgnoreCase("%" + searchItem.toLowerCase() + "%", pageable);
			
			productMaster = productMasterRepository.findAllByNameContainingOrCategoryNameContainingIgnoreCase(searchItem,
					searchItem, pageable);
			return productMaster;
		}
		productMaster = productMasterRepository.findAll(pageable);
		return productMaster;

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

	private String generaterandomCategoryCode() {
		Random random = new Random();
		String randomCategoryCode = "Category#" + random.nextInt(9999);
		return randomCategoryCode;
	}

	@Override
	public CategoryMaster fetchCategoryMasterById(Long id) {
		Optional<CategoryMaster> categoryOptioal = categoryRepository.findById(id);
		if (categoryOptioal.isEmpty()) {
			throw new UserAuthenticationException("Category not found.");
		}
		return categoryOptioal.get();
	}

	@Override
	public List<CategoryMaster> getAllCategoryMaster() {
		// return categoryRepository.findAll();
		return categoryRepository.findAllByIsActive(true);

	}

	@Override
	public GenericResponseEntity categoryStatusChange(Long id, boolean status) {
		Optional<CategoryMaster> categoryOptional = categoryRepository.findById(id);
		if (categoryOptional.isEmpty()) {
			throw new UserAuthenticationException("Category not found.");
		}
		CategoryMaster category = categoryOptional.get();

		if (status == false) {
			Long productsCount = productMasterRepository.countByCategoryId(id);
			if (productsCount == 0) {
				category.setIsActive(status);

			} else {
				throw new UserAuthenticationException("This category has products! Cannot disable!");
			}
		} else if (status == true) {
			category.setIsActive(status);
		}
		categoryRepository.save(category);
		return new GenericResponseEntity(202, "Category status updated successfully!");

	}

	@Override
	public GenericResponseEntity addCategoryMaster(User user, CategoryMasterDto categoryMaster) throws IOException {

		CategoryMaster cm = new CategoryMaster();
		if (categoryMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(categoryMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter category name!");
		}
		cm.setName(categoryMaster.getName().trim());

		int dbCountByName = categoryRepository.countByNameIgnoreCase(categoryMaster.getName());
		if (dbCountByName > 0) {
			throw new UserAuthenticationException("This category name is already used!");
		}

		String categoryCode = generaterandomCategoryCode();
		int countByCategorycode = categoryRepository.countByCategoryCode(categoryCode);
		if (countByCategorycode > 0) {
			categoryCode = generaterandomProductCode();
		}

		Base64Image imageCategory = categoryMaster.getImage();
		if (imageCategory.getBase64() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(imageCategory.getBase64()) && imageCategory.getFileName() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(imageCategory.getFileName())) {
			throw new UserAuthenticationException("base64 And fileName both are required.");
		}

		String fileExtesntion = fileNameExtension(imageCategory.getFileName());
		if ("png".equalsIgnoreCase(fileExtesntion) || "jpg".equalsIgnoreCase(fileExtesntion)
				|| "jpeg".equalsIgnoreCase(fileExtesntion) || "webp".equalsIgnoreCase(fileExtesntion)) {
			String imageUrl = storageService.uploadFile(imageCategory.getBase64(), imageCategory.getFileName(),
					ROOT_FOLDER_START_CATEGORY_IMAGES);
			cm.setImage(imageUrl);
		} else {
			throw new UserAuthenticationException("png, jpg, jpeg, and webp file allow only.");
		}

		if (categoryMaster.getDescription() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(categoryMaster.getDescription())) {
			cm.setDescription(null);
		} else {
			cm.setDescription(categoryMaster.getDescription());
		}

		cm.setCategoryCode(categoryCode);
		cm.setIsActive(true);
		cm.setCreatedAt(new Date());
		cm.setUpdatedBy(null);
		cm.setCreatedBy(user);
		cm.setUpdatedAt(null);
		categoryRepository.save(cm);
		updateUserRelatedField(user);
		return new GenericResponseEntity(201, "Category added successfully!");
	}

	@Override
	public GenericResponseEntity updateCategoryMaster(Long id, User user, CategoryMasterDto categoryMaster)
			throws IOException {

		Optional<CategoryMaster> categoryOptioal = categoryRepository.findById(id);
		if (categoryOptioal.isEmpty()) {
			throw new UserAuthenticationException("Category not found.");
		}

		CategoryMaster categoryMasterDB = categoryOptioal.get();

		if (categoryMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(categoryMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter product name!");
		}
		categoryMaster.setName(categoryMaster.getName().trim());

		if (!categoryMasterDB.getName().equalsIgnoreCase(categoryMaster.getName())) {
			int dbCountByName = categoryRepository.countByName(categoryMaster.getName());
			if (!categoryMasterDB.getName().equals(categoryMaster.getName())) {
				if (dbCountByName > 0) {
					throw new UserAuthenticationException("Product with this name already exists!");
				}
			}
		}
		Base64Image imageCategory = categoryMaster.getImage();
		if (imageCategory.getBase64() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(imageCategory.getBase64()) && imageCategory.getFileName() == null
				|| StringUtils.isEmptyOrWhitespaceOnly(imageCategory.getFileName())) {

			if (categoryMasterDB.getImage() != null) {
				categoryMasterDB.setImage(categoryMasterDB.getImage());
			} else {
				throw new UserAuthenticationException("base64 And fileName both are required.");
			}
		} else {

			String fileExtesntion = fileNameExtension(imageCategory.getFileName());
			if ("png".equalsIgnoreCase(fileExtesntion) || "jpg".equalsIgnoreCase(fileExtesntion)
					|| "jpeg".equalsIgnoreCase(fileExtesntion) || "webp".equalsIgnoreCase(fileExtesntion)) {

				if (categoryMasterDB.getImage() != null) {
					String ImageURL = categoryMasterDB.getImage();
					storageService.deleteFile(fileNameSplitbaseURL(ImageURL));
					categoryMasterDB.setImage(null);
				}
				String imageUrl = storageService.uploadFile(imageCategory.getBase64(), imageCategory.getFileName(),
						ROOT_FOLDER_START_CATEGORY_IMAGES);
				categoryMasterDB.setImage(imageUrl);
			} else {
				throw new UserAuthenticationException("png, jpg, jpeg, and webp file allow only.");
			}
		}

		categoryMasterDB.setName(categoryMaster.getName());
		categoryMasterDB.setDescription(categoryMaster.getDescription());
		categoryMasterDB.setIsActive(categoryMasterDB.getIsActive());
		categoryMasterDB.setUpdatedBy(user);
		categoryMasterDB.setUpdatedAt(new Date());
		categoryRepository.save(categoryMasterDB);

		updateUserRelatedField(user);
		return new GenericResponseEntity(202, "Category updated successfully.");

	}

	@Override
	public Page<CategoryMaster> getAllCategoriesPagination(String searchItem, int page, int size) {

//		Pageable pageable = buildPagable(page, size);
		Page<CategoryMaster> categoryMaster = null;
		Pageable pageable = buildPagable(page, size);
		if (searchItem != null && !StringUtils.isEmptyOrWhitespaceOnly(searchItem)) {
			searchItem = searchItem.trim();
			Page<CategoryMaster> category = categoryRepository
					.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchItem, searchItem, pageable);
			return category;
		}
		categoryMaster = categoryRepository.findAll(pageable);
		return categoryMaster;

	}

	@Override
	public GenericResponseEntity deleteCategoryMaster(Long id) {

		Optional<CategoryMaster> categoryMasterOptional = categoryRepository.findById(id);
		if (categoryMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Category not found!");
		}

		Long countByCategory = productMasterRepository.countByCategoryId(id);
		if (countByCategory > 0) {
			throw new UserAuthenticationException("Category has products! Cannot delete.");
		}

		categoryRepository.deleteById(id);

		return new GenericResponseEntity(200, "Category deleted successfully!");
	}

	// ----------------------------------- UomMster APIs
	// ----------------------------------------
	@Override
	public GenericResponseEntity addUomMaster(UomMaster uomMaster) {

		if (uomMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(uomMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter uom name!");
		}
		uomMaster.setName(uomMaster.getName().trim());

		int dbCountByName = uomMasterRepository.countByNameIgnoreCase(uomMaster.getName());
		if (dbCountByName > 0) {
			throw new UserAuthenticationException("This uom is already used!");
		}

		String uomCode = generaterandomUomCode();

		int countByUomcode = uomMasterRepository.countByUomCode(uomCode);
		if (countByUomcode > 0) {

			throw new UserAuthenticationException("Failed! Try again!");

		}

		uomMaster.setUomCode(uomCode);
		uomMaster.setCreatedAt(new Date());
		uomMaster.setUpdatedBy(null);
		uomMaster.setUpdatedAt(null);

		uomMasterRepository.save(uomMaster);
		updateUserRelatedField(uomMaster.getCreatedBy());
		return new GenericResponseEntity(201, "Uom added successfully!");
	}

	@Override
	public GenericResponseEntity updateUomMaster(Long id, UomMaster uomMaster) {

		Optional<UomMaster> uomOptional = uomMasterRepository.findById(id);
		if (uomOptional.isEmpty()) {
			throw new UserAuthenticationException("Uom not found.");
		}

		UomMaster uomMasterDB = uomOptional.get();

		if (uomMaster.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(uomMaster.getName().trim())) {
			throw new UserAuthenticationException("Please enter uom name!");
		}
		uomMaster.setName(uomMaster.getName().trim());

		if (!uomMasterDB.getName().equalsIgnoreCase(uomMaster.getName())) {
			int dbCountByName = uomMasterRepository.countByName(uomMaster.getName());
			if (!uomMasterDB.getName().equals(uomMaster.getName())) {
				if (dbCountByName > 0) {
					throw new UserAuthenticationException("Uom with this name already exists!");
				}
			}
		}

		// int dbCountByName = uomMasterRepository.countByName(uomMaster.getName());
		// if (!uomMasterDB.getName().equals(uomMaster.getName())) {
		// if (dbCountByName > 0) {
		// throw new UserAuthenticationException("Uom with this name already exists!");
		// }
		// }
		uomMasterDB.setName(uomMaster.getName());
		uomMasterDB.setUpdatedBy(uomMaster.getUpdatedBy());
		uomMasterDB.setUpdatedAt(new Date());
		uomMasterRepository.save(uomMasterDB);
		updateUserRelatedField(uomMasterDB.getUpdatedBy());
		return new GenericResponseEntity(202, "Uom updated successfully!");
	}

	private String generaterandomUomCode() {
		Random random = new Random();
		String randomUomCode = "UOM" + random.nextInt(9999);
		return randomUomCode;
	}

	@Override
	public UomMaster fetchUomMasterById(Long id) {

		Optional<UomMaster> uomOptional = uomMasterRepository.findById(id);
		if (uomOptional.isEmpty()) {
			throw new UserAuthenticationException("Uom not found.");
		}
		return uomOptional.get();
	}

	@Override
	public List<UomMaster> getAllUomMaster() {
		return uomMasterRepository.findAll();
	}

	@Override
	public Page<UomMaster> getAllUomPagination(String searchItem, int page, int size) {
		Pageable pageable = buildPagable(page, size);
		Page<UomMaster> uomMaster = null;
//		uomMaster = uomMasterRepository.findAll(pageable);
		if (searchItem != null && !StringUtils.isEmptyOrWhitespaceOnly(searchItem)) {
			searchItem = searchItem.trim();

			uomMaster = uomMasterRepository.findByNameContainingIgnoreCase(searchItem, pageable);
			if (uomMaster.isEmpty()) {
				uomMaster = uomMasterRepository.findByUomCodeContainingIgnoreCase(searchItem, pageable);

			}
			return uomMaster;
		}

		uomMaster = uomMasterRepository.findAll(pageable);
		return uomMaster;

	}

	@Override
	public GenericResponseEntity deleteUomMaster(Long id) {

		Optional<UomMaster> uomOptional = uomMasterRepository.findById(id);
		if (uomOptional.isEmpty()) {
			throw new UserAuthenticationException("Uom not found!");
		}

		Long countByUom = productMasterRepository.countByUomId(id);
		if (countByUom > 0) {
			throw new UserAuthenticationException("This uom is used in products! Cannot delete.");
		}
		uomMasterRepository.deleteById(id);

		return new GenericResponseEntity(200, "Uom deleted successfully!");
	}

	public User fetchUserById(Long id) {
		User user = userRepo.findById(id).orElse(null);
		if (user == null)
			return user;
		return user;
	}

	public void updateUserRelatedField(User user) {

		User userDB = fetchUserById(user.getId());
		userDB.setIsHavingAnyRecords(true);
		userRepo.save(userDB);
		Org orgDB = fetchOrgById(userDB.getOrg().getId());
		orgDB.setIsHavingRecord(true);
		orgRepository.save(orgDB);
	}

	private Org fetchOrgById(Long id) {
		Org orgDB = orgRepository.findById(id).orElse(null);
		if (orgDB == null) {
			throw new UserAuthenticationException("Org not available for this id");
		}
		return orgDB;
	}

	// ---------------------------------------manufacturer
	// API----------------------------------
	@Override
	public GenericResponseEntity addManufacturerMaster(ManufacturerMaster manufacturerMaster) {

		if (manufacturerMaster.getFullName() == null || manufacturerMaster.getFullName().isBlank()) {
			throw new UserAuthenticationException("Please fill fullname!");
		}
		manufacturerMaster.setFullName(manufacturerMaster.getFullName().trim());

		if (manufacturerMaster.getBusinessName() == null || manufacturerMaster.getBusinessName().isBlank()) {
			throw new UserAuthenticationException("Please fill bussiness name!");
		}
		manufacturerMaster.setBusinessName(manufacturerMaster.getBusinessName().trim());

		if (manufacturerMaster.getEmail() == null || manufacturerMaster.getEmail().isBlank()) {
			throw new UserAuthenticationException("Please fill email!");

		}
		if (!manufacturerMaster.getEmail().contains("@")) {
			throw new UserAuthenticationException("Invalid email address!");

		}
		manufacturerMaster.setEmail(manufacturerMaster.getEmail().trim());

		if (manufacturerMaster.getMobile() == null || manufacturerMaster.getMobile().isBlank()) {
			throw new UserAuthenticationException("Please fill mobile number!");

		}

		manufacturerMaster.setMobile(manufacturerMaster.getMobile().trim());

		if (manufacturerMaster.getMobile().length() != 10) {
			throw new UserAuthenticationException("Invalid mobile number length!");
		}
		int countByEmail = manufacturerMasterRepository.countByEmail(manufacturerMaster.getEmail());
		if (countByEmail > 0) {
			throw new UserAuthenticationException("Manufacturer with this email already exists!");
		}

		int countByMobile = manufacturerMasterRepository.countByMobile(manufacturerMaster.getMobile());
		if (countByMobile > 0) {
			throw new UserAuthenticationException("Manufacturer with this mobile already exists!");
		}

		if (manufacturerMaster.getAddress() == null) {
			throw new UserAuthenticationException("Please fill address!");
		}
		validateManufacturerAddress(manufacturerMaster.getAddress());

		Address address = addressRepository.save(manufacturerMaster.getAddress());

		manufacturerMaster.setAddress(address);

		manufacturerMaster.setIsActive(true);
		manufacturerMaster.setIsEmailVerified(true);
		manufacturerMaster.setIsMobileVerified(true);
		manufacturerMaster.setCreatedAt(new Date());

		manufacturerMasterRepository.save(manufacturerMaster);

		return new GenericResponseEntity(201, "Manufacturer created successfully!");
	}

	private void validateManufacturerAddress(Address address) {
		if (address.getCity() == null || address.getCity().isBlank()) {
			throw new UserAuthenticationException("Please fill city!");
		}
		if (address.getDistrict() == null || address.getDistrict().isBlank()) {
			throw new UserAuthenticationException("Please fill district!");
		}
		if (address.getState() == null || address.getState().isBlank()) {
			throw new UserAuthenticationException("Please fill state!");
		}
		if (address.getCountry() == null || address.getCountry().isBlank()) {
			throw new UserAuthenticationException("Please fill country!");
		}
		if (address.getPincode() == null) {
			throw new UserAuthenticationException("Please fill PIN code!");
		}
		// PIN code need to validate specifically-(length)

	}

	private ManufacturerMaster fetchManufacturerMasterById(Long id) {
		Optional<ManufacturerMaster> manufacturerMasterOptional = manufacturerMasterRepository.findById(id);
		if (manufacturerMasterOptional.isEmpty()) {
			throw new UserAuthenticationException("Manufacturer not found!");
		}
		return manufacturerMasterOptional.get();
	}

	@Override
	public ManufacturerMaster fetchManufacturerMaster(Long id) {

		return fetchManufacturerMasterById(id);
	}

	@Override
	public List<ManufacturerMaster> getAllManufacturerMaster() {
		return manufacturerMasterRepository.findAllByIsActive(true);
	}

	@Override
	public Page<ManufacturerMaster> getAllManufacturerMasterPagination(String searchItem, int page, int size) {
		Pageable pageable = buildPagable(page, size);
		Page<ManufacturerMaster> manufacturerMaster = null;
		
//			manufacturerMaster = manufacturerMasterRepository
//					.searchManufacurerByNameOrAddressIgnoreCase("%" + searchItem.toLowerCase() + "%", pageable);
		if (searchItem != null && !StringUtils.isEmptyOrWhitespaceOnly(searchItem)) {
			searchItem = searchItem.trim();
			manufacturerMaster = manufacturerMasterRepository.findAllByFullNameContainingIgnoreCase(searchItem,
					pageable);
			if (manufacturerMaster.isEmpty()) {

				manufacturerMaster = manufacturerMasterRepository.findAllByBusinessNameContainingIgnoreCase(searchItem,
						pageable);

			}
			if (manufacturerMaster.isEmpty()) {
				manufacturerMaster = manufacturerMasterRepository.findAllByAddressCityContainingIgnoreCase(searchItem,
						pageable);

			}
			return manufacturerMaster;
}
		manufacturerMaster = manufacturerMasterRepository.findAll(pageable);

		return manufacturerMaster;

	}

	@Override
	public GenericResponseEntity updateManufacturerMaster(Long id, ManufacturerMaster manufacturerMaster) {

		ManufacturerMaster manufacturerMasterDB = fetchManufacturerMasterById(id);

		if (manufacturerMaster.getFullName() == null || manufacturerMaster.getFullName().isBlank()) {
			throw new UserAuthenticationException("Please fill fullname!");
		}
		manufacturerMasterDB.setFullName(manufacturerMaster.getFullName().trim());

		if (manufacturerMaster.getBusinessName() == null || manufacturerMaster.getBusinessName().isBlank()) {
			throw new UserAuthenticationException("Please fill bussiness name!");
		}
		manufacturerMasterDB.setBusinessName(manufacturerMaster.getBusinessName().trim());

		if (manufacturerMaster.getEmail() == null || manufacturerMaster.getEmail().isBlank()) {
			throw new UserAuthenticationException("Please fill email!");

		}
		if (!manufacturerMaster.getEmail().contains("@")) {
			throw new UserAuthenticationException("Invalid email address!");
		}

		manufacturerMaster.setEmail(manufacturerMaster.getEmail().trim());

		if (!manufacturerMasterDB.getEmail().equals(manufacturerMaster.getEmail())) {

			int countByEmail = manufacturerMasterRepository.countByEmail(manufacturerMaster.getEmail());
			if (countByEmail > 0) {
				throw new UserAuthenticationException("Manufacturer with this email already exists!");
			}
		}
		manufacturerMasterDB.setEmail(manufacturerMaster.getEmail());

		if (manufacturerMaster.getMobile() == null || manufacturerMaster.getMobile().isBlank()) {
			throw new UserAuthenticationException("Please fill mobile number!");

		}
		manufacturerMaster.setMobile(manufacturerMaster.getMobile().trim());

		if (!manufacturerMasterDB.getMobile().equals(manufacturerMaster.getMobile())) {
			int countByMobile = manufacturerMasterRepository.countByMobile(manufacturerMaster.getMobile());
			if (countByMobile > 0) {
				throw new UserAuthenticationException("Manufacturer with this mobile already exists!");
			}
		}

		if (manufacturerMaster.getAddress() == null) {
			throw new UserAuthenticationException("Please fill address!");
		}

		validateManufacturerAddress(manufacturerMaster.getAddress());

		Address addressDB = manufacturerMasterDB.getAddress();
		Address address = manufacturerMaster.getAddress();

		addressDB.setApartmentNo(address.getApartmentNo());
		addressDB.setArea(address.getArea());
		addressDB.setCity(address.getCity());
		addressDB.setCountry(address.getCountry());
		addressDB.setDistrict(address.getDistrict());
		addressDB.setPlotNo(address.getPlotNo());
		addressDB.setLandmark(address.getLandmark());
		addressDB.setPincode(address.getPincode());

		manufacturerMasterDB.setAddress(addressDB);

		manufacturerMasterDB.setUpdatedBy(manufacturerMaster.getUpdatedBy());
		manufacturerMasterDB.setUpdatedAt(new Date());
		manufacturerMasterDB.setIsEmailVerified(true);
		manufacturerMasterDB.setIsMobileVerified(true);
		manufacturerMasterRepository.save(manufacturerMasterDB);

		return new GenericResponseEntity(202, "Manufacturer updated succesfully!");
	}

	@Override
	public GenericResponseEntity deleteProductImageByImageId(Long id, User loginUser, Long productId) {

		Optional<ProductMaster> productMaster = productMasterRepository.findById(productId);
		ProductMaster productMaster2 = productMaster.get();
		List<ProductImage> urlImages = productMaster2.getImages();
		for (ProductImage urlImage : urlImages) {
			if (urlImage.getId() == id) {
				storageService.deleteFile(fileNameSplitbaseURL(urlImage.getUrl()));
			}
		}

		Iterator<ProductImage> itr = productMaster2.getImages().iterator();
		boolean matchFound = false;
		while (itr.hasNext()) {

			ProductImage artifact = itr.next();
			if (artifact.getId() == id) {
				itr.remove();
				matchFound = true;
			}
		}
		if (matchFound) {
			productImageRepository.deleteById(id);
		}

		return new GenericResponseEntity(200, "Product image deleted successfully");
	}

}
