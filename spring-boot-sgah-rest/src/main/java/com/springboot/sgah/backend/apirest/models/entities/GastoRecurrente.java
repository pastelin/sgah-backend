package com.springboot.sgah.backend.apirest.models.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gastos_recurrentes")
public class GastoRecurrente {

	@Id 
	@Column(name = "cd_gasto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cdGasto;

	@Column(name = "nb_gasto")
	private String nbGasto;

	@Column(name = "cd_estatus")
	private Integer cdEstatus;

	private BigDecimal monto;

	public Integer getCdGasto() {
		return cdGasto;
	}

	public void setCdGasto(Integer cdGasto) {
		this.cdGasto = cdGasto;
	}

	public String getNbGasto() {
		return nbGasto;
	}

	public void setNbGasto(String nbGasto) {
		this.nbGasto = nbGasto;
	}

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}
