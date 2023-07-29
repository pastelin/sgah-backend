package com.springboot.sgah.backend.apirest.services;

import java.math.BigDecimal;
import java.util.List;

import com.springboot.sgah.backend.apirest.entities.Ahorro;

public interface AhorroService {

	List<Ahorro> findAllAhorro();
	BigDecimal calcularAhorro();
	List<Ahorro> findAhorroByCurrentMonth(int month, int year);
	Ahorro saveAhorro(Ahorro ahorro);
}
