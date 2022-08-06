package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

/*
 * origins = {}
 * Se indica la lista de dominios/IPs permitidos.
 */
@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping(value = "/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/clientes")
	public List<Cliente> index() {

		return clienteService.findAll();
	}
	
	/*
	 * Listado paginado
	 * 
	 * PageRequest:
	 * Tipo de dato concreto de la intefaz "Pageable". 
	 * Esta es la clase de implementación para la interfaz "Pageable" que es lo esperado a recibir.
	 * 
	 * of(page, 4):
	 * Método que contiene el número de páginas y el tamaño.
	 * Primer argumento es el número de páginas(comienza en cero) y el segundo es la cantidad de registros a mostrar
	 * 
	 */
	@GetMapping(value = "/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		
		//Instanciamos un objeto a enviar por argumento, del tipo de interfaz "Pageable" con su tipo de dato concreto "PageRequest".
		Pageable pageable = PageRequest.of(page, 4);
		
		return clienteService.findAll(pageable);
		//return clienteService.findAll(PageRequest.of(page, 4));
	}

	/*
	 * ResponseEntity: Clase/componente de Spring que permite manejar mensajes de
	 * error. Esta clase permite enviar nuestra clase Entity al "response body". Si
	 * ocurre un error, se retornará un tipo "String" y no del tipo que se envíe a
	 * esta clase. El tipo de dato que se le envié debe ser genérico.
	 * 
	 * ? : Este signo representa un tipo de dato genérico, que puede ser un objeto
	 * de cualquier tipo de dato.
	 */
	@GetMapping(value = "/clientes/{id}")
	// @ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> show(@PathVariable Long id) {

		Cliente cliente = null;

		/*
		 * Para validar la existencia del objeto encontrado necesitaremos un tipo "Map"
		 * de Java. Map<K, V> : Necesario para almacenar objetos o valores asociados a
		 * un nombre para asignar y enviar los mensajes de error.
		 */
		Map<String, Object> response = new HashMap<>();

		/*
		 * DataAccessException: Excepción propia de Spring para manejar errores de SQL a
		 * nivel de servidor como: conexión, sintaxias, BD. Por si ocurre un error del
		 * lado del servidor, como con la base de datos.
		 */
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException ex) {

			// Enviamos dos mensajes, uno personalizado y el otro obtenido con la
			// información de la excepción.
			response.put("mensaje", "Error al realizar la consulta a la base de datos.");
			response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		if (cliente == null) {
			// Añadimos un item al "response" del tipo "HashMap" con el mensaje
			// personalizado.
			response.put("mensaje",
					"El cliente con el ID: ".concat(id.toString().concat(" no existe en la base de datos.")));

			/*
			 * <Map<String, Object>>: El tipo de dato a enviar será el mismo de la
			 * construccion de la respuesta. response : Cuerpo de la respuesta que contiene
			 * el mensaje de que no se encontró un resultado. HttpStatus.NOT_FOUND : Código
			 * de la respuesta http.
			 */
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		/*
		 * new ResponseEntity<Cliente>(cliente, HttpStatus.OK) : 1° argumento, se envía
		 * un objeto, este sería el contenido a guardar en el "response body". 2°
		 * argumento, se envía el código de la respuesta http. <Cliente> : Tipo de dato
		 * necesario a devolver, en este caso "Cliente" para cuando la respuesta sea
		 * exitosa.
		 */
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);

	}

	/*
	 * code = HttpStatus.CREATED: Indicamos que el código de respuesta será 201 de
	 * creado. Si no se asigna un código, por defecto enviará el 200 .
	 * 
	 * @Valid : Necesario para que se cumplan las validaciones en el "entity" de la
	 * clase"Ciente" (@NotEmpty) Valida que antes que entre a la lógica de la
	 * función, cumpla la validación dentro de la clase "Cliente". Es un interceptor
	 * de Spring.
	 * 
	 * BindingResult : Inyectamos al método "create()" el objeto que contiene los
	 * mensajes de error en la validación del "entity" "Cliente". Importante la
	 * declaración del "BindingResult" luego del objeto a validar en la firma de
	 * la función.
	 * 
	 */
	@PostMapping(value = "/clientes")
	// @ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

		// Validación a recomendación del profesor, seteando el ID con nulo o cero para
		// que no haga un "update" en vez que "insert"
		if (cliente.getId() != null && cliente.getId() > 0) {
			cliente.setId(0L);
			// cliente.setId(null);
		}

		Cliente cliente_nuevo = null;
		Map<String, Object> response = new HashMap<>();

		// Validación de errores del "entity Cliente".
		if (result.hasErrors()) {

			/*
			 * result.getFieldErrors() : Obtenemos la lista de errores capturados de la
			 * validación y lo iteramos en un bucle. err.getField() : Obtiene el nombre del
			 * campo donde falló la validación. getDefaultMessage() : Función que contiene
			 * el mensaje de error, de tipo String.
			 */

			// 1° Opción - Bucle convencional
			/*
			List<String> errors = new ArrayList<>();

			for (FieldError err : result.getFieldErrors()) {
				errors.add("El campo '" + err.getField() + "' :  " + err.getDefaultMessage());
			}
			*/

			// 2° Opción - Stream
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			// Esto de usar el método set para asignar la fecha sería una alternativa, en
			// caso no tengamos el @PrePersist en la clase entity
			// cliente.setCreateAt(new Date());
			cliente_nuevo = clienteService.save(cliente);

		} catch (DataAccessException ex) {
			response.put("mensaje", "Error al registrar a la base de datos.");
			response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// En este caso retornamos un "Map" en la función que contendrá tanto el cliente
		// creado como un mensaje de éxito.
		response.put("mensaje", "Cliente registrado correctamente.");
		response.put("cliente", cliente_nuevo);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@PutMapping(value = "/clientes/{id}")
	// @ResponseStatus(code = HttpStatus.CREATED)
	// Importante la delclaración del "BindingResult" luego del objeto a validar
	// en la firma de la función.
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

		Cliente clienteActual = clienteService.findById(id);

		Cliente clienteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		// Validación de errores del "entity Cliente".
		if (result.hasErrors()) {

			// 2° Opción - Stream
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "Error, no se pudo editar, el cliente con el ID: "
					.concat(id.toString().concat(" no existe en la base de datos.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(new Date());

			clienteUpdated = clienteService.save(clienteActual);

		} catch (DataAccessException ex) {

			response.put("mensaje", "Error al actualizar en la base de datos.");
			response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Cliente actualizado correctamente.");
		response.put("cliente", clienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/clientes/{id}")
	// @ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			// Spring Data por detrás valida que el cliente exista, así que no será
			// necesario verificar la existencia de este ID.
			clienteService.delete(id);

		} catch (DataAccessException ex) {

			response.put("mensaje", "Error al eliminar en la base de datos.");
			response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Cliente eliminado correctamente.");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
