package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "inversiones")
@NamedStoredProcedureQuery(name = "spuInversion", procedureName = "spu_inversion", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "folio", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "monto", type = BigDecimal.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "descripcion", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "fecha", type = LocalDate.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "estatus", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "app_inversion", type = Integer.class) })
public class Inversion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String folio;

	@NotNull
	private BigDecimal monto;

	@NotEmpty
	private String descripcion;

	@NotNull
	@Column(name = "fecha_creacion")
	private LocalDate fechaCreacion;

	@Column(name = "cd_estatus")
	private Integer cdEstatus;

	@NotNull
	@Column(name = "cd_app_inversion")
	private Integer cdAppInversion;
	
	

	public Inversion() {
		this.folio = Long.toString(new Date().getTime());
		this.fechaCreacion = LocalDate.now();
		this.cdEstatus = 1;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

	public Integer getCdAppInversion() {
		return cdAppInversion;
	}

	public void setCdAppInversion(Integer cdAppInversion) {
		this.cdAppInversion = cdAppInversion;
	}

}
