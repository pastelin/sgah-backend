package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.ProductoFinancieroDto;
import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;

public class DtoMapperProductoFinanciero {

	private ProductoFinanciero productoFinanciero;

	private DtoMapperProductoFinanciero() {
	}

	public static DtoMapperProductoFinanciero builder() {
		return new DtoMapperProductoFinanciero();
	}

	public DtoMapperProductoFinanciero setProductoFinanciero(ProductoFinanciero productoFinanciero) {
		this.productoFinanciero = productoFinanciero;
		return this;
	}

	public ProductoFinancieroDto buildProductoFinancieroDto() {
		if (productoFinanciero == null) {
			throw new NullPointerException("Debe pasar el entity ProductoFinanciero!");
		}

		return new ProductoFinancieroDto(productoFinanciero.getCdAppInversion(),
				productoFinanciero.getNbAppInversion());
	}

}
