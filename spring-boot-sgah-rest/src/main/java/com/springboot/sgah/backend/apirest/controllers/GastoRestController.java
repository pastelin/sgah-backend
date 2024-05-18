package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

import com.springboot.sgah.backend.apirest.models.dto.GastoDto;
import com.springboot.sgah.backend.apirest.models.dto.GastoRecurrenteDto;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperGasto;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperGastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.rm.LocalDateUtil;
import com.springboot.sgah.backend.apirest.services.GastoService;

@CrossOrigin(origins = { "http://localhost:5173/" })
@RestController
@RequestMapping("/sgah/v0/gasto")
public class GastoRestController { 

	@Autowired
	GastoService gastoService;

	@GetMapping("/")
	public ResponseEntity<Map<String, Object>> findGastoByCurrentMonth() {
		Map<String, Object> response = new HashMap<>();
		List<GastoDto> gastosDto = new ArrayList<>();

		try {
			List<Gasto> gastos = gastoService.findGastoByCurrentMonth(LocalDateUtil.getMonth(null),
					LocalDateUtil.getYear(null));

			for (Gasto gasto : gastos) {
				gastosDto.add(
						DtoMapperGasto.builder().setGasto(gasto).buildGastoDto(gastoService.findAllGastoRecurrente()));
			}
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("gastos", gastosDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/montos")
	public ResponseEntity<Map<String, Object>> obtenerMontos() {

		Map<String, Object> response = new HashMap<>();
		BigDecimal montoDisponible = null;
		BigDecimal montoGastado = null;

		try {

			montoDisponible = gastoService.calcularMontoDisponible();
			montoGastado = gastoService.obtenerGastoMensual(LocalDateUtil.getMonth(null), LocalDateUtil.getYear(null));

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (montoDisponible == null) {
			response.put(TEXT_MENSAJE, "No hay información para realiza el calculo");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put("montoDisponible", montoDisponible);
		response.put("montoGastado", montoGastado);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> guardarGasto(@Valid @RequestBody GastoDto gasto, BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			BigDecimal saldoDisponible = gastoService.calcularMontoDisponible();

			if (gasto.getTipoMovimiento().getCdTipo() == 2 && gasto.getMonto().compareTo(saldoDisponible) >= 1) {
				response.put(TEXT_MENSAJE, "El monto ingresado no debe ser mayor a $ " + saldoDisponible);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			gastoService.saveGasto(DtoMapperGasto.builder().setGastoDto(gasto).buiGasto());

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put(TEXT_MENSAJE, "Gasto guardado con éxito!");
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@GetMapping("/categoria")
	public ResponseEntity<Map<String, Object>> obtenerGastoRecurrente() {

		Map<String, Object> response = new HashMap<>();
		List<GastoRecurrenteDto> gastosRecurrentesDto = new ArrayList<>();

		try {
			List<GastoRecurrente> gastosRecurrentes = gastoService.findAllGastoRecurrente();

			for (GastoRecurrente gastoRecurrente : gastosRecurrentes) {
				gastosRecurrentesDto.add(DtoMapperGastoRecurrente.builder().setGastoRecurrente(gastoRecurrente)
						.buildGastoRecurrenteDto());
			}

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("gastosRecurrentes", gastosRecurrentesDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
