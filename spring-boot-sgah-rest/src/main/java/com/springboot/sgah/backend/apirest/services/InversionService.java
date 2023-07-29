package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;

import com.springboot.sgah.backend.apirest.entities.CatalogoAppInversion;
import com.springboot.sgah.backend.apirest.entities.Inversion;

public interface InversionService {

	List<Inversion> findAllInversion();
	
	BigDecimal calcularMonto();
	
	BigDecimal obtenerMontoActual(String folio);
	
	List<CatalogoAppInversion> findAllCatalogoAppInversion();
	
	void saveInversion(Inversion inversion);

	void updateInversion(Inversion inversion);
}
