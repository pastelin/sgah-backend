package com.springboot.sgah.backend.apirest.models.dto.mapper;

import java.util.List;

import com.springboot.sgah.backend.apirest.models.dto.GastoDto;
import com.springboot.sgah.backend.apirest.models.dto.GastoRecurrenteDto;
import com.springboot.sgah.backend.apirest.models.dto.TipoMovimientoDto;
import com.springboot.sgah.backend.apirest.models.entities.Gasto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;

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

	private GastoRecurrenteDto buildGastoRecurrenteDto(List<GastoRecurrente> gastosRecurrentes, Integer id) {
		GastoRecurrenteDto gastoRecurrenteDto = null;

		for (GastoRecurrente gastoRecurrente : gastosRecurrentes) {
			if (gastoRecurrente.getCdGasto().equals(id)) {
				gastoRecurrenteDto = DtoMapperGastoRecurrente.builder().setGastoRecurrente(gastoRecurrente)
						.buildGastoRecurrenteDto();
				break;
			}
		}

		return gastoRecurrenteDto;
	}

	public GastoDto buildGastoDto(List<GastoRecurrente> gastosRecurrentes) {
		if (gasto == null) {
			throw new NullPointerException("Debe pasar el entity Gasto!");
		}

		GastoRecurrenteDto gastoRecurrenteDto = buildGastoRecurrenteDto(gastosRecurrentes,
				gasto.getCdGastoRecurrente());

		TipoMovimientoDto tipoMovimientoDto = new TipoMovimientoDto();
		tipoMovimientoDto.setCdTipo(gasto.getCdTipoMovimiento());
		tipoMovimientoDto.setNbTipo((gasto.getCdTipoMovimiento() == 1) ? "Ingreso" : "Gasto");

		return new GastoDto(gasto.getFechaCreacion(), gasto.getMonto(), gasto.getDescripcion(),
				gastoRecurrenteDto, tipoMovimientoDto);
	}

	public Gasto buiGasto() {
		if (gastoDto == null) {
			throw new NullPointerException("Debe pasar el dto Gasto!");
		}

		gasto = new Gasto();
		gasto.setMonto(gastoDto.getMonto());
		gasto.setDescripcion(gastoDto.getDescripcion());
		gasto.setCdGastoRecurrente(gastoDto.getGastoRecurrente().getCdGasto());
		gasto.setCdTipoMovimiento(gastoDto.getTipoMovimiento().getCdTipo());

		return gasto;
	}

}
