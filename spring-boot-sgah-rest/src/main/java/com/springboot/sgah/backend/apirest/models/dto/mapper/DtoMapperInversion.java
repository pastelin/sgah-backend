package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.InversionDto;
import com.springboot.sgah.backend.apirest.models.dto.ProductoFinancieroDto;
import com.springboot.sgah.backend.apirest.models.entities.Inversion;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;

public class DtoMapperInversion {

	private Inversion inversion;
	private InversionDto inversionDto;

	private DtoMapperInversion() {
	}

	public static DtoMapperInversion builder() {
		return new DtoMapperInversion();
	}

	public DtoMapperInversion setInversion(Inversion inversion) {
		this.inversion = inversion;
		return this;
	}

	public DtoMapperInversion setInversionDto(InversionDto inversionDto) {
		this.inversionDto = inversionDto;
		return this;
	}

	public InversionDto buildInversionDto() {
		if (inversion == null) {
			throw new NullPointerException("Debe pasar el entity Inversion!");
		}

		return new InversionDto(inversion.getFolio(), inversion.getMonto(), inversion.getDescripcion(),
				inversion.getFechaCreacion(), new ProductoFinancieroDto(inversion.getAppInversion().getCdAppInversion(),
						inversion.getAppInversion().getNbAppInversion()));
	}

	public Inversion buildInversion(ProductoFinanciero productoFinanciero) {
		if (inversionDto == null) {
			throw new NullPointerException("Debe pasar el dto Inversion!");
		}

		inversion = new Inversion();
		inversion.setFolio(inversionDto.getFolio());
		inversion.setFechaCreacion(inversionDto.getFechaCreacion());
		inversion.setDescripcion(inversionDto.getDescripcion());
		inversion.setMonto(inversionDto.getMonto());
		inversion.setCdAppInversion(productoFinanciero);

		return inversion;
	}

}
