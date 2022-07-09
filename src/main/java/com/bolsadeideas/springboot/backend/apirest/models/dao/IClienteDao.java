package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{
	
	//1° Opción de listar clientes ordenados de forma descendente por id
	@Query("select c from Cliente c order by id desc")
	public List<Cliente> findOrderByIdDesc();
	
	//2° Opción de listar clientes ordenados de forma descendente por id
	public List<Cliente> findAllByOrderByIdDesc();

}
