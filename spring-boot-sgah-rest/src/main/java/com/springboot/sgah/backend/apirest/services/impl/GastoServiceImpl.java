package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.GastoDao;
import com.springboot.sgah.backend.apirest.models.dao.GastoRecurrenteDao;
import com.springboot.sgah.backend.apirest.models.dao.OrigenMovimientoDao;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByMonth;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByYear;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.OrigenMovimiento;
import com.springboot.sgah.backend.apirest.services.GastoService;

@Service
public class GastoServiceImpl implements GastoService {

	private GastoDao gastoDao;
	private GastoRecurrenteDao gastoRecurrenteDao;
	private OrigenMovimientoDao origenMovimientoDao;

	public GastoServiceImpl() {
	}

	@Autowired
	public GastoServiceImpl(GastoDao gastoDao, GastoRecurrenteDao gastoRecurrenteDao,
			OrigenMovimientoDao origenMovimientoDao) {
		this.gastoDao = gastoDao;
		this.gastoRecurrenteDao = gastoRecurrenteDao;
		this.origenMovimientoDao = origenMovimientoDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Gasto> findGastoByMonth(int month, int year) {
		return gastoDao.findGastoByCurrentMonth(month, year);
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
	public void saveGasto(Gasto gasto) {
		gastoDao.save(gasto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GastoRecurrente> findAllGastoRecurrente() {
		return (List<GastoRecurrente>) gastoRecurrenteDao.findAll();
	}

	@Override
	public List<HistoricalBalanceByYear> findHistoricalBalanceByYear(int year) {
		List<HistoricalBalanceByYear> historicalBalance = new ArrayList<>();

		for (int month = 1; month <= 12; month++) {
			BigDecimal saldoIngresado = gastoDao.calculateRevenuePerMonthAndYear(month, year);
			BigDecimal saldoGastado = gastoDao.calculateExpensesByMonthAndYear(month, year);

			if (saldoIngresado != null || saldoGastado != null) {
				HistoricalBalanceByYear historicalBalanceByYear = new HistoricalBalanceByYear();
				historicalBalanceByYear.setMonthNumber(month);
				historicalBalanceByYear.setMonth(getMonthValue(month));
				historicalBalanceByYear.setSaldoIngresado(saldoIngresado);
				historicalBalanceByYear.setSaldoGastado(saldoGastado);
				historicalBalance.add(historicalBalanceByYear);
			}
		}

		return historicalBalance;
	}

	private String getMonthValue(int month) {
		Month monthEnum = Month.of(month);
		return monthEnum.getDisplayName(TextStyle.FULL, new Locale("es"));
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
				if (gasto.getGastoRecurrente().getCdGasto().equals(gastoRecurrente.getCdGasto())) {
					if (gasto.getOrigenMovimiento().getId().equals(1)) {
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

	@Override
	public Optional<GastoRecurrente> findGastoRecurrenteById(Integer id) {
		return gastoRecurrenteDao.findById(id);
	}

	@Override
	public Optional<OrigenMovimiento> findOrigenMovimientoById(Integer id) {
		return origenMovimientoDao.findById(id);
	}

	@Override
	public Optional<Gasto> editExpense(Long id, Gasto gasto) {
		Optional<Gasto> gastoOptional = gastoDao.findById(id);

		if (gastoOptional.isPresent()) {
			Gasto gastoDb = gastoOptional.get();
			gastoDb.setDescripcion(gasto.getDescripcion());
			gastoDb.setFechaCreacion(gasto.getFechaCreacion());
			gastoDb.setMonto(gasto.getMonto());
			gastoDb.setGastoRecurrente(gasto.getGastoRecurrente());
			gastoDb.setOrigenMovimiento(gasto.getOrigenMovimiento());
			return Optional.of(gastoDao.save(gastoDb));
		}

		return gastoOptional;
	}

	@Override
	public Optional<Gasto> delete(Long id) {
		Optional<Gasto> gastoOptional = gastoDao.findById(id);

		gastoOptional.ifPresent(p -> gastoDao.delete(p));
			

		return gastoOptional;
	}

}
