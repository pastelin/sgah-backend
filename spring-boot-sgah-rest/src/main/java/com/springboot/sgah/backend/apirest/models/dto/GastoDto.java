package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class GastoDto {


	private Long id;
	
	private LocalDate creationDate;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private String descripcion;

	@NotNull
	private GastoRecurrenteDto gastoRecurrente;

	@NotNull
	private OrigenMovimientoDto origenMovimiento;

	public GastoDto() {
	}

	public GastoDto(Long id, LocalDate creationDate, BigDecimal amount, String descripcion,
			GastoRecurrenteDto gastoRecurrenteDto, OrigenMovimientoDto origenMovimiento) {
		this.id = id;
		this.creationDate = creationDate;
		this.amount = amount;
		this.descripcion = descripcion;
		this.gastoRecurrente = gastoRecurrenteDto;
		this.origenMovimiento = origenMovimiento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
