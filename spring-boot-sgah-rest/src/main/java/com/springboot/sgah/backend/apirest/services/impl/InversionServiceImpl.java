package com.springboot.sgah.backend.apirest.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.dao.CatalogoAppInversionDao;
import com.springboot.sgah.backend.apirest.dao.InversionDao;
import com.springboot.sgah.backend.apirest.entities.CatalogoAppInversion;
import com.springboot.sgah.backend.apirest.entities.Inversion;
import com.springboot.sgah.backend.apirest.entities.InversionDto;
import com.springboot.sgah.backend.apirest.services.InversionService;

@Service
public class InversionServiceImpl implements InversionService {

	@Autowired
	InversionDao inversionDao;
	
	@Autowired
	CatalogoAppInversionDao catalogoDao;
	
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
	public List<CatalogoAppInversion> findAllCatalogoAppInversion() {
		return (List<CatalogoAppInversion>) catalogoDao.findAll();
	}

	@Override
	public void saveInversion(Inversion inversion) {
		inversionDao.saveInversion(inversion.getFolio(), inversion.getMonto(), inversion.getDescripcion(), inversion.getFechaCreacion(),
				inversion.getCdEstatus(), inversion.getCdAppInversion());
	}

	@Override
	@Transactional
	public Inversion updateInversion(Inversion inversion) {
		return inversionDao.save(inversion);
		
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obtenerMontoActual(String folio) {
		return inversionDao.obtenerMontoActual(folio);
	}

	@Override
	public List<InversionDto> updateDescripcionInversion(List<Inversion> inversiones) {
		
		List<InversionDto> inversionesDto = new ArrayList<>();
		List<CatalogoAppInversion> catalogos =  (List<CatalogoAppInversion>) catalogoDao.findAll();
		
		inversiones.stream().forEach(inversion -> {

			InversionDto inversionDto = new InversionDto(inversion);
			
			catalogos.stream().forEach( catalogo -> {
				
				if (inversion.getCdAppInversion() == catalogo.getCdAppInversion()) {
					inversionDto.setNbAppInversion(catalogo.getNbAppInversion());
					return;
				}
				
			});
			
			inversionesDto.add(inversionDto);
		});
		
		
		return inversionesDto;
	}

	@Override
	public Inversion obtenerInversion(String folio) {
		return inversionDao.findById(folio).orElse(null);
	}

}
