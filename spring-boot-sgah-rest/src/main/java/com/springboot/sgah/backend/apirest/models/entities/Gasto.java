package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="gastos")
public class Gasto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(name="fecha_creacion")
	private LocalDate fechaCreacion;
	
	@NotNull
	private BigDecimal monto;
	
	@NotEmpty
	private String descripcion;
	
	@NotNull
	@Column(name="cd_gasto_recurrente")
	private Integer cdGastoRecurrente;
	
	@NotNull
	@Column(name="cd_estatus")
	private Integer cdEstatus;
	
	@NotNull
	@Column(name="cd_tipo_movimiento_gasto")
	private Integer cdTipoMovimiento;

	
	
	public Gasto() {
		this.fechaCreacion = LocalDate.now();
		this.cdEstatus = 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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

	public Integer getCdGastoRecurrente() {
		return cdGastoRecurrente;
	}

	public void setCdGastoRecurrente(Integer cdGastoRecurrente) {
		this.cdGastoRecurrente = cdGastoRecurrente;
	}

	public Integer getCdEstatus() {
		return cdEstatus;
	}

	public void setCdEstatus(Integer cdEstatus) {
		this.cdEstatus = cdEstatus;
	}

	public Integer getCdTipoMovimiento() {
		return cdTipoMovimiento;
	}

	public void setCdTipoMovimiento(Integer cdTipoMovimiento) {
		this.cdTipoMovimiento = cdTipoMovimiento;
	}
	
}
