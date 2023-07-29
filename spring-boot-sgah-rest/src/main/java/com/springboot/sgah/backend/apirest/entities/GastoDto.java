package com.springboot.sgah.backend.apirest.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoDto {
	private Long id;

	private LocalDate fechaCreacion;

	private BigDecimal monto;

	private String descripcion;

	private String nbGastoRecurrente;

	private String nbTipoMovimiento;
	
	public GastoDto(Long id, LocalDate fechaCreacion, BigDecimal monto, String descripcion, String nbGastoRecurrente,
			String nbTipoMovimiento) {
		this.id = id;
		this.fechaCreacion = fechaCreacion;
		this.monto = monto;
		this.descripcion = descripcion;
		this.nbGastoRecurrente = nbGastoRecurrente;
		this.nbTipoMovimiento = nbTipoMovimiento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNbGastoRecurrente() {
		return nbGastoRecurrente;
	}

	public void setNbGastoRecurrente(String nbGastoRecurrente) {
		this.nbGastoRecurrente = nbGastoRecurrente;
	}

	public String getNbTipoMovimiento() {
		return nbTipoMovimiento;
	}

	public void setNbTipoMovimiento(String nbTipoMovimiento) {
		this.nbTipoMovimiento = nbTipoMovimiento;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
}
