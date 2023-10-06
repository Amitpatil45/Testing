package com.root32.dentalproductservice.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.root32.dto.GenericResponseEntity;
import com.root32.dto.InitiateSaleResponseDto;
import com.root32.entity.Org;
import com.root32.entity.Sale;
import com.root32.entity.User;

public interface SaleService {

	InitiateSaleResponseDto getinitiateSaleResponseDto(String productBarcode,Org org);

	GenericResponseEntity addSale(Sale sale, User user, Org org);

	Page<Sale> getAllSales(int page, int size, Org org);

	List<Sale> fetchAllSales(Org org);

	Sale fetchSaleById(Long id);

}
