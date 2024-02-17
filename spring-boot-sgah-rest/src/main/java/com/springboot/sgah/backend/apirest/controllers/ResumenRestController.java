package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_ERROR;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.services.AhorroService;
import com.springboot.sgah.backend.apirest.services.GastoService;
import com.springboot.sgah.backend.apirest.services.InversionService;
import com.springboot.sgah.backend.apirest.services.PrestamoService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/resumen/v0/resumen")
public class ResumenRestController {
	
	@Autowired
	AhorroService ahorroService;
	
	@Autowired
	GastoService gastoService;
	
	@Autowired
	PrestamoService prestamoService;
	
	@Autowired
	InversionService inversionService;
	
	@GetMapping("/detalle")
	public ResponseEntity<?> obtenerDetalleIngresos() {
	
		Map<String, Object> response = new HashMap<>();
		BigDecimal totalMontoAhorrado = null;
		BigDecimal totalMontoDisponibleGasto = null;
		BigDecimal totalMontoPrestamo = null;
		BigDecimal totalMontoInvertido = null;
		
			
		
		try {
			totalMontoAhorrado = ahorroService.calcularAhorro();
			totalMontoDisponibleGasto = gastoService.calcularMontoDisponible();
			totalMontoPrestamo = prestamoService.calcularPrestamo();
			totalMontoInvertido = inversionService.calcularMonto();
		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar el calculo en el detalle de ingresos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("ahorro", totalMontoAhorrado != null ? totalMontoAhorrado : 0 );
		response.put("gastos", totalMontoDisponibleGasto != null ? totalMontoDisponibleGasto : 0);
		response.put("prestamo", totalMontoPrestamo != null ? totalMontoPrestamo : 0);
		response.put("inversion", totalMontoInvertido != null ? totalMontoInvertido : 0);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	

}
