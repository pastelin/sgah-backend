package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_INVERSION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.springboot.sgah.backend.apirest.models.dto.InversionDto;
import com.springboot.sgah.backend.apirest.models.dto.ProductoFinancieroDto;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperInversion;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperProductoFinanciero;
import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.services.InversionService;

import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/", "http://localhost:5175/" })
@RestController
@RequestMapping("/sgah/v0/inversion")
public class InversionRestController {

	InversionService inversionService;

	public InversionRestController() {
	}

	@Autowired
	public InversionRestController(InversionService inversionService) {
		this.inversionService = inversionService;
	}

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> findInversiones() {

		Map<String, Object> response = new HashMap<>();
		List<InversionDto> inversionesDto = new ArrayList<>();

		try {
			List<Inversion> inversiones = inversionService.findAllInversion();

			for (Inversion inversion : inversiones) {
				inversionesDto.add(DtoMapperInversion.builder().setInversion(inversion)
						.buildInversionDto());
			}

		} catch (DataAccessException e) {
			e.printStackTrace();
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("inversiones", inversionesDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/productosFinancieros")
	public ResponseEntity<Map<String, Object>> findProductosFinancieros() {

		Map<String, Object> response = new HashMap<>();
		List<ProductoFinancieroDto> productosFinancierosDto = new ArrayList<>();

		try {
			List<ProductoFinanciero> productosFinancieros = inversionService.findAllProductosFinancieros();

			for (ProductoFinanciero productoFinanciero : productosFinancieros) {
				productosFinancierosDto
						.add(DtoMapperProductoFinanciero.builder().setProductoFinanciero(productoFinanciero)
								.buildProductoFinancieroDto());

			}
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("productosFinancieros", productosFinancierosDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/increment")
	public ResponseEntity<Map<String, Object>> agregarInversion(@Valid @RequestBody InversionDto inversion,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			Optional<ProductoFinanciero> productoFinanciero = inversionService
					.findProductoFinancieroById(inversion.getProductoFinanciero().getCdApp());

			if (!productoFinanciero.isPresent()) {
				response.put(TEXT_MENSAJE, "No se encontró el producto financiero!");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			inversionService.saveInversion(
					DtoMapperInversion.builder().setInversionDto(inversion).buildInversion(productoFinanciero.get()));

			response.put(TEXT_MENSAJE, "El incremento de $" + inversion.getMonto() + " fue éxito!");
			response.put("fecha", inversion.getFechaCreacion());
			response.put("folio", inversion.getFolio());
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/retiro")
	public ResponseEntity<Map<String, Object>> retirarInversion(@Valid @RequestBody InversionDto inversion,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			BigDecimal saldoDisponible = inversionService.obtenerMontoActual(inversion.getFolio());
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

			Optional<ProductoFinanciero> productoFinanciero = inversionService
					.findProductoFinancieroById(inversion.getProductoFinanciero().getCdApp());

			if (!productoFinanciero.isPresent()) {
				response.put(TEXT_MENSAJE, "No se encontró el producto financiero!");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			inversionService.updateInversion(
					DtoMapperInversion.builder().setInversionDto(inversion).buildInversion(productoFinanciero.get()));
		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "El retiro fue éxito!");
		response.put("monto", inversion.getMonto());
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
		InversionDto inversionDto = null;

		try {
			Inversion inversion = inversionService.obtenerInversion(folio);
			inversionDto = DtoMapperInversion.builder().setInversion(inversion)
					.buildInversionDto();
		} catch (DataAccessException ex) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(ex), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_INVERSION, inversionDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
