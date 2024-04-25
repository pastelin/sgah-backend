package com.springboot.sgah.backend.apirest.models.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.Gasto;

public interface GastoDao extends CrudRepository<Gasto, Long> {

	@Query(value = "select * from gastos where cd_estatus = 1 and month(fecha_creacion) = ?1 and year(fecha_creacion) = ?2", nativeQuery = true)
	List<Gasto> findGastoByCurrentMonth(int month, int year);

	@Query(value = "select * from gastos where cd_gasto_recurrente = ?1", nativeQuery = true)
	List<Gasto> findGastoByCategoria(Integer value);

	@Query(value = "select * from gastos where cd_tipo_movimiento_gasto = ?1", nativeQuery = true)
	List<Gasto> findGastoByTipo(Integer value);

	@Query(value = "select SUM(monto) from gastos where cd_estatus = 1 and cd_tipo_movimiento_gasto = 1", nativeQuery = true)
	BigDecimal obtenerTotalIngreso();

	@Query(value = "select SUM(monto) from gastos where cd_estatus = 1 and cd_tipo_movimiento_gasto = 2", nativeQuery = true)
	BigDecimal obtenerTotalGastado();

	@Query(value = "select sum(monto) from gastos where cd_tipo_movimiento_gasto = 2 and cd_estatus = 1 and cd_gasto_recurrente <> 11 and month(fecha_creacion) = ?1 and year(fecha_creacion) = ?2", nativeQuery = true)
	BigDecimal obtenerGastoMensual(int month, int year);

}
