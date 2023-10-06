package com.root32.dentalproductservice.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.root32.dto.CategoryMasterDto;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.ProductMasterDto;
import com.root32.entity.CategoryMaster;
import com.root32.entity.ManufacturerMaster;
import com.root32.entity.ProductMaster;
import com.root32.entity.UomMaster;
import com.root32.entity.User;

public interface MasterTableService {

	GenericResponseEntity addProductMaster(User user, ProductMasterDto productMaster) throws IOException;

	ProductMaster fetchProductMasterById(Long id);

	List<ProductMaster> getAllProductMaster();

	GenericResponseEntity updateProductMaster(Long id, User user, ProductMasterDto productMaster) throws IOException;

	GenericResponseEntity addCategoryMaster(User user, CategoryMasterDto categoryMaster) throws IOException;

	CategoryMaster fetchCategoryMasterById(Long id);

	List<CategoryMaster> getAllCategoryMaster();

	GenericResponseEntity categoryStatusChange(Long id, boolean status);

	GenericResponseEntity updateCategoryMaster(Long id, User user, CategoryMasterDto categoryMaster) throws IOException;

	GenericResponseEntity addUomMaster(UomMaster uomMaster);

	GenericResponseEntity updateUomMaster(Long id, UomMaster uomMaster);

	UomMaster fetchUomMasterById(Long id);

	List<UomMaster> getAllUomMaster();

	Page<UomMaster> getAllUomPagination(String searchItem, int page, int size);

	GenericResponseEntity changeProductStatus(Long id, Boolean status, User loginUser);

	GenericResponseEntity deleteProductMaster(Long id);

	GenericResponseEntity deleteCategoryMaster(Long id);

	GenericResponseEntity deleteUomMaster(Long id);

	GenericResponseEntity addManufacturerMaster(ManufacturerMaster manufacturerMaster);

	ManufacturerMaster fetchManufacturerMaster(Long id);

	List<ManufacturerMaster> getAllManufacturerMaster();

	Page<ManufacturerMaster> getAllManufacturerMasterPagination(String searchItem, int page, int size);

	GenericResponseEntity updateManufacturerMaster(Long id, ManufacturerMaster manufacturerMaster);

    GenericResponseEntity deleteProductImageByImageId(Long id, User loginUser,Long productId);

	Page<ProductMaster> getAllProducts(String searchItem, int page, int size);

	Page<CategoryMaster> getAllCategoriesPagination(String searchItem, int page, int size);


}