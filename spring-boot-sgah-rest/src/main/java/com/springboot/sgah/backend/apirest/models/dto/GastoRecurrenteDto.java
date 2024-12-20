package com.springboot.sgah.backend.apirest.models.dto;

public class GastoRecurrenteDto {

	private Integer cdGasto;
	private String nbGasto;

	public GastoRecurrenteDto(Integer cdGasto, String nbGasto) {
		this.cdGasto = cdGasto;
		this.nbGasto = nbGasto;
	}

	public GastoRecurrenteDto() {
	}

	public Integer getCdGasto() {
		return cdGasto;
	}

	public void setCdGasto(Integer cdGasto) {
		this.cdGasto = cdGasto;
	}

	public String getNbGasto() {
		return nbGasto;
	}

	public void setNbGasto(String nbGasto) {
		this.nbGasto = nbGasto;
	}
}
