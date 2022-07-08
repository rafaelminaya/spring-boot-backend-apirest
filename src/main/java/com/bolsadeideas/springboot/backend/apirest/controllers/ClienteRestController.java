package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

/*
 * origins = {}
 * Se indica la lista de dominios/IPs permitidos.
 */
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(value = "/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value = "/clientes")
	public List<Cliente> index(){
		
		return clienteService.findAll();
	}
	
	@GetMapping(value = "/clientes/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Cliente show(@PathVariable Long id) {
		return clienteService.findById(id);
		
	}
	
	/*
	 * code = HttpStatus.CREATED:
	 * Indicamos que el código de respuesta será 201 de creado.
	 * Si no se asigna un código, por defecto enviará el 200 .
	 */
	@PostMapping(value = "/clientes")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cliente create(@RequestBody Cliente cliente) {
		//Esto de usar el método set para asignar la fecha sería una alternativa, en caso no tengamos el @PrePersist en la clase entity
		//cliente.setCreateAt(new Date());
		
		return clienteService.save(cliente);
		
	}
	
	@PutMapping(value = "/clientes/{id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
		
		Cliente clienteActual = clienteService.findById(id);
		
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setEmail(cliente.getEmail());
		
		//save() También actualiza si se le envía un id con valor.		
		return clienteService.save(clienteActual);
	}
	
	@DeleteMapping(value = "/clientes/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}

}
