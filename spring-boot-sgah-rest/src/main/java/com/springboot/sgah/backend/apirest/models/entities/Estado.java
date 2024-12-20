package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalogo_estado")
public class Estado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cd_estado")
	private Integer cdEstado;

	@Column(name = "nb_estado")
	private String nbEstado;

	public Integer getCdEstado() {
		return cdEstado;
	}

	public void setCdEstado(Integer cdEstado) {
		this.cdEstado = cdEstado;
	}

	public String getNbEstado() {
		return nbEstado;
	}

	public void setNbEstado(String nbEstado) {
		this.nbEstado = nbEstado;
	}

}
