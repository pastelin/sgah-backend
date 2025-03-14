package com.springboot.sgah.backend.apirest.models.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springboot.sgah.backend.apirest.models.entities.Inversion;

public interface InversionDao extends CrudRepository<Inversion, String> {

	@Query(value = "select SUM(monto) from inversiones", nativeQuery = true)
	BigDecimal calcularInversion();

	@Query(value = "select monto from inversiones where folio = ?1", nativeQuery = true)
	BigDecimal obtenerMontoActual(String folio);

	@Query(value = "update inversiones set monto = ?1 where folio = ?2", nativeQuery = true)
	void updateInversion(BigDecimal monto, String folio);

	@Procedure(name = "spuInversion")
	void saveInversion(@Param("folio") String folio, @Param("monto") BigDecimal monto,
			@Param("descripcion") String descripcion,
			@Param("fecha") LocalDate fecha,
			@Param("app_inversion1") Integer appInversion);

}
