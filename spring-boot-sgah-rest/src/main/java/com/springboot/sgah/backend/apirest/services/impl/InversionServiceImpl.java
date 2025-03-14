package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.models.dao.InversionDao;
import com.springboot.sgah.backend.apirest.models.dao.ProductoFinancieroDao;
import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;
import com.springboot.sgah.backend.apirest.services.InversionService;

@Service
public class InversionServiceImpl implements InversionService {

	InversionDao inversionDao;

	ProductoFinancieroDao catalogoDao;

	public InversionServiceImpl() {
	}

	@Autowired
	public InversionServiceImpl(InversionDao inversionDao, ProductoFinancieroDao catalogoDao) {
		this.inversionDao = inversionDao;
		this.catalogoDao = catalogoDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Inversion> findAllInversion() {
		return (List<Inversion>) inversionDao.findAll();
	}

	@Override
	public BigDecimal calcularMonto() {
		return inversionDao.calcularInversion();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductoFinanciero> findAllProductosFinancieros() {
		return (List<ProductoFinanciero>) catalogoDao.findAll();
	}

	@Override
	public void saveInversion(Inversion inversion) {
		inversionDao.saveInversion(inversion.getFolio(), inversion.getMonto(), inversion.getDescripcion(),
				inversion.getFechaCreacion(), inversion.getAppInversion().getCdAppInversion());
	}

	@Override
	@Transactional
	public void updateInversion(Inversion inversion) {
		inversionDao.save(inversion);

	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obtenerMontoActual(String folio) {
		return inversionDao.obtenerMontoActual(folio);
	}

	@Override
	@Transactional(readOnly = true)
	public Inversion obtenerInversion(String folio) {
		return inversionDao.findById(folio).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProductoFinanciero> findProductoFinancieroById(Integer id) {
		return catalogoDao.findById(id);
	}

}
