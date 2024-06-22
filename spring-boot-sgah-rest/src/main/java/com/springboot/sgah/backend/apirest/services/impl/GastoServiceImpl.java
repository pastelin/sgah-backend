package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.GastoDao;
import com.springboot.sgah.backend.apirest.models.dao.GastoRecurrenteDao;
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
	public BigDecimal obtenerGastoMensual(int month, int year) {
		return gastoDao.obtenerGastoMensual(month, year);
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

}
