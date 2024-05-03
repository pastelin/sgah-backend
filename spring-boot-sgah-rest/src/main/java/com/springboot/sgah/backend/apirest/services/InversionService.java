package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;

import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;

public interface InversionService {

	List<Inversion> findAllInversion();

	Inversion obtenerInversion(String folio);

	BigDecimal calcularMonto();

	BigDecimal obtenerMontoActual(String folio);

	List<ProductoFinanciero> findAllProductosFinancieros();

	void saveInversion(Inversion inversion);

	void updateInversion(Inversion inversion);
}
