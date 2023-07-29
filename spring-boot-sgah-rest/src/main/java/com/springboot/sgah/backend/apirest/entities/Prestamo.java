package com.springboot.sgah.backend.apirest.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="prestamos")
public class Prestamo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String folio;

	@NotNull
	@Column(name = "monto_prestado")
	private BigDecimal montoPrestado;

	@NotEmpty
	private String descripcion;

	@NotNull
	@Column(name = "fecha_creacion")
	private LocalDate fechaCreacion;

	@Column(name = "monto_pagado")
	private BigDecimal montoPagado;

	@Column(name = "cd_estatus")
	private Integer cdEstatus;

	public Prestamo() {
		this.fechaCreacion = LocalDate.now();
		this.cdEstatus = 1;
		this.montoPagado = new BigDecimal(0);
		this.folio = Long.toString(new Date().getTime());
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getMontoPrestado() {
		return montoPrestado;
	}

	public void setMontoPrestado(BigDecimal montoPrestado) {
		this.montoPrestado = montoPrestado;
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

	public BigDecimal getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(BigDecimal montoPagado) {
		this.montoPagado = montoPagado;
	}

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

}
