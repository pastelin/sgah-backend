package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_PRESTAMO;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.models.dto.PrestamoDto;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperPrestamo;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.Prestamo;
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.services.AhorroService;
import com.springboot.sgah.backend.apirest.services.GastoService;
import com.springboot.sgah.backend.apirest.services.PrestamoService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/sgah/v0/prestamo")
public class PrestamoRestController {

	@Autowired
	PrestamoService prestamoService;

	@Autowired
	AhorroService ahorroService;

	@Autowired
	GastoService gastoService;

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> listarPrestamoActivo() {

		Map<String, Object> response = new HashMap<>();
		List<PrestamoDto> prestamosDto = new ArrayList<>();

		try {
			List<Prestamo> prestamos = prestamoService.listarPrestamoActivo();

			for (Prestamo prestamo : prestamos) {
				prestamosDto.add(DtoMapperPrestamo.builder().setPrestamo(prestamo).buildPrestamoDto());
			}

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("prestamos", prestamosDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{folio}")
	public ResponseEntity<Map<String, Object>> obtenerPrestamoByFolio(@PathVariable String folio) {

		Map<String, Object> response = new HashMap<>();
		PrestamoDto prestamoDto = null;

		try {
			prestamoDto = DtoMapperPrestamo.builder().setPrestamo(prestamoService.obtenerPrestamo(folio))
					.buildPrestamoDto();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_PRESTAMO, prestamoDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/saldoUtilizado")
	public ResponseEntity<Map<String, Object>> obtenerSaldoUtilizado() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal saldoUtilizado = null;

		try {
			saldoUtilizado = prestamoService.calcularPrestamo();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("saldoUtilizado", saldoUtilizado);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> agregarPrestamo(@Valid @RequestBody PrestamoDto prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			BigDecimal saldoAhorrado = ahorroService.calcularAhorro();

			if (saldoAhorrado == null) {
				response.put(TEXT_MENSAJE, "No hay información del saldo ahorrado.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			if (prestamo.getSaldoPrestado().compareTo(saldoAhorrado) >= 1) {
				response.put(TEXT_MENSAJE, "El monto no debe ser mayor a $" + saldoAhorrado);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			prestamoService.agregarPrestamo(DtoMapperPrestamo.builder().setPrestamoDto(prestamo)
					.buildPrestamo());

			gastoService.saveGasto(new Gasto(prestamo.getSaldoPrestado(), prestamo.getDescripcion(), 11, 1));
		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo guardado con éxito!");
		response.put("folio", prestamo.getFolio());
		response.put("fechaCreacion", prestamo.getFechaCreacion());
		response.put("saldoPagado", prestamo.getSaldoPagado());
		response.put("cdEstatus", prestamo.getCdEstatus());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<Map<String, Object>> actualizarPrestamo(@Valid @RequestBody PrestamoDto prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			Prestamo prestamoAnterior = prestamoService.obtenerPrestamo(prestamo.getFolio());

			if (prestamoAnterior == null) {
				response.put(TEXT_MENSAJE, "Error al obtener el prestamo en base de datos.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			BigDecimal saldoPrestado = prestamoAnterior.getMontoPrestado();
			BigDecimal saldoPagado = prestamoAnterior.getMontoPagado();
			BigDecimal saldoDeuda = saldoPrestado.subtract(saldoPagado);
			BigDecimal saldoDisponibleGasto = gastoService.calcularMontoDisponible();

			if (prestamo.getSaldoPagado().compareTo(saldoDeuda) >= 1
					|| prestamo.getSaldoPagado().compareTo(saldoDisponibleGasto) >= 1) {
				response.put(TEXT_MENSAJE, "El monto no debe ser mayor a la deuda actual o saldo disponible.");
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}


			BigDecimal currentSaldoPagado = prestamo.getSaldoPagado().add(saldoPagado);
			
			if (saldoPrestado.compareTo(currentSaldoPagado) == 0) {
				prestamo.setCdEstatus(2);
			}

			gastoService.saveGasto(new Gasto(prestamo.getSaldoPagado(), prestamo.getDescripcion(), 11, 2));
			
			prestamo.setSaldoPagado(currentSaldoPagado);
			prestamoService.actualizarPrestamo(DtoMapperPrestamo.builder().setPrestamoDto(prestamo)
					.buildPrestamo());


		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo actualizado con éxito!");
		response.put("saldoPagado", prestamo.getSaldoPagado());
		response.put("cdEstatus", prestamo.getCdEstatus());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
