package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.AhorroDto;
import com.springboot.sgah.backend.apirest.models.entities.Ahorro;

public class DtoMapperAhorro {

	private Ahorro ahorro;

	private AhorroDto ahorroDto;

	private DtoMapperAhorro() {
	}

	public static DtoMapperAhorro builder() {
		return new DtoMapperAhorro();
	}

	public DtoMapperAhorro setAhorro(Ahorro ahorro) {
		this.ahorro = ahorro;
		return this;
	}

	public DtoMapperAhorro setAhorroDto(AhorroDto ahorroDto) {
		this.ahorroDto = ahorroDto;
		return this;
	}

	public AhorroDto buildAhorroDto() {
		if (ahorro == null) {
			throw new NullPointerException("Debe pasar el entity Ahorro!");
		}

		return new AhorroDto(ahorro.getFechaCreacion(), ahorro.getMonto(), ahorro.getDescripcion());
	}

	public Ahorro buildAhorro() {
		if (ahorroDto == null) {
			throw new NullPointerException("Debe pasar el dto Ahorro!");
		}

		ahorro = new Ahorro();
		ahorro.setDescripcion(ahorroDto.getDescripcion());
		ahorro.setMonto(ahorroDto.getMonto());

		return ahorro;
	}

}
