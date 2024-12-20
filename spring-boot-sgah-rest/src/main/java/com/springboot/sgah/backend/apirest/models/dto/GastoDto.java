package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class GastoDto {

	private LocalDate fechaCreacion;

	@NotNull
	private BigDecimal monto;

	@NotNull
	private String descripcion;

	@NotNull
	private GastoRecurrenteDto gastoRecurrente;

	@NotNull
	private OrigenMovimientoDto origenMovimiento;

	public GastoDto() {
	}

	public GastoDto(LocalDate fechaCreacion, BigDecimal monto, String descripcion,
			GastoRecurrenteDto gastoRecurrenteDto, OrigenMovimientoDto origenMovimiento) {
		this.fechaCreacion = fechaCreacion;
		this.monto = monto;
		this.descripcion = descripcion;
		this.gastoRecurrente = gastoRecurrenteDto;
		this.origenMovimiento = origenMovimiento;
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

	public GastoRecurrenteDto getGastoRecurrente() {
		return gastoRecurrente;
	}

	public void setGastoRecurrente(GastoRecurrenteDto gastoRecurrenteDto) {
		this.gastoRecurrente = gastoRecurrenteDto;
	}

	public OrigenMovimientoDto getOrigenMovimiento() {
		return origenMovimiento;
	}

	public void setOrigenMovimiento(OrigenMovimientoDto origenMovimiento) {
		this.origenMovimiento = origenMovimiento;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
}
