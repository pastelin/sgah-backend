package com.springboot.sgah.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.sgah.backend.apirest.entities.Ahorro;
import com.springboot.sgah.backend.apirest.services.GastoService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class AhorroRestController {

	@Autowired
	GastoService gastoService;
	
	@GetMapping("/ahorros")
	public List<Ahorro> findAllAhorro() {
		return gastoService.findAllAhorro();
	}
	
	
}
