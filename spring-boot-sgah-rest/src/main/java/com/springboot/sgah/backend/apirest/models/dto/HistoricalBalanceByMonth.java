package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public class HistoricalBalanceByMonth {

	@NotBlank
	private String categoria;

	@NotBlank
	private BigDecimal saldoGastado;

	@NotBlank
	private Integer origenMovimiento;

	public String getCategoria() {
		return categoria;
	}

	public HistoricalBalanceByMonth() {
	}

	public HistoricalBalanceByMonth(String categoria, BigDecimal saldoGastado, Integer origenMovimiento) {
		this.categoria = categoria;
		this.saldoGastado = saldoGastado;
		this.origenMovimiento = origenMovimiento;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getSaldoGastado() {
		return saldoGastado;
	}

	public void setSaldoGastado(BigDecimal saldoGastado) {
		this.saldoGastado = saldoGastado;
	}

	public Integer getOrigenMovimiento() {
		return origenMovimiento;
	}

	public void setOrigenMovimiento(Integer origenMovimiento) {
		this.origenMovimiento = origenMovimiento;
	}

}
