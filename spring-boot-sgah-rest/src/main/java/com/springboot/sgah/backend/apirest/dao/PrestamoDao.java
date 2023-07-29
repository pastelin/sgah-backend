package com.springboot.sgah.backend.apirest.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.entities.Prestamo;

public interface PrestamoDao extends CrudRepository<Prestamo, String> {

	@Query(value="select SUM(monto_prestado - monto_pagado) from prestamos where cd_estatus = 1", nativeQuery=true)
	BigDecimal calcularPrestamo();

	@Query(value = "select * from prestamos where cd_estatus = 1", nativeQuery=true)
	List<Prestamo> listarPrestamoActivo();
	
}
