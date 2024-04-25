package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_INVERSION;

import java.math.BigDecimal;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.InversionDto;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.services.InversionService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/sgah/v0/inversion")
public class InversionRestController {

	@Autowired
	InversionService inversionService;

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> findInversiones() {

		Map<String, Object> response = new HashMap<>();
		List<Inversion> inversiones = null;
		List<InversionDto> inversionesDto = null;

		try {
			inversiones = inversionService.findAllInversion();
			inversionesDto = inversionService.updateDescripcionInversion(inversiones);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (inversionesDto == null || inversionesDto.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de inversiones");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("inversiones", inversionesDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/productosFinancieros")
	public ResponseEntity<Map<String, Object>> findProductosFinancieros() {

		Map<String, Object> response = new HashMap<>();
		List<ProductoFinanciero> productosFinancieros = null;

		try {
			productosFinancieros = inversionService.findAllProductosFinancieros();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (productosFinancieros == null || productosFinancieros.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información en el catalodo de app para inversiones");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("productosFinancieros", productosFinancieros);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/incremento")
	public ResponseEntity<Map<String, Object>> agregarInversion(@Valid @RequestBody Inversion inversion,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		InversionDto inversionDto = null;

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			inversionService.saveInversion(inversion);
			inversionDto = inversionService.updateDescripcionInversion(Arrays.asList(inversion)).get(0);

		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "El incremento de $" + inversion.getMonto() + " fue éxito!");
		response.put(TEXT_INVERSION, inversionDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/retiro")
	public ResponseEntity<Map<String, Object>> retirarInversion(@Valid @RequestBody Inversion inversion,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		BigDecimal saldoDisponible = null;
		Inversion inversionUpdated = null;

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			saldoDisponible = inversionService.obtenerMontoActual(inversion.getFolio());
			BigDecimal saldoRetirado = inversion.getMonto();

			if (saldoDisponible == null) {
				response.put(TEXT_MENSAJE, "No se pudo obtener el saldo de la inversión.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			if (saldoRetirado.compareTo(new BigDecimal(0)) == 0) {
				response.put(TEXT_MENSAJE, "El saldo a retirar sebe ser mayor a $0!");
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			} else if (saldoRetirado.compareTo(saldoDisponible) >= 1) {
				response.put(TEXT_MENSAJE, "El saldo a retirar no debe ser mayor al saldo disponible!");
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			inversion.setMonto(saldoDisponible.subtract(saldoRetirado));
			inversionUpdated = inversionService.updateInversion(inversion);
		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "El retiro fue éxito!");
		response.put(TEXT_INVERSION, inversionUpdated);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping("/saldo")
	public ResponseEntity<Map<String, Object>> obtenerSaldoInvertido() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal saldoInvertido = null;

		try {
			saldoInvertido = inversionService.calcularMonto();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("saldoInvertido", saldoInvertido);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{folio}")
	public ResponseEntity<Map<String, Object>> obtenerInversionByFolio(@PathVariable String folio) {

		Map<String, Object> response = new HashMap<>();
		Inversion inversion = null;

		try {
			inversion = inversionService.obtenerInversion(folio);
		} catch (DataAccessException ex) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(ex), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (inversion == null) {
			response.put(TEXT_MENSAJE, "No se encontró la inversion con folio: " + folio + " en la base de datos.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put(TEXT_INVERSION, inversion);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
