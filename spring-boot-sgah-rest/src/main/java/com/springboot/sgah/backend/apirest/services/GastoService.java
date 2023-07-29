package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.springboot.sgah.backend.apirest.entities.Gasto;
import com.springboot.sgah.backend.apirest.entities.GastoDto;
import com.springboot.sgah.backend.apirest.entities.GastoRecurrente;

public interface GastoService {

	List<Gasto> findGastoByCurrentMonth(int month, int year);

	List<Gasto> findGastoByCategoria(Integer value);

	List<Gasto> findAllGasto();

	List<Gasto> findGastoByTipo(Integer value);

	List<Gasto> findGastoByFecha(LocalDate fechaInicio, LocalDate fechaFin);
	
	List<GastoRecurrente> findAllGastoRecurrente();
	
	List<GastoDto> updateDescripcionGasto(List<Gasto> gastos);

	Gasto saveGasto(Gasto gasto);

	BigDecimal obtenerGastoMensual(int month, int year);

	BigDecimal calcularMontoDisponible();

	
}
