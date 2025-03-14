package com.springboot.sgah.backend.apirest.controllers;

import static com.springboot.sgah.backend.apirest.rm.Constants.TEXT_MENSAJE;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.models.dto.GastoDto;
import com.springboot.sgah.backend.apirest.models.dto.GastoRecurrenteDto;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByMonth;
import com.springboot.sgah.backend.apirest.models.dto.HistoricalBalanceByYear;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperGasto;
import com.springboot.sgah.backend.apirest.models.dto.mapper.DtoMapperGastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.OrigenMovimiento;
import com.springboot.sgah.backend.apirest.rm.ErrorMessageUtil;
import com.springboot.sgah.backend.apirest.rm.LocalDateUtil;
import com.springboot.sgah.backend.apirest.services.GastoService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = { "http://localhost:5173/", "http://localhost:5174/", "http://localhost:5175/" })
@RestController
@RequestMapping("/sgah/v0/gasto")
public class GastoRestController {

	GastoService gastoService;

	public GastoRestController() {
	}

	@Autowired
	public GastoRestController(GastoService gastoService) {
		this.gastoService = gastoService;
	}

	@GetMapping("/{year}")
	public ResponseEntity<Map<String, Object>> findHistoricalBalanceByYear(@PathVariable Integer year) {
		try {
			List<HistoricalBalanceByYear> historicalBalance = gastoService.findHistoricalBalanceByYear(year);
			Map<String, Object> response = Collections.singletonMap("historicalBalance", historicalBalance);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{year}/{month}")
	public ResponseEntity<Map<String, Object>> findGastoByMonth(@PathVariable Integer year,
			@PathVariable Integer month) {
		try {
			List<Gasto> gastos = gastoService.findGastoByMonth(month, year);
			List<GastoDto> gastosDto = gastos.stream()
					.map(gasto -> DtoMapperGasto.builder().setGasto(gasto).buildGastoDto())
					.toList();

			Map<String, Object> response = Collections.singletonMap("gastos", gastosDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/historical/{year}/{month}")
	public ResponseEntity<Map<String, Object>> findHistoricalBalanceByMonth(@PathVariable Integer year,
			@PathVariable Integer month) {
		try {
			List<HistoricalBalanceByMonth> historicalBalance = gastoService.findHistoricalBalanceByMonth(month, year);
			Map<String, Object> response = Collections.singletonMap("historicalBalance", historicalBalance);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/montos")
	public ResponseEntity<Map<String, Object>> obtenerMontos() {
		Map<String, Object> response = new HashMap<>();

		try {
			BigDecimal montoDisponible = gastoService.calcularMontoDisponible();
			BigDecimal montoGastado = gastoService.calculateExpensesByMonthAndYear(LocalDateUtil.getMonth(null),
					LocalDateUtil.getYear(null));

			if (montoDisponible == null) {
				response.put(TEXT_MENSAJE, "No hay información para realizar el cálculo");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			response.put("montoDisponible", montoDisponible);
			response.put("montoGastado", montoGastado);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> guardarGasto(@Valid @RequestBody GastoDto gasto, BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {
			Optional<GastoRecurrente> gastoRecurrente = gastoService
					.findGastoRecurrenteById(gasto.getGastoRecurrente().getCdGasto());

			Optional<OrigenMovimiento> origenMovimiento = gastoService
					.findOrigenMovimientoById(gasto.getOrigenMovimiento().getId());

			if (!gastoRecurrente.isPresent() || !origenMovimiento.isPresent()) {
				response.put(TEXT_MENSAJE, "No se encontró la categoría del gasto");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			BigDecimal saldoDisponible = gastoService.calcularMontoDisponible();

			if (gasto.getOrigenMovimiento().getId() == 2 && gasto.getAmount().compareTo(saldoDisponible) >= 1) {
				response.put(TEXT_MENSAJE, "El monto ingresado no debe ser mayor a $ " + saldoDisponible);
				return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
			}

			Gasto gastoEntity = DtoMapperGasto.builder().setGastoDto(gasto).buiGasto(gastoRecurrente.get(),
					origenMovimiento.get());
			gastoService.saveGasto(gastoEntity);

			response.put(TEXT_MENSAJE, "Gasto guardado con éxito!");
			response.put("gasto", DtoMapperGasto.builder().setGasto(gastoEntity).buildGastoDto());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/categoria")
	public ResponseEntity<Map<String, Object>> obtenerGastoRecurrente() {

		try {
			List<GastoRecurrente> gastosRecurrentes = gastoService.findAllGastoRecurrente();
			List<GastoRecurrenteDto> gastosRecurrentesDto = gastosRecurrentes.stream()
					.map(gastoRecurrente -> DtoMapperGastoRecurrente.builder().setGastoRecurrente(gastoRecurrente)
							.buildGastoRecurrenteDto())
					.toList();

			Map<String, Object> response = Collections.singletonMap("gastosRecurrentes", gastosRecurrentesDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody GastoDto gasto,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			return new ResponseEntity<>(ErrorMessageUtil.getFieldsErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		try {

			GastoRecurrente gastoRecurrente = new GastoRecurrente(gasto.getGastoRecurrente().getCdGasto(),
					gasto.getGastoRecurrente().getNbGasto());

			OrigenMovimiento origenMovimiento = new OrigenMovimiento(gasto.getOrigenMovimiento().getId(),
					gasto.getOrigenMovimiento().getDescripcion());

			Gasto gastoEntity = DtoMapperGasto.builder().setGastoDto(gasto).buiGasto(
					gastoRecurrente, origenMovimiento);

			Optional<Gasto> gastoOptional = gastoService.editExpense(id, gastoEntity);

			if (gastoOptional.isPresent()) {
				response.put(TEXT_MENSAJE, "¡Gasto modificado exitosamente!");
				response.put("expense", DtoMapperGasto.builder().setGasto(gastoEntity).buildGastoDto());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			response.put(TEXT_MENSAJE, "No se encontró el gasto");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

		} catch (DataAccessException e) {
			return new ResponseEntity<>(ErrorMessageUtil.getErrorMessage(e), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<GastoDto> delete(@PathVariable Long id) {

		Optional<Gasto> gastoOptional = gastoService.delete(id);

		if (gastoOptional.isPresent()) {
			return ResponseEntity.ok(DtoMapperGasto.builder().setGasto(gastoOptional.get()).buildGastoDto());
		}

		return ResponseEntity.notFound().build();
	}
}
