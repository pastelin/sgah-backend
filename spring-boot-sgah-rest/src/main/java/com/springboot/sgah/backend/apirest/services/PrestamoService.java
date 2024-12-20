package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.springboot.sgah.backend.apirest.models.entities.Estado;
import com.springboot.sgah.backend.apirest.models.entities.Prestamo;

public interface PrestamoService {

	List<Prestamo> listarPrestamoHistorico();
	
	List<Prestamo> listarPrestamoActivo();
	
	void agregarPrestamo(Prestamo prestamo);
	
	void actualizarPrestamo(Prestamo prestamo);
	
	BigDecimal calcularPrestamo();
	
	Prestamo obtenerPrestamo(String folio);

	Optional<Estado> findEstadoById(Integer id);
}
