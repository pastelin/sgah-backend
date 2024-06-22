package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

public class InversionDto {

	private String folio;

	@NotNull
	private BigDecimal monto;

	@NotNull
	private String descripcion;

	private LocalDate fechaCreacion;

	@NotNull
	private ProductoFinancieroDto productoFinanciero;

	public InversionDto() {
		this.folio = Long.toString(new Date().getTime());
		this.fechaCreacion = LocalDate.now();
	}

	public InversionDto(String folio, BigDecimal monto, String descripcion, LocalDate fechaCreacion,
			ProductoFinancieroDto productoFinanciero) {
		this.folio = folio;
		this.monto = monto;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.productoFinanciero = productoFinanciero;
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

	public ProductoFinancieroDto getProductoFinanciero() {
		return productoFinanciero;
	}

	public void setProductoFinanciero(ProductoFinancieroDto productoFinanciero) {
		this.productoFinanciero = productoFinanciero;
	}

}
