package com.springboot.sgah.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.sgah.backend.apirest.models.entities.GastoRecurrente;

public interface GastoRecurrenteDao extends CrudRepository<GastoRecurrente, Integer> {

}
