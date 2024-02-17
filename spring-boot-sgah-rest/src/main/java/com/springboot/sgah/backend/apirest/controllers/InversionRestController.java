package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_ERROR;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.entities.CatalogoAppInversion;
import com.springboot.sgah.backend.apirest.entities.Inversion;
import com.springboot.sgah.backend.apirest.entities.InversionDto;
import com.springboot.sgah.backend.apirest.services.InversionService;;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/inversion/v0/inversion")
public class InversionRestController {
	
	@Autowired
	InversionService inversionService;
	
	@GetMapping("/detalle")
	public ResponseEntity<?> findAllInversion() {
		
		Map<String, Object> response = new HashMap<>();
		List<Inversion> inversiones = null;
		List<InversionDto> inversionesDto = null;
		
		try {
			inversiones = inversionService.findAllInversion();
			inversionesDto = inversionService.updateDescripcionInversion(inversiones);
			
		} catch(DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(inversionesDto == null || inversionesDto.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de inversiones");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(inversionesDto, HttpStatus.OK);
	}
	

	@GetMapping("/consultaApp")
	public ResponseEntity<?> findAllAppInversion() {
		
		Map<String, Object> response = new HashMap<>();
		List<CatalogoAppInversion> appInversiones = null;
		
		try {
			appInversiones = inversionService.findAllCatalogoAppInversion();
		} catch(DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(appInversiones == null || appInversiones.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información en el catalodo de app para inversiones");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(appInversiones, HttpStatus.OK);
	}
	
	
	@PostMapping("/operacionAgregar")
	public ResponseEntity<Map<String, Object>> agregarInversion(@Valid @RequestBody Inversion inversion, BindingResult result) {
		
		Map<String, Object> response = new HashMap<>();
		InversionDto inversionDto = null;
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			// Implementar que el SP regrese valores
			inversionService.saveInversion(inversion);
			inversionDto = inversionService.updateDescripcionInversion(Arrays.asList(inversion)).get(0);
			
		} catch(DataException e) {
			response.put(TEXT_MENSAJE, "Error al guardar en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(TEXT_MENSAJE, "Inversion guardada con éxito!");
		response.put("inversion", inversionDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/operacionRetiro")
	public ResponseEntity<Map<String, Object>> retirarInversion(@Valid @RequestBody Inversion inversion, BindingResult result) {
		
		Map<String, Object> response = new HashMap<>();
		BigDecimal montoActual = null;
		Inversion inversionUpdated = null;
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			montoActual = inversionService.obtenerMontoActual(inversion.getFolio());
			BigDecimal montoNuevo = inversion.getMonto();
			
			if(montoActual == null) {
				response.put(TEXT_MENSAJE, "Error al obtener el monto de inversión en base de datos.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			
			if(montoNuevo.compareTo(montoActual) >= 1) {
				response.put(TEXT_MENSAJE, "El monto a retirar es mayor al monto de la inversión!");
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}
			
			inversion.setMonto(montoActual.subtract(montoNuevo));
			inversionUpdated = inversionService.updateInversion(inversion);
		} catch(DataException e) {
			response.put(TEXT_MENSAJE, "Error al guardar en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(TEXT_MENSAJE, "Retiro de inversion realizado con exito!");
		response.put("inversion", inversionUpdated);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/saldoInvertido")
	public ResponseEntity<?> calcularTotalInversion() {
		
		Map<String, Object> response = new HashMap<>();
		BigDecimal montoInvertido = null;
		
		try {
			montoInvertido = inversionService.calcularMonto();
		} catch(DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(montoInvertido, HttpStatus.OK);
	}
	
	@GetMapping("/detalleInversion/{folio}")
	public ResponseEntity<?> obtenerInversionByFolio(@PathVariable String folio) {

		Map<String, Object> response = new HashMap<>();
		Inversion inversion = null;

		try {
			inversion = inversionService.obtenerInversion(folio);
		} catch (DataAccessException ex) {
			response.put(TEXT_MENSAJE, "Error al realizar consulta en base de datos");
			response.put(TEXT_ERROR,
					ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (inversion == null) {
			response.put(TEXT_MENSAJE, "No existe la inversion");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(inversion, HttpStatus.OK);
	}

	
}
