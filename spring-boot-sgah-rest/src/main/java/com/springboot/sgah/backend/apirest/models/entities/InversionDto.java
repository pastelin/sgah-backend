package com.springboot.sgah.backend.apirest.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InversionDto {

	private String folio;

	private BigDecimal monto;

	private String descripcion;

	private LocalDate fechaCreacion;

	private Integer cdEstatus;

	private String nbAppInversion;

	public InversionDto(Inversion inversion) {
		this.folio = inversion.getFolio();
		this.monto = inversion.getMonto();
		this.descripcion = inversion.getDescripcion();
		this.fechaCreacion = inversion.getFechaCreacion();
		this.cdEstatus = inversion.getCdEstatus();
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

	public String getNbAppInversion() {
		return nbAppInversion;
	}

	public void setNbAppInversion(String nbAppInversion) {
		this.nbAppInversion = nbAppInversion;
	}

}
