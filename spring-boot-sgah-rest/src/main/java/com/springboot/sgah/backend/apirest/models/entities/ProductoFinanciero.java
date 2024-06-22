package com.springboot.sgah.backend.apirest.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos_financieros")
public class ProductoFinanciero {

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
