package com.springboot.sgah.backend.apirest.models.dto.mapper;

import com.springboot.sgah.backend.apirest.models.dto.PrestamoDto;
import com.springboot.sgah.backend.apirest.models.entities.Estado;
import com.springboot.sgah.backend.apirest.models.entities.Prestamo;

public class DtoMapperPrestamo {

	private Prestamo prestamo;
	private PrestamoDto prestamoDto;

	private DtoMapperPrestamo() {
	}
 
	public static DtoMapperPrestamo builder() {
		return new DtoMapperPrestamo();
	}

	public DtoMapperPrestamo setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
		return this;
	}

	public DtoMapperPrestamo setPrestamoDto(PrestamoDto prestamoDto) {
		this.prestamoDto = prestamoDto;
		return this;
	}

	public PrestamoDto buildPrestamoDto() {
		if (prestamo == null) {
			throw new NullPointerException("Debe pasar el entity Prestamo!");
		}

		return new PrestamoDto(prestamo.getFolio(), prestamo.getMontoPrestado(), prestamo.getDescripcion(),
				prestamo.getFechaCreacion(), prestamo.getMontoPagado(), prestamo.getEstado().getCdEstado());
	}

	public Prestamo buildPrestamo(Estado estado) {
		if (prestamoDto == null) {
			throw new NullPointerException("Debe pasar el dto Prestamo!");
		}

		prestamo = new Prestamo();
		prestamo.setFolio(prestamoDto.getFolio());
		prestamo.setMontoPrestado(prestamoDto.getSaldoPrestado());
		prestamo.setDescripcion(prestamoDto.getDescripcion());
		prestamo.setFechaCreacion(prestamoDto.getFechaCreacion());
		prestamo.setMontoPagado(prestamoDto.getSaldoPagado());
		prestamo.setEstatus(estado);

		return prestamo;
	}

}
