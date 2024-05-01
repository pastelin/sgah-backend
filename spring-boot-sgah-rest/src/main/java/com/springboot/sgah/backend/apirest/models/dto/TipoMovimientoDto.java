package com.springboot.sgah.backend.apirest.models.dto;

public class TipoMovimientoDto {

	private Integer cdTipo;
	private String nbTipo;

	public TipoMovimientoDto(Integer cdTipoMovimiento, String nbTipoMovimiento) {
		this.cdTipo = cdTipoMovimiento;
		this.nbTipo = nbTipoMovimiento;
	}

	public TipoMovimientoDto() {
	}

	public Integer getCdTipo() {
		return cdTipo;
	}

	public void setCdTipo(Integer cdGasto) {
		this.cdTipo = cdGasto;
	}

	public String getNbTipo() {
		return nbTipo;
	}

	public void setNbTipo(String nbGasto) {
		this.nbTipo = nbGasto;
	}
}
