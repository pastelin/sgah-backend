package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_PRESTAMO;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.entities.Gasto;
import com.springboot.sgah.backend.apirest.entities.Prestamo;
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
		List<Prestamo> prestamos = null;

		try {
			prestamos = prestamoService.listarPrestamoActivo();
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (prestamos == null || prestamos.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de prestamos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("prestamos", prestamos);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{folio}")
	public ResponseEntity<Map<String, Object>> obtenerPrestamoByFolio(@PathVariable String folio) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamo = null;

		try {
			prestamo = prestamoService.obtenerPrestamo(folio);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (prestamo == null) {
			response.put(TEXT_MENSAJE, "No hay información del prestamo.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put(TEXT_PRESTAMO, prestamo);
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

	// TODO: Analizar si se puede refactorizar este método (Reglas de negocio)
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> agregarPrestamo(@Valid @RequestBody Prestamo prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamoSaved = null;

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			BigDecimal saldoAhorrado = ahorroService.calcularAhorro();

			if (saldoAhorrado == null) {
				response.put(TEXT_MENSAJE, "No hay información del saldo ahorrado.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			if (prestamo.getMontoPrestado().compareTo(saldoAhorrado) >= 1) {
				response.put(TEXT_MENSAJE, "El monto no debe ser mayor a $" + saldoAhorrado);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			prestamoSaved = prestamoService.agregarPrestamo(prestamo);

			// TODO: Refactorizar para que lo ejecute la capa de GASTO
			Gasto gasto = new Gasto();
			gasto.setDescripcion(prestamo.getDescripcion());
			gasto.setMonto(prestamo.getMontoPrestado());
			gasto.setCdGastoRecurrente(11);

			gastoService.saveGasto(gasto);
		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo guardado con éxito!");
		response.put(TEXT_PRESTAMO, prestamoSaved);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<Map<String, Object>> actualizarPrestamo(@Valid @RequestBody Prestamo prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamoUpdated = null;

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

			if (prestamo.getMontoPagado().compareTo(saldoDeuda) >= 1
					|| prestamo.getMontoPagado().compareTo(saldoDisponibleGasto) >= 1) {
				response.put(TEXT_MENSAJE, "El monto no debe ser mayor a la deuda actual o saldo disponible.");
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			Gasto gasto = new Gasto();
			gasto.setCdTipoMovimiento(2);
			gasto.setMonto(prestamo.getMontoPagado());
			gasto.setCdGastoRecurrente(11);
			gasto.setDescripcion(prestamo.getDescripcion());

			prestamo.setMontoPagado(prestamo.getMontoPagado().add(saldoPagado));

			if (saldoPrestado.compareTo(prestamo.getMontoPagado()) == 0) {
				prestamo.setCdEstatus(2);
			}

			prestamoUpdated = prestamoService.actualizarPrestamo(prestamo);
			gastoService.saveGasto(gasto);

		} catch (DataException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo actualizado con éxito!");
		response.put(TEXT_PRESTAMO, prestamoUpdated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
