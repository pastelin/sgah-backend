package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoDto {
	private Long id;

	private LocalDate fechaCreacion;

	private BigDecimal monto;

	private String descripcion;

	private GastoRecurrenteDto gastoRecurrente;

	private TipoMovimientoDto tipoMovimiento;

	public GastoDto() {
	}

	public GastoDto(Long id, LocalDate fechaCreacion, BigDecimal monto, String descripcion,
			GastoRecurrenteDto gastoRecurrenteDto, TipoMovimientoDto tipoMovimientoDto) {
		this.id = id;
		this.fechaCreacion = fechaCreacion;
		this.monto = monto;
		this.descripcion = descripcion;
		this.gastoRecurrente = gastoRecurrenteDto;
		this.tipoMovimiento = tipoMovimientoDto;
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
