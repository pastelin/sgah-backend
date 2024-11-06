package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.GastoDao;
import com.springboot.sgah.backend.apirest.models.dao.GastoRecurrenteDao;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByMonth;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByYear;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.services.GastoService;

@Service
public class GastoServiceImpl implements GastoService {

	@Autowired
	GastoDao gastoDao;

	@Autowired
	GastoRecurrenteDao gastoRecurrenteDao;

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findGastoByMonth(int month, int year) {
		return gastoDao.findGastoByCurrentMonth(month, year);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findGastoByCategoria(Integer value) {
		return gastoDao.findGastoByCategoria(value);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculateExpensesByMonthAndYear(int month, int year) {
		return gastoDao.calculateExpensesByMonthAndYear(month, year);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calcularMontoDisponible() {
		BigDecimal montoIngresado = gastoDao.obtenerTotalIngreso();
		BigDecimal montoGastado = gastoDao.obtenerTotalGastado();

		return montoIngresado.subtract(montoGastado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findAllGasto() {
		return (List<Gasto>) gastoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findGastoByTipo(Integer value) {
		return gastoDao.findGastoByTipo(value);
	}

	@Override
	public void saveGasto(Gasto gasto) {
		gastoDao.save(gasto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GastoRecurrente> findAllGastoRecurrente() {
		return (List<GastoRecurrente>) gastoRecurrenteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findGastoByYear(int year) {
		return gastoDao.findGastoByYear(year);
	}

	@Override
	public List<HistoricalBalanceByYear> findHistoricalBalanceByYear(int year) {
		List<HistoricalBalanceByYear> historicalBalance = new ArrayList<>();

		int month = 1;

		while (month <= 12) {
			HistoricalBalanceByYear historicalBalanceByYear = new HistoricalBalanceByYear();
			historicalBalanceByYear.setMonthNumber(month);
			historicalBalanceByYear.setMonth(getMonthValue(month));
			historicalBalanceByYear.setSaldoIngresado(gastoDao.calculateRevenuePerMonthAndYear(month, year));
			historicalBalanceByYear.setSaldoGastado(gastoDao.calculateExpensesByMonthAndYear(month, year));

			if (historicalBalanceByYear.getSaldoIngresado() != null
					|| historicalBalanceByYear.getSaldoGastado() != null) {
				historicalBalance.add(historicalBalanceByYear);
			}
			month++;
		}

		return historicalBalance;
	}

	private String getMonthValue(int month) {
		String[] months = { "", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
				"Octubre", "Noviembre", "Diciembre" };
		return (month >= 1 && month <= 12) ? months[month] : "";
	}

		@Override
	public List<HistoricalBalanceByMonth> findHistoricalBalanceByMonth(int month, int year) {
		List<HistoricalBalanceByMonth> historicalBalance = new ArrayList<>();
		List<Gasto> gastos = gastoDao.findGastoByCurrentMonth(month, year);
		List<GastoRecurrente> gastoRecurrentes = (List<GastoRecurrente>) gastoRecurrenteDao.findAll();
	
		gastoRecurrentes.forEach(gastoRecurrente -> {
			String categoria = gastoRecurrente.getNbGasto();
			BigDecimal saldoGastado = BigDecimal.ZERO;
			BigDecimal saldoIngresado = BigDecimal.ZERO;
	
			for (Gasto gasto : gastos) {
				if (gasto.getCdGastoRecurrente().equals(gastoRecurrente.getCdGasto())) {
					if (gasto.getCdTipoMovimiento().equals(1)) {
						saldoIngresado = saldoIngresado.add(gasto.getMonto());
					} else {
						saldoGastado = saldoGastado.add(gasto.getMonto());
					}
				}
			}
	
			if (saldoIngresado.compareTo(BigDecimal.ZERO) > 0) {
				historicalBalance.add(new HistoricalBalanceByMonth(categoria, saldoIngresado, 1));
			}
	
			if (saldoGastado.compareTo(BigDecimal.ZERO) > 0) {
				historicalBalance.add(new HistoricalBalanceByMonth(categoria, saldoGastado, 2));
			}
		});
	
		return historicalBalance;
	}

}
