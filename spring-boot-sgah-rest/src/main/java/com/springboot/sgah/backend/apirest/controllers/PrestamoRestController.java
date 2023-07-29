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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.entities.Gasto;
import com.springboot.sgah.backend.apirest.entities.Prestamo;
import com.springboot.sgah.backend.apirest.services.AhorroService;
import com.springboot.sgah.backend.apirest.services.GastoService;
import com.springboot.sgah.backend.apirest.services.PrestamoService;

@CrossOrigin(origins = { "http://127.0.0.1:5173/" })
@RestController
@RequestMapping("/prestamo/v0/prestamo")
public class PrestamoRestController {

	@Autowired
	PrestamoService prestamoService;

	@Autowired
	AhorroService ahorroService;

	@Autowired
	GastoService gastoService;

	@GetMapping("/detallePrestamosActivos")
	public ResponseEntity<?> listarPrestamoActivo() {

		Map<String, Object> response = new HashMap<>();
		List<Prestamo> prestamos = null;

		try {
			prestamos = prestamoService.listarPrestamoActivo();
		} catch (DataAccessException ex) {
			response.put(TEXT_MENSAJE, "Error al realizar consulta en base de datos");
			response.put(TEXT_ERROR,
					ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (prestamos == null || prestamos.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay prestamos activos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(prestamos, HttpStatus.OK);
	}
	
	@GetMapping("/detallePrestamo/{folio}")
	public ResponseEntity<?> obtenerPrestamoByFolio(@PathVariable String folio) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamos = null;

		try {
			prestamos = prestamoService.obtenerPrestamo(folio);
		} catch (DataAccessException ex) {
			response.put(TEXT_MENSAJE, "Error al realizar consulta en base de datos");
			response.put(TEXT_ERROR,
					ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (prestamos == null) {
			response.put(TEXT_MENSAJE, "No existe el prestamo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(prestamos, HttpStatus.OK);
	}
	
	@GetMapping("/saldoUtilizado")
	public ResponseEntity<?> obtenerSaldoUtilizado() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal saldoUtilizado = null;

		try {
			saldoUtilizado = prestamoService.calcularPrestamo();
		} catch (DataAccessException ex) {
			response.put(TEXT_MENSAJE, "Error al realizar consulta en base de datos");
			response.put(TEXT_ERROR,
					ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(saldoUtilizado, HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<Map<String, Object>> agregarPrestamo(@Valid @RequestBody Prestamo prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamoSaved = null;

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			BigDecimal montoAhorrado = ahorroService.calcularAhorro();

			if (montoAhorrado == null) {
				response.put(TEXT_MENSAJE, "Error al obtener el monto del ahorro en base de datos.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			if (prestamo.getMontoPrestado().compareTo(montoAhorrado) >= 1) {
				response.put(TEXT_MENSAJE, "El monto a prestar no debe ser mayor a $" + montoAhorrado);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			prestamoSaved = prestamoService.agregarPrestamo(prestamo);

			Gasto gasto = new Gasto();
			gasto.setDescripcion(prestamo.getDescripcion());
			gasto.setMonto(prestamo.getMontoPrestado());

			gastoService.saveGasto(gasto);

		} catch (DataException ex) {
			response.put(TEXT_MENSAJE, "Error al guardar en base de datos");
			response.put(TEXT_ERROR, ex.getMessage().concat(": ").concat(ex.getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo guardado con éxito!");
		response.put("prestamo", prestamoSaved);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/operacionActualiza")
	public ResponseEntity<Map<String, Object>> actualizarPrestamo(@Valid @RequestBody Prestamo prestamo,
			BindingResult result) {

		Map<String, Object> response = new HashMap<>();
		Prestamo prestamoUpdated = null;

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			Prestamo prestamoAnterior = prestamoService.obtenerPrestamo(prestamo.getFolio());

			if (prestamoAnterior == null) {
				response.put(TEXT_MENSAJE, "Error al obtener el prestamo en base de datos.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			BigDecimal montoPrestado = prestamoAnterior.getMontoPrestado();
			BigDecimal montoPagado = prestamoAnterior.getMontoPagado();
			BigDecimal montoDeuda = montoPrestado.subtract(montoPagado);

			if (prestamo.getMontoPagado().compareTo(montoDeuda) >= 1) {
				response.put(TEXT_MENSAJE, "El monto a pagar no puede ser mayor a $" + montoDeuda);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			BigDecimal montoGastoDisponible = gastoService.calcularMontoDisponible();

			if (prestamo.getMontoPagado().compareTo(montoGastoDisponible) >= 1) {
				response.put(TEXT_MENSAJE, "El monto a pagar no puede ser mayor a $" + montoGastoDisponible);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			Gasto gasto = new Gasto();
			gasto.setCdTipoMovimiento(2);
			gasto.setMonto(prestamo.getMontoPagado());
			gasto.setCdGastoRecurrente(11);
			gasto.setDescripcion(prestamo.getDescripcion());

			prestamo.setMontoPagado(prestamo.getMontoPagado().add(montoPagado));

			if (montoPrestado.compareTo(prestamo.getMontoPagado()) == 0) {
				prestamo.setCdEstatus(2);
			}

			prestamoUpdated = prestamoService.actualizarPrestamo(prestamo);
			gastoService.saveGasto(gasto);

		} catch (DataException ex) {
			response.put(TEXT_MENSAJE, "Error al guardar en base de datos");
			response.put(TEXT_ERROR, ex.getMessage().concat(": ").concat(ex.getLocalizedMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Prestamo actualizado con éxito!");
		response.put("prestamo", prestamoUpdated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
