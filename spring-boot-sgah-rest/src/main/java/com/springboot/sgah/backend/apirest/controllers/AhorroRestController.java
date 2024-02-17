package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_ERROR;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.entities.Ahorro;
import com.springboot.sgah.backend.apirest.rm.LocalDateUtil;
import com.springboot.sgah.backend.apirest.services.AhorroService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/ahorro/v0/ahorro")
public class AhorroRestController {

	@Autowired
	AhorroService ahorroService;

	@GetMapping("/detalle1")
	public ResponseEntity<?> findAhorroByCurrentMonth() {

		Map<String, Object> response = new HashMap<>();
		List<Ahorro> ahorros = null;

		try {
			ahorros = ahorroService.findAhorroByCurrentMonth(LocalDateUtil.getMonth(null), LocalDateUtil.getYear(null));
		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ahorros == null || ahorros.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de ahorros");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(ahorros, HttpStatus.OK);

	}

	@GetMapping("/detalle")
	public ResponseEntity<?> findAllAhorro() {

		Map<String, Object> response = new HashMap<>();
		List<Ahorro> ahorros = null;

		try {
			ahorros = ahorroService.findAllAhorro();
		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ahorros == null || ahorros.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de ahorros");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(ahorros, HttpStatus.OK);
	}

	@GetMapping("/saldo")
	public ResponseEntity<?> calcularSaldoDisponible() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal totalAhorro = null;

		try {

			totalAhorro = ahorroService.calcularAhorro();

		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar el calculo del ahorro en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (totalAhorro == null) {
			response.put(TEXT_MENSAJE, "No hay información para realizar el calculo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(totalAhorro, HttpStatus.OK);
	}

	@PostMapping("/agrega")
	public ResponseEntity<?> guardarAhorro(@Valid @RequestBody Ahorro ahorro, BindingResult result) {
		Ahorro newAhorro = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			newAhorro = ahorroService.saveAhorro(ahorro);
		} catch(DataException e) {
			response.put(TEXT_MENSAJE, "Error al guardar en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(TEXT_MENSAJE, "Ahorro guardado con éxito!");
		response.put("ahorro", newAhorro);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
}
