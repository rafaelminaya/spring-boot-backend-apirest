package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Factura;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Producto;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Region;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	/*
	 * Recibirá un objeto "Pageable" por argumento  el cual es una instancia de "PageRequest" 
	 * que contiene el número de páginas y la cantidad de elementos por página a mostrar
	 * 
	 * Pageable: 
	 * Interfaz importada de "org.springframework.data.domain.Pageable"
	 * 
	 * Page:
	 * Intrefaz importada de "org.springframework.data.domain.Page"
	 * Es similar a un "List" pero a través de rangos.
	 * Tiene parámetros importantes para poder paginar en la vista(frontend) como: 
	 * El número de página actual, total de elementos del paginador, total de páginas, número de elmentos por páginas, si es la primera página, si es la última página.
	 */
	public Page<Cliente> findAll(Pageable page);

	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);
	
	// REGION
	
	public List<Region> findAllRegiones();
	
	// FACTURA
	
	public Factura findFacturaById(Long id);
	
	public Factura SaveFactura(Factura factura);
	
	public void deleteFacturaById(Long id);
	
	// PRODUCTO
	public List<Producto> findProductoByNombre(String termino);
	
	
}
