package com.springboot.sgah.backend.apirest.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.sgah.backend.apirest.dao.AhorroDao;
import com.springboot.sgah.backend.apirest.entities.Ahorro;
import com.springboot.sgah.backend.apirest.services.GastoService;

@Service
public class GastoServiceImpl implements GastoService {

	@Autowired
	AhorroDao ahorroDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Ahorro> findAllAhorro() {
		return (List<Ahorro>) ahorroDao.findAll();
	}

}
