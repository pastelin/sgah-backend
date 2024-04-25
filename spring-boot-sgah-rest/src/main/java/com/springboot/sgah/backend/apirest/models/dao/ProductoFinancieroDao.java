package com.springboot.sgah.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.ProductoFinanciero;

public interface ProductoFinancieroDao extends CrudRepository<ProductoFinanciero, Integer> {

} 
