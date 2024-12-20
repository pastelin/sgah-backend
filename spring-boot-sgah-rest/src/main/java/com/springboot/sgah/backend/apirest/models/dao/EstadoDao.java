package com.springboot.sgah.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.Estado;

public interface EstadoDao extends CrudRepository<Estado, Integer> {

}
