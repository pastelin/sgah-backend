package com.springboot.sgah.backend.apirest.models.dto;

public class GastoRecurrenteDto {

	private Integer cdGasto;
	private String nbGasto;
	private Integer cdEstatus;

	public GastoRecurrenteDto(Integer cdGasto, String nbGasto, Integer cdEstatus) {
		this.cdGasto = cdGasto;
		this.nbGasto = nbGasto;
		this.cdEstatus = cdEstatus;
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

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

}
