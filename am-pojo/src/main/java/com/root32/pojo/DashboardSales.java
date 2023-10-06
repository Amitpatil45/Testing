package com.root32.pojo;

import java.math.BigDecimal;
import java.util.Date;

public interface DashboardSales {

	public String getProductName();

	public void setProductMaster(String productName);

	public BigDecimal getAmount();

	public void setAmount(BigDecimal amount);

	public Date getSaleDate();

	public void setSaleDate(Date saleDate);

}
