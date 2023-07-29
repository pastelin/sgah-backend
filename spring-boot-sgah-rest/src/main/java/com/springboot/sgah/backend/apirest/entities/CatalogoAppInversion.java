package com.springboot.sgah.backend.apirest.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="catalogo_app_inversiones")
public class CatalogoAppInversion {

	@Id
	@Column(name="cd_app_inversion")
	private Integer cdAppInversion;
	
	@Column(name="nb_app_inversion")
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
