package com.springboot.sgah.backend.apirest.models.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.Gasto;

public interface GastoDao extends CrudRepository<Gasto, Long> {

	@Query(value = "select g from Gasto g left join fetch g.gastoRecurrente left join fetch g.origenMovimiento where month(g.fechaCreacion) = ?1 and year(g.fechaCreacion) = ?2")
	List<Gasto> findGastoByCurrentMonth(int month, int year);

	@Query(value = "select SUM(monto) from gastos where cd_origen_movimiento = 1", nativeQuery = true)
	BigDecimal obtenerTotalIngreso();

	@Query(value = "select SUM(monto) from gastos where cd_origen_movimiento = 2", nativeQuery = true)
	BigDecimal obtenerTotalGastado();

	@Query(value = "select sum(monto) from gastos where cd_origen_movimiento = 2 and month(fecha_creacion) = ?1 and year(fecha_creacion) = ?2", nativeQuery = true)
	BigDecimal calculateExpensesByMonthAndYear(int month, int year);

	@Query(value = "select sum(monto) from gastos where cd_origen_movimiento = 1 and month(fecha_creacion) = ?1 and year(fecha_creacion) = ?2", nativeQuery = true)
	BigDecimal calculateRevenuePerMonthAndYear(int month, int year);

}
