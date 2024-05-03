package com.springboot.sgah.backend.apirest.models.dto.mapper;

import java.util.List;

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

	private ProductoFinancieroDto buildProductoFinancieroDto(List<ProductoFinanciero> productosFinancieros,
			Integer id) {
		ProductoFinancieroDto productoFinancieroDto = null;

		for (ProductoFinanciero productoFinanciero : productosFinancieros) {
			if (productoFinanciero.getCdAppInversion().equals(id)) {
				productoFinancieroDto = DtoMapperProductoFinanciero.builder().setProductoFinanciero(productoFinanciero)
						.buildProductoFinancieroDto();
				break;
			}
		}

		return productoFinancieroDto;
	}

	public InversionDto buildInversionDto(List<ProductoFinanciero> productosFinancieros) {
		if (inversion == null) {
			throw new NullPointerException("Debe pasar el entity Inversion!");
		}

		ProductoFinancieroDto productoFinancieroDto = buildProductoFinancieroDto(productosFinancieros,
				inversion.getCdAppInversion());

		return new InversionDto(inversion.getFolio(), inversion.getMonto(), inversion.getDescripcion(),
				inversion.getFechaCreacion(), productoFinancieroDto);
	}

	public Inversion buildInversion() {
		if (inversionDto == null) {
			throw new NullPointerException("Debe pasar el dto Inversion!");
		}

		inversion = new Inversion();
		inversion.setFolio(inversionDto.getFolio());
		inversion.setFechaCreacion(inversionDto.getFechaCreacion());
		inversion.setDescripcion(inversionDto.getDescripcion());
		inversion.setMonto(inversionDto.getMonto());
		inversion.setCdAppInversion(inversionDto.getProductoFinanciero().getCdApp());

		return inversion;
	}

}
