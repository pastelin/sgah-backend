package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;

import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByMonth;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByYear;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;

public interface GastoService {

	List<Gasto> findGastoByMonth(int month, int year);
	
	List<HistoricalBalanceByMonth> findHistoricalBalanceByMonth(int month, int year);

	List<Gasto> findGastoByYear(int year);

	List<Gasto> findGastoByCategoria(Integer value);

	List<Gasto> findAllGasto();

	List<Gasto> findGastoByTipo(Integer value);

	List<GastoRecurrente> findAllGastoRecurrente();

	void saveGasto(Gasto gasto);

	BigDecimal calculateExpensesByMonthAndYear(int month, int year);

	BigDecimal calcularMontoDisponible();

	List<HistoricalBalanceByYear> findHistoricalBalanceByYear(int year);

}
