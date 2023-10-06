package com.root32.dentalproductservice.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dentalproductservice.service.MasterTableService;
import com.root32.dto.CategoryMasterDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.ProductMasterDto;
import com.root32.entity.CategoryMaster;
import com.root32.entity.ManufacturerMaster;
import com.root32.entity.ProductMaster;
import com.root32.entity.UomMaster;
import com.root32.entity.User;

@RestController
@RequestMapping("/api/masterData")
public class MasterTableController {

	@Autowired
	private MasterTableService masterTableService;

	// --------------------------------------- ProductMaster APIs
	// --------------------------------------------------------
	@PostMapping("/product")
	public ResponseEntity<GenericResponseEntity> addProductMaster(HttpServletRequest httpServletRequest,
			@RequestBody ProductMasterDto productMaster) throws IOException {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);

		GenericResponseEntity gre = masterTableService.addProductMaster(user, productMaster);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);

	}

	@GetMapping("/product/{id}")
	public ProductMaster fetchProductMasterById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return masterTableService.fetchProductMasterById(id);
	}

	@GetMapping("/product")
	public List<ProductMaster> getAllProductMaster(HttpServletRequest httpServletRequest) {
		return masterTableService.getAllProductMaster();
	}

	@GetMapping("/all-products")
	public Page<ProductMaster> getAllproducts(HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String searchItem, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		return masterTableService.getAllProducts(searchItem, page, size);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<GenericResponseEntity> updateProductMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody ProductMasterDto productMaster) throws IOException {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		// productMaster.setUpdatedBy(user);

		GenericResponseEntity gre = masterTableService.updateProductMaster(id, user, productMaster);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteProductImageByImageId/productId/{productId}/imageId/{id}")
	public ResponseEntity<GenericResponseEntity> deleteProductImageByImageId(HttpServletRequest httpServletRequest,
			@PathVariable Long productId, @PathVariable Long id) {
		User loginUser = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = masterTableService.deleteProductImageByImageId(id, loginUser, productId);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@PutMapping("/productStatusChange/{id}")
	public ResponseEntity<GenericResponseEntity> changeProductStatus(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestParam Boolean status) {
		User loginUser = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		GenericResponseEntity gre = masterTableService.changeProductStatus(id, status, loginUser);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@DeleteMapping("product/{id}")
	public ResponseEntity<GenericResponseEntity> deleteProductMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id) {

		GenericResponseEntity gre = masterTableService.deleteProductMaster(id);
		return new ResponseEntity<>(gre, HttpStatus.OK);

	}

	// ------------------------------- CategoryMaster
	// APIs-----------------------------------------

	@PostMapping("/category")
	public ResponseEntity<GenericResponseEntity> addCategoryMaster(HttpServletRequest httpServletRequest,
			@RequestBody CategoryMasterDto categoryMaster) throws IOException {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		// categoryMaster.setCreatedBy(user);

		GenericResponseEntity gre = masterTableService.addCategoryMaster(user, categoryMaster);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);

	}

	@PutMapping("/category/{id}")
	public ResponseEntity<GenericResponseEntity> updateCategoyMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody CategoryMasterDto categoryMaster) throws IOException {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		// categoryMaster.setUpdatedBy(user);

		GenericResponseEntity gre = masterTableService.updateCategoryMaster(id, user, categoryMaster);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@GetMapping("/category/{id}")
	public CategoryMaster fetchCategoryMasterById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return masterTableService.fetchCategoryMasterById(id);
	}

	@GetMapping("/category")
	public List<CategoryMaster> getAllCategoryMaster(HttpServletRequest httpServletRequest) {
		return masterTableService.getAllCategoryMaster();
	}

	@PutMapping("/categoryStatusChange/{id}")
	public ResponseEntity<GenericResponseEntity> categoryStatusChange(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestParam boolean status) {

		GenericResponseEntity gre = masterTableService.categoryStatusChange(id, status);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@GetMapping("/all-categories")
	public Page<CategoryMaster> getAllCategoriesPagination(HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String searchItem, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		return masterTableService.getAllCategoriesPagination(searchItem, page, size);
	}

	@DeleteMapping("category/{id}")
	public ResponseEntity<GenericResponseEntity> deletecategoryMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id) {

		GenericResponseEntity gre = masterTableService.deleteCategoryMaster(id);
		return new ResponseEntity<>(gre, HttpStatus.OK);
	}

	// --------------------------------------- UomMaster
	// APIs------------------------------------------------

	@PostMapping("/uom")
	public ResponseEntity<GenericResponseEntity> addUomMaster(HttpServletRequest httpServletRequest,
			@RequestBody UomMaster uomMaster) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		uomMaster.setCreatedBy(user);

		GenericResponseEntity gre = masterTableService.addUomMaster(uomMaster);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);

	}

	@PutMapping("/uom/{id}")
	public ResponseEntity<GenericResponseEntity> updateUomMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody UomMaster uomMaster) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		uomMaster.setUpdatedBy(user);

		GenericResponseEntity gre = masterTableService.updateUomMaster(id, uomMaster);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@DeleteMapping("uom/{id}")
	public ResponseEntity<GenericResponseEntity> deleteUomMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id) {

		GenericResponseEntity gre = masterTableService.deleteUomMaster(id);
		return new ResponseEntity<>(gre, HttpStatus.OK);

	}

	@GetMapping("/uom/{id}")
	public UomMaster fetchUomMasterById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return masterTableService.fetchUomMasterById(id);
	}

	@GetMapping("/uom")
	public List<UomMaster> getAllUomMaster(HttpServletRequest httpServletRequest) {
		return masterTableService.getAllUomMaster();
	}

	@GetMapping("/all-uoms")
	public Page<UomMaster> getAllUomsPagination(HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String searchItem, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		return masterTableService.getAllUomPagination(searchItem, page, size);
	}
	// ----------------------------- Manufacturer Master ----------------------

	@PostMapping("/manufacturer")
	public ResponseEntity<GenericResponseEntity> addManufacturerMaster(HttpServletRequest httpServletRequest,
			@RequestBody ManufacturerMaster manufacturerMaster) {

		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		manufacturerMaster.setCreatedBy(user);

		GenericResponseEntity gre = masterTableService.addManufacturerMaster(manufacturerMaster);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);

	}

	@PutMapping("/manufacturer/{id}")
	public ResponseEntity<GenericResponseEntity> updatemanufacturerMaster(HttpServletRequest httpServletRequest,
			@PathVariable Long id, @RequestBody ManufacturerMaster manufacturerMaster) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		manufacturerMaster.setUpdatedBy(user);

		GenericResponseEntity gre = masterTableService.updateManufacturerMaster(id, manufacturerMaster);
		return new ResponseEntity<>(gre, HttpStatus.ACCEPTED);

	}

	@GetMapping("/manufacturer/{id}")
	public ManufacturerMaster fetchManufacturerMaster(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return masterTableService.fetchManufacturerMaster(id);
	}

	@GetMapping("/manufacturer")
	public List<ManufacturerMaster> getAllManufacturerMaster(HttpServletRequest httpServletRequest) {
		return masterTableService.getAllManufacturerMaster();
	}

	@GetMapping("/all-manufacturers")
	public Page<ManufacturerMaster> getAllManufacturerMasterPagination(HttpServletRequest httpServletRequest,
			@RequestParam(required = false) String searchItem, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		return masterTableService.getAllManufacturerMasterPagination(searchItem, page, size);
	}

}