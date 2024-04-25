package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.GastoDao;
import com.springboot.sgah.backend.apirest.models.dao.GastoRecurrenteDao;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoDto;
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
	public List<Gasto> findGastoByCurrentMonth(int month, int year) {
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
	public List<Gasto> findGastoByFecha(LocalDate fechaInicio, LocalDate fechaFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Gasto saveGasto(Gasto gasto) {
		return gastoDao.save(gasto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GastoRecurrente> findAllGastoRecurrente() {
		return (List<GastoRecurrente>) gastoRecurrenteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GastoDto> updateDescripcionGasto(List<Gasto> gastos) {
		List<GastoDto> dtoGastos = new ArrayList<>();
		List<GastoRecurrente> gastosRecurrentes = findAllGastoRecurrente();

		gastos.stream().forEach(gasto -> {

			String categoriaGasto = "N/A";
			if(gasto.getCdGastoRecurrente() != 0) {
				for(GastoRecurrente gastoR : gastosRecurrentes) {
					if(gastoR.getCdGasto().equals(gasto.getCdGastoRecurrente())) {
						categoriaGasto = gastoR.getNbGasto();
						break;
					}
				}
			}
			
//			String categoriaGasto = (gasto.getCdGastoRecurrente() != 0)
//					? gastosRecurrentes.get(gasto.getCdGastoRecurrente() - 1).getNbGasto()
//					: "N/A";
			String tipoMovimientoGasto = (gasto.getCdTipoMovimiento() == 1) ? "Ingreso" : "Gasto";

			dtoGastos.add(new GastoDto(gasto.getId(), gasto.getFechaCreacion(), gasto.getMonto(), gasto.getDescripcion(),
					categoriaGasto, tipoMovimientoGasto));
		});
		
		return dtoGastos;
	}

}
