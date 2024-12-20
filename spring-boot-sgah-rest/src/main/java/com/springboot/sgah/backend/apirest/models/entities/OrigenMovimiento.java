package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "catalogo_origen_movimiento")
public class OrigenMovimiento implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cd_origen_movimiento")
	private Integer id;

	@Column(name = "nb_origen_movimiento")
	private String descripcion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
