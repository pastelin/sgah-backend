package com.springboot.sgah.backend.apirest.models.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class HistoricalBalanceByYear {

	@NotNull
	private String month;

	@NotNull
	private int monthNumber;

	@NotNull
	private BigDecimal saldoIngresado;

	@NotNull
	private BigDecimal saldoGastado;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	public BigDecimal getSaldoIngresado() {
		return saldoIngresado;
	}

	public void setSaldoIngresado(BigDecimal saldoIngresado) {
		this.saldoIngresado = saldoIngresado;
	}

	public BigDecimal getSaldoGastado() {
		return saldoGastado;
	}

	public void setSaldoGastado(BigDecimal saldoGastado) {
		this.saldoGastado = saldoGastado;
	}

}
