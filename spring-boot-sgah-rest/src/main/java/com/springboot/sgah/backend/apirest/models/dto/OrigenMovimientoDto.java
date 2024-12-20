package com.springboot.sgah.backend.apirest.models.dto;

public class OrigenMovimientoDto {

	private Integer id;
	private String descripcion;

	public OrigenMovimientoDto(Integer id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public OrigenMovimientoDto() {
	}

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
