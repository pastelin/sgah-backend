package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PrestamoDto {

	private String folio;

	@NotNull
	private BigDecimal saldoPrestado;

	@NotEmpty
	private String descripcion;

	@NotNull
	private LocalDate fechaCreacion;

	private BigDecimal saldoPagado;

	private Integer cdEstado;

	public PrestamoDto() {
		this.fechaCreacion = LocalDate.now();
		this.cdEstado = 1;
		this.saldoPagado = new BigDecimal(0);
		this.folio = Long.toString(System.currentTimeMillis());
	}

	public PrestamoDto(String folio, BigDecimal saldoPrestado, String descripcion, LocalDate fechaCreacion,
			BigDecimal saldoPagado, Integer cdEstado) {
		this.folio = folio;
		this.saldoPrestado = saldoPrestado;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.saldoPagado = saldoPagado;
		this.cdEstado = cdEstado;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getSaldoPrestado() {
		return saldoPrestado;
	}

	public void setSaldoPrestado(BigDecimal saldoPrestado) {
		this.saldoPrestado = saldoPrestado;
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

	public BigDecimal getSaldoPagado() {
		return saldoPagado;
	}

	public void setSaldoPagado(BigDecimal saldoPagado) {
		this.saldoPagado = saldoPagado;
	}

	public Integer getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(Integer cdEstado) {
		this.cdEstado = cdEstado;
	}

}
