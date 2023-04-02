package com.bolsadeideas.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
	// 1 ° Opción - JPQL
	//  %?1% : Indica que buscará el término que coincide sea al inicio o al final de la cadena
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String termino);
	
	// 2° Opción - Query methods - Que contenga la palabra (Containing)
	public List<Producto> findByNombreContainingIgnoreCase(String termino);
	
	// 3° Opción - Query methods - Que empiece con el termino (StartingWith)
	public List<Producto> findByNombreStartingWithIgnoreCase(String termino);	
	
}
