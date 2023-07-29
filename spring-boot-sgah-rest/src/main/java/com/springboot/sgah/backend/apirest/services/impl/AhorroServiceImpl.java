package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.dao.AhorroDao;
import com.springboot.sgah.backend.apirest.dao.InversionDao;
import com.springboot.sgah.backend.apirest.dao.PrestamoDao;
import com.springboot.sgah.backend.apirest.entities.Ahorro;
import com.springboot.sgah.backend.apirest.services.AhorroService;

@Service
public class AhorroServiceImpl implements AhorroService {

	@Autowired
	AhorroDao ahorroDao;
	
	@Autowired
	PrestamoDao prestamoDao;
	
	@Autowired
	InversionDao inversionDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Ahorro> findAllAhorro() {
		return (List<Ahorro>) ahorroDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calcularAhorro() {
		BigDecimal ahorro = ahorroDao.calcularAhorro();
		BigDecimal inversion = inversionDao.calcularInversion();
		BigDecimal prestamo = prestamoDao.calcularPrestamo();
		
		return ahorro.subtract(inversion).subtract(prestamo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ahorro> findAhorroByCurrentMonth(int month, int year) {
		return ahorroDao.findAhorroByCurrentMonth(month, year);
	}

	@Override
	@Transactional
	public Ahorro saveAhorro(Ahorro ahorro) {
		return ahorroDao.save(ahorro);
	}

}
