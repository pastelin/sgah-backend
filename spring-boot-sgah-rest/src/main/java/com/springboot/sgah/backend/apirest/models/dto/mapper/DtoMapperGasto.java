package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.GastoDto;
import com.springboot.sgah.backend.apirest.models.dto.GastoRecurrenteDto;
import com.springboot.sgah.backend.apirest.models.dto.OrigenMovimientoDto;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;
import com.springboot.sgah.backend.apirest.models.entities.OrigenMovimiento;

public class DtoMapperGasto {

	private Gasto gasto;
	private GastoDto gastoDto;

	private DtoMapperGasto() {
	}

	public static DtoMapperGasto builder() {
		return new DtoMapperGasto();
	}

	public DtoMapperGasto setGasto(Gasto gasto) {
		this.gasto = gasto;
		return this;
	}

	public DtoMapperGasto setGastoDto(GastoDto gastoDto) {
		this.gastoDto = gastoDto;
		return this;
	}

	public GastoDto buildGastoDto() {
		if (gasto == null) {
			throw new NullPointerException("Debe pasar el entity Gasto!");
		}

		OrigenMovimientoDto origenMovimiento = null;
		GastoRecurrenteDto gastoRecurrenteDto = null;

		if (gasto.getOrigenMovimiento() != null) {
			origenMovimiento = new OrigenMovimientoDto(gasto.getOrigenMovimiento().getId(),
					gasto.getOrigenMovimiento().getDescripcion());

		}

		if (gasto.getGastoRecurrente() != null) {
			gastoRecurrenteDto = new GastoRecurrenteDto(gasto.getGastoRecurrente().getCdGasto(),
					gasto.getGastoRecurrente().getNbGasto());

		}

		return new GastoDto(gasto.getId(), gasto.getFechaCreacion(), gasto.getMonto(), gasto.getDescripcion(),
				gastoRecurrenteDto, origenMovimiento);
	}

	public Gasto buiGasto(GastoRecurrente gastoRecurrente, OrigenMovimiento origenMovimiento) {
		if (gastoDto == null) {
			throw new NullPointerException("Debe pasar el dto Gasto!");
		}

		gasto = new Gasto();
		gasto.setId(gastoDto.getId());
		gasto.setMonto(gastoDto.getAmount());
		gasto.setDescripcion(gastoDto.getDescripcion());
		gasto.setGastoRecurrente(gastoRecurrente);
		gasto.setOrigenMovimiento(origenMovimiento);
		gasto.setFechaCreacion(gastoDto.getCreationDate());

		return gasto;
	}

}
