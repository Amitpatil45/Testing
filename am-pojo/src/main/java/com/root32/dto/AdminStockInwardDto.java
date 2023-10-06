package com.root32.dto;

public class AdminStockInwardDto {

	private Long batchId;

	private Long quantity;

	public Long getBatchId() {
		return batchId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

}
