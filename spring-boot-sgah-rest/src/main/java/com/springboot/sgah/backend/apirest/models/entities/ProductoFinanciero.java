package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalogo_productos_financieros")
public class ProductoFinanciero implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cd_app_inversion")
	private Integer cdAppInversion;

	@Column(name = "nb_app_inversion")
	private String nbAppInversion;

	public Integer getCdAppInversion() {
		return cdAppInversion;
	}

	public void setCdAppInversion(Integer cdAppInversion) {
		this.cdAppInversion = cdAppInversion;
	}

	public String getNbAppInversion() {
		return nbAppInversion;
	}

	public void setNbAppInversion(String nbAppInversion) {
		this.nbAppInversion = nbAppInversion;
	}

}
