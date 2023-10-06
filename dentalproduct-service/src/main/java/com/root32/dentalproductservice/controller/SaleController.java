package com.root32.dentalproductservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root32.dentalproductservice.service.SaleService;
import com.root32.dto.GenericResponseEntity;
import com.root32.dto.InitiateSaleResponseDto;
import com.root32.entity.Org;
import com.root32.entity.Sale;
import com.root32.entity.User;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

	@Autowired
	private SaleService saleService;

	@GetMapping("/admin/{productBarcode}")
	public InitiateSaleResponseDto getinitiateSaleResponseDto(HttpServletRequest httpServletRequest,
			@PathVariable String productBarcode) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return saleService.getinitiateSaleResponseDto(productBarcode,org);
	}

	@PostMapping("/admin")
	public ResponseEntity<GenericResponseEntity> addSale(HttpServletRequest httpServletRequest,
			@RequestBody Sale sale) {
		User user = (User) httpServletRequest.getAttribute(User.LOGIN_USER);
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		GenericResponseEntity gre = saleService.addSale(sale, user, org);
		return new ResponseEntity<>(gre, HttpStatus.CREATED);
	}

	@GetMapping("/saleList")
	public List<Sale> getAllProductMaster(HttpServletRequest httpServletRequest) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);

		return saleService.fetchAllSales(org);
	}

	@GetMapping("/all-sales")
	public Page<Sale> getAllproducts(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {
		Org org = (Org) httpServletRequest.getAttribute(User.USER_ORG);
		return saleService.getAllSales(page, size, org);
	}

	@GetMapping("/{id}")
	public Sale fetchSaleById(HttpServletRequest HttpServletRequest, @PathVariable Long id) {
		return saleService.fetchSaleById(id);
	}

}
