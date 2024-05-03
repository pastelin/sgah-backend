package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class GastoDto {

	private LocalDate fechaCreacion; 

	@NotNull
	private BigDecimal monto;

	@NotNull
	private String descripcion;

	@NotNull
	private GastoRecurrenteDto gastoRecurrente;

	@NotNull
	private TipoMovimientoDto tipoMovimiento;

	public GastoDto() {
	}

	public GastoDto(LocalDate fechaCreacion, BigDecimal monto, String descripcion,
			GastoRecurrenteDto gastoRecurrenteDto, TipoMovimientoDto tipoMovimientoDto) {
		this.fechaCreacion = fechaCreacion;
		this.monto = monto;
		this.descripcion = descripcion;
		this.gastoRecurrente = gastoRecurrenteDto;
		this.tipoMovimiento = tipoMovimientoDto;
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

	public TipoMovimientoDto getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimientoDto tipoMovimientoDto) {
		this.tipoMovimiento = tipoMovimientoDto;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
}
