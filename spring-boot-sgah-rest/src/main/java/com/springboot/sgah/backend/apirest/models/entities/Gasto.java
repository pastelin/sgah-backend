package com.springboot.sgah.backend.apirest.models.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "gastos")
public class Gasto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "fecha_creacion")
	private LocalDate fechaCreacion;

	@NotNull
	private BigDecimal monto;

	@NotEmpty
	private String descripcion;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cd_gasto_recurrente")
	private GastoRecurrente gastoRecurrente;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cd_origen_movimiento")
	private OrigenMovimiento origenMovimiento;

	public Gasto() {
		this.fechaCreacion = LocalDate.now();
	}

	public Gasto(BigDecimal monto, String descripcion, GastoRecurrente gastoRecurrente,
			OrigenMovimiento origenMovimiento) {
		this.monto = monto;
		this.descripcion = descripcion;
		this.gastoRecurrente = gastoRecurrente;
		this.origenMovimiento = origenMovimiento;
		this.fechaCreacion = LocalDate.now();
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

	public GastoRecurrente getGastoRecurrente() {
		return gastoRecurrente;
	}

	public void setGastoRecurrente(GastoRecurrente gastoRecurrente) {
		this.gastoRecurrente = gastoRecurrente;
	}

	public OrigenMovimiento getOrigenMovimiento() {
		return origenMovimiento;
	}

	public void setCdTipoMovimiento(OrigenMovimiento origenMovimiento) {
		this.origenMovimiento = origenMovimiento;
	}

}
