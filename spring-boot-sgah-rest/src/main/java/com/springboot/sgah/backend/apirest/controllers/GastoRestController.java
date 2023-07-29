package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_ERROR;
import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import com.springboot.sgah.backend.apirest.entities.GastoDto;
import com.springboot.sgah.backend.apirest.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.rm.LocalDateUtil;
import com.springboot.sgah.backend.apirest.services.GastoService;

@CrossOrigin(origins = { "http://127.0.0.1:5173/" })
@RestController
@RequestMapping("/gasto/v0/gasto")
public class GastoRestController {

	@Autowired
	GastoService gastoService;

	@GetMapping("/detalle")
	public ResponseEntity<?> findGastoByCurrentMonth() {

		Map<String, Object> response = new HashMap<>();
		List<Gasto> gastos = null;
		List<GastoDto> newGastos = null;

		try {
			gastos = gastoService.findGastoByCurrentMonth(LocalDateUtil.getMonth(null), LocalDateUtil.getYear(null));
			newGastos = gastoService.updateDescripcionGasto(gastos);
		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consula en la base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (gastos == null || gastos.isEmpty()) {
			response.put(TEXT_MENSAJE, "No hay información de gastos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(newGastos, HttpStatus.OK);
	}

	@GetMapping("/calculo")
	public ResponseEntity<?> calcularGastoDisponible() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal montoDisponible = null;

		try {

			montoDisponible = gastoService.calcularMontoDisponible();

		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar el calculo del monto disponible en base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (montoDisponible == null) {
			response.put(TEXT_MENSAJE, "No hay información para realiza el calculo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(montoDisponible, HttpStatus.OK);
	}

	@GetMapping("/montos")
	public ResponseEntity<?> obtenerMontos() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal montoDisponible = null;
		BigDecimal montoGastado = null;

		try {

			montoDisponible = gastoService.calcularMontoDisponible();
			montoGastado = gastoService.obtenerGastoMensual(LocalDateUtil.getMonth(null), LocalDateUtil.getYear(null));

		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar el calculo del monto disponible en base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (montoDisponible == null) {
			response.put(TEXT_MENSAJE, "No hay información para realiza el calculo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("montoDisponible", montoDisponible);
		response.put("montoGastado", montoGastado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/filtro/{id}")
	public ResponseEntity<?> filtrarGasto(@PathVariable String id, Integer value) {

		Map<String, Object> response = new HashMap<>();
		List<Gasto> gastos = null;

		try {

			switch (id) {
			case "todo":
				gastos = gastoService.findAllGasto();
				break;
			case "categoria":
				gastos = gastoService.findGastoByCategoria(value);
				break;
			case "tipo movimiento":
				gastos = gastoService.findGastoByTipo(value);
				break;

			default:
				break;
			}

		} catch (DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al realizar la consulta en base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(gastos == null) {
			response.put(TEXT_MENSAJE, "No se obtuvo información en base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(gastos, HttpStatus.OK);
	}

	@PostMapping("/agrega")
	public ResponseEntity<Map<String, Object>> guardarGasto(@Valid @RequestBody Gasto gasto, BindingResult result) {
		
		Map<String, Object> response = new HashMap<>();
		GastoDto gastoDto = null;
		BigDecimal montoDisponible = null;
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			montoDisponible = gastoService.calcularMontoDisponible();
			
			if(gasto.getMonto().compareTo(montoDisponible) >= 1) {
				response.put(TEXT_MENSAJE, "El monto ingresado no debe ser mayor a $ " + montoDisponible);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}
			
			gastoService.saveGasto(gasto);
			gastoDto = gastoService.updateDescripcionGasto(Arrays.asList(gasto)).get(0);
			
		} catch(DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al guardar en base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(TEXT_MENSAJE, "Gasto guardado con éxito!");
		response.put("gasto", gastoDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@GetMapping("/categoria")
	public ResponseEntity<?> obtenerGastoRecurrente() {
		
		Map<String, Object> response = new HashMap<>();
		List<GastoRecurrente> gastosRecurrentes = null;
		
		try {
			gastosRecurrentes = gastoService.findAllGastoRecurrente();
		} catch(DataAccessException e) {
			response.put(TEXT_MENSAJE, "Error al consultar en base de datos");
			response.put(TEXT_ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(gastosRecurrentes == null || gastosRecurrentes.isEmpty()) {
			response.put(TEXT_MENSAJE, "No se obtuvo información en base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(gastosRecurrentes, HttpStatus.OK);
	}
}
