package com.root32.dto;

public class PoDto {

	private Long id;

	private String poCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPoCode() {
		return poCode;
	}

	public void setPoCode(String poCode) {
		this.poCode = poCode;
	}

	public PoDto(Long id, String poCode) {
		super();
		this.id = id;
		this.poCode = poCode;
	}
	
	
}
