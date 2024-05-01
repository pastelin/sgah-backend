package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.GastoRecurrenteDto;
import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;

public class DtoMapperGastoRecurrente {

	private GastoRecurrente gastoRecurrente;

	private DtoMapperGastoRecurrente() {
	}

	public static DtoMapperGastoRecurrente builder() {
		return new DtoMapperGastoRecurrente();
	}

	public DtoMapperGastoRecurrente setGastoRecurrente(GastoRecurrente gastoRecurrente) {
		this.gastoRecurrente = gastoRecurrente;
		return this;
	}

	public GastoRecurrenteDto buildGastoRecurrenteDto() {
		if (gastoRecurrente == null) {
			throw new NullPointerException("Debe pasar el entity GastoRecurrente!");
		}

		return new GastoRecurrenteDto(gastoRecurrente.getCdGasto(), gastoRecurrente.getNbGasto(),
				gastoRecurrente.getCdEstatus());
	}

}


