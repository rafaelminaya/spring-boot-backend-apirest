package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

/*
 * JpaRepository : 
 * Esta interfaz hereda de "PagingAndSortingRepository" y este de "CrudRepository".
 * Por lo que podremos usar las misma funciones implementadas para un CRUD de "CrudRepository", 
 * además de la paginación de "PagingAndSortingRepository".
 */
public interface IClienteDao extends JpaRepository<Cliente, Long>{
	
	//1° Opción JPQL - Para listar clientes ordenados de forma descendente por id
	@Query("select c from Cliente c order by id desc")
	public List<Cliente> findOrderByIdDesc();
	
	//2° Opción Query Methods - Para listar clientes ordenados de forma descendente por id
	public List<Cliente> findAllByOrderByIdDesc();

}
