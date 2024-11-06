package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public class HistoricalBalanceByMonth {

	@NotBlank
	private String categoria;

	@NotBlank
	private BigDecimal saldoGastado;

	@NotBlank
	private Integer tipoMovimiento;

	public String getCategoria() {
		return categoria;
	}

	public HistoricalBalanceByMonth() {
	}

	public HistoricalBalanceByMonth(String categoria, BigDecimal saldoGastado, Integer tipoMovimiento) {
		this.categoria = categoria;
		this.saldoGastado = saldoGastado;
		this.tipoMovimiento = tipoMovimiento;
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

	public Integer getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(Integer tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

}
