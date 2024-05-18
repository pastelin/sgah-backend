package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.PrestamoDao;
import com.springboot.sgah.backend.apirest.models.entities.Prestamo;
import com.springboot.sgah.backend.apirest.services.PrestamoService;

@Service
public class PrestamoServiceImpl implements PrestamoService {

	@Autowired
	PrestamoDao prestamoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Prestamo> listarPrestamoHistorico() {
		return (List<Prestamo>) prestamoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Prestamo> listarPrestamoActivo() {
		return prestamoDao.listarPrestamoActivo();
	}

	@Override
	@Transactional
	public void agregarPrestamo(Prestamo prestamo) {
		prestamoDao.save(prestamo);
	}

	@Override
	@Transactional
	public void actualizarPrestamo(Prestamo prestamo) {
		prestamoDao.save(prestamo);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calcularPrestamo() {
		return prestamoDao.calcularPrestamo();
	}

	@Override
	@Transactional(readOnly = true)
	public Prestamo obtenerPrestamo(String folio) {
		return prestamoDao.findById(folio).orElse(null);
	}

}
