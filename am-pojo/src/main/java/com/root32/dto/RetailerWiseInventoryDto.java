package com.root32.dto;

import java.util.Date;
import java.util.List;

import com.root32.entity.Org;

public interface RetailerWiseInventoryDto {

	public Long getOrgId();

	void setOrgId(Long orgId);

	String getBusinessName();

	void setBusinessName(String businessName);

	Long getProductId();

	void setProduct(Long productId);

	String getProductName();

	void setProductName(String productName);

	Long getQuantity();

	void setQuantity(Long quantity);

	void setProductAndQuantityDtos(List<ProductAndQuantityDto> productAndQuantityDtos);

	Date getLastRefillDate();

	void setLastRefillDate(Date lastRefilldate);

	Date getLastSaleDate();

	void setLastSaleDate(Date lastSaleDate);

}
