package com.root32.dto;

import java.math.BigDecimal;
import java.util.List;

import com.root32.entity.Sale;
import com.root32.pojo.DashBoardProductStock;

public class Dashboard {
	private Long productSold;
	private BigDecimal revenueGenerated;

	List<DashBoardProductStock> productStockList;

	List<Sale> saleList;

	public Long getProductSold() {
		return productSold;
	}

	public void setProductSold(Long productSold) {
		this.productSold = productSold;
	}

	public BigDecimal getRevenueGenerated() {
		return revenueGenerated;
	}

	public void setRevenueGenerated(BigDecimal revenueGenerated) {
		this.revenueGenerated = revenueGenerated;
	}

	public List<DashBoardProductStock> getProductStockList() {
		return productStockList;
	}

	public void setProductStockList(List<DashBoardProductStock> productStockList) {
		this.productStockList = productStockList;
	}

	public List<Sale> getSaleList() {
		return saleList;
	}

	public void setSaleList(List<Sale> saleList) {
		this.saleList = saleList;
	}

}
