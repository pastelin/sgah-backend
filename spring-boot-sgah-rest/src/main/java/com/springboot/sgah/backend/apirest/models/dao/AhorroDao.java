package com.springboot.sgah.backend.apirest.models.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.Ahorro;

public interface AhorroDao extends CrudRepository<Ahorro, Long> {

	@Query(value = "select SUM(monto) from ahorros", nativeQuery = true)
	BigDecimal calcularAhorro();

	@Query(value = "select * from ahorros where month(fecha_creacion) = ?1 and year(fecha_creacion) = ?2", nativeQuery = true)
	List<Ahorro> findAhorroByCurrentMonth(int month, int year);
}
