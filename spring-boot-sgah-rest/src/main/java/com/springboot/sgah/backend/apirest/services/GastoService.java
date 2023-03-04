package com.springboot.sgah.backend.apirest.services;

import java.util.List;

import com.springboot.sgah.backend.apirest.entities.Ahorro;

public interface GastoService {

	List<Ahorro> findAllAhorro();
	
}
