package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalogo_gastos_recurrentes")
public class GastoRecurrente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "cd_gasto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cdGasto;

	@Column(name = "nb_gasto")
	private String nbGasto;

	public GastoRecurrente() {
	}

	public GastoRecurrente(Integer cdGasto, String nbGasto) {
		this.cdGasto = cdGasto;
		this.nbGasto = nbGasto;
	}

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

}
