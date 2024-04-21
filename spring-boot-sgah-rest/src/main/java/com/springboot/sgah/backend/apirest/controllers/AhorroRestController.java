package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.services.AhorroService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/sgah/v0/ahorro")
public class AhorroRestController {

	@Autowired
	AhorroService ahorroService;

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> findAllAhorro() {

		Map<String, Object> response = new HashMap<>();
		List<Ahorro> ahorros = null;

		try {
			ahorros = ahorroService.findAllAhorro();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (ahorros == null || ahorros.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de ahorros");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("ahorros", ahorros);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/saldo")
	public ResponseEntity<Map<String, Object>> calcularSaldoDisponible() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal saldoDisponible = null;

		try {

			saldoDisponible = ahorroService.calcularAhorro();

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (saldoDisponible == null) {
			response.put(TEXT_MENSAJE, "No hay información para realizar el calculo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("saldoDisponible", saldoDisponible);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> guardarAhorro(@Valid @RequestBody Ahorro ahorro, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			ahorroService.saveAhorro(ahorro);
		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Ahorro guardado con éxito!");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

}
