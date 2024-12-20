package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByMonth;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByYear;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.OrigenMovimiento;

public interface GastoService {

	List<Gasto> findGastoByMonth(int month, int year);

	List<HistoricalBalanceByMonth> findHistoricalBalanceByMonth(int month, int year);

	List<Gasto> findAllGasto();

	List<GastoRecurrente> findAllGastoRecurrente();

	void saveGasto(Gasto gasto);

	BigDecimal calculateExpensesByMonthAndYear(int month, int year);

	BigDecimal calcularMontoDisponible();

	List<HistoricalBalanceByYear> findHistoricalBalanceByYear(int year);

	Optional<GastoRecurrente> findGastoRecurrenteById(Integer id);

	Optional<OrigenMovimiento> findOrigenMovimientoById(Integer id);

}
