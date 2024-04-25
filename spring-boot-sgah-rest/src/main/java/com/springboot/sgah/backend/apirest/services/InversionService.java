package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;

import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.InversionDto;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;

public interface InversionService {

	List<Inversion> findAllInversion();

	List<InversionDto> updateDescripcionInversion(List<Inversion> inversiones );
	
	Inversion obtenerInversion(String folio);
	
	BigDecimal calcularMonto();
	
	BigDecimal obtenerMontoActual(String folio);
	
	List<ProductoFinanciero> findAllProductosFinancieros();
	
	void saveInversion(Inversion inversion);

	Inversion updateInversion(Inversion inversion);
}
