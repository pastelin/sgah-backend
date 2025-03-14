package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "inversiones")
@NamedStoredProcedureQuery(name = "spuInversion", procedureName = "spu_inversion", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "folio", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "monto", type = BigDecimal.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "descripcion", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "fecha", type = LocalDate.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "app_inversion1", type = Integer.class) })
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

	@NotNull
	@JoinColumn(name = "cd_app_inversion")
	@ManyToOne(fetch = FetchType.EAGER)
	private ProductoFinanciero appInversion;

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

	public ProductoFinanciero getAppInversion() {
		return appInversion;
	}

	public void setCdAppInversion(ProductoFinanciero appInversion) {
		this.appInversion = appInversion;
	}

}
