package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	//Logger : Importado de "org.slf4j.Logger"
	private final Logger log = LoggerFactory.getLogger(ClienteRestController.class);

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
			
			Cliente cliente = clienteService.findById(id);
			//Verificamos si el cliente ya tenía una foto para eliminarla
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				
				//Convertirmos el "rutaFotoAnterior" en un archivo
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				//Eliminamos el archivo validándo su existencia (en caso exista y pueda ser leído)
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
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
	
	/*
	 * Método upload para subir una imagen para un cliente, el cual recibirá 2 arguementos:
	 * 1) El archivo de tipo MultipartFile por medio de parámetro del request
	 * 2) El ID del cliente 
	 * 
	 * MultipartFile : Tipo de interfaz necesaria que representa un archivo a subir.
	 */
	@PostMapping(value = "/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id ){
		
		Map<String, Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		//Validmos que exista la imagen
		if(!archivo.isEmpty()) {
			/*
			 * UUID.randomUUID().toString() : Genera una cadena de caracteres aleatorio para concatenar al nombre del archivo y hacerlo único
			 * archivo.getOriginalFilename() : Obtiene el nombre del archivo
			 * replace(" ", "") : Reemplazamos un espacio en blanco por nada, para así borrar los posibles espacio vacíos del nombre del archivo.
			 */
			String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
			
			/*
			 * Seleccionamos en nuestro equipo, una ruta externa a nuestro proyecto. 
			 * En producción será un directorio físico del servidor. Este directorio no se almacenará dentro del WAR o JAR usados para el depoy.
			 * "c://Temp//uploads" : Sería una alternetiva a "uploads".
			 * 
			 * resolve() : Permite concatenar el nombre de la carpeta con el nombre del archivo a subir, para definir la "ruta única" que necesita todo archivo.
			 * 
			 * Path : Importado de "java.nio.file.Path"
			 */
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath(); //"c://Temp//uploads"
			log.info(rutaArchivo.toString());
			
			/*
			 * copy() : Función que permite copiar y pegar un archivo a la ruta escogida. Este método necesitará lanzar un try/catch, así que lo generamos.
			 * "rutaArchivo" Es la ruta donde donde se copiará el archivo a subir.
			 * 
			 */
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {				
				response.put("mensaje", "Error al subir la imagen del cliente "+ nombreArchivo);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			//Verificamos si el cliente ya tenía una foto para eliminarla
			String nombreFotoAnterior = cliente.getFoto();
			
			if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
				Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
				
				//Convertirmos el "rutaFotoAnterior" en un archivo
				File archivoFotoAnterior = rutaFotoAnterior.toFile();
				
				//Eliminamos el archivo validándo su existencia (en caso exista y pueda ser leído)
				if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
					archivoFotoAnterior.delete();
				}
			}
			
			//En caso todo lo anterior salga bien, guardamos el nombre de la foto.
			cliente.setFoto(nombreArchivo);
			
			//Guardamos los cambios del cliente
			clienteService.save(cliente);
			
			//Enviamos una respuesta con la nueva foto incluida
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}			
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/*
	 * Método handler para mostrar la foto en el navegador
	 * Resource es importado de "org.springframework.core.io.Resource;"
	 * 
	 * Resource : Tipo de dato necesario, ya que a partir de este será almacenado en el "ResponseEntity" a retornar para mostrar en el navegador.
	 * 
	 * {nombreFoto:.+}
	 * Los signos ":.+" es una expresión regular indicando que esta variable será 
	 * el nombre de un archivo que conendrá un punto (".") alguna extensión ("+") como ".jpg", ".png", etc.
	 * 
	 * La forma de probar este enpoint es copiando y pegando la ruta en el navegador para obtener la foto
	 * Ejemplo: http://localhost:8080/api/uploads/img/NOMBRE_IMAGEN
	 */
	@GetMapping(value = "/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		//Obtenemos la ruta del nombre del archivo del parámetro
		Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
		log.info(rutaArchivo.toString());
		
		//Creamos un recurso que contendrá a la imagen
		Resource recurso = null;
		
		/*
		 * Instanciamos un objeto de tipo "UrlResource" por medio de su constructor, 
		 * enviando la ruta del archivo parseado a "uri" ya que es el tipo de dato aceptado por la firma del constructor.
		 * Este constructor requiere de un try/catch el cual generaremos. 
		 * Este lanzará una excepción "MalformedURLException" el cual se lanza cuando está mal formada la ruta URL del archivo.
		 */
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		//validamos que el recurso exista(haya sido registado correctamente)
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("No se pudo cargar la imagen: "+nombreFoto);
		}
		/*
		 * Enviamos las cabeceras(http headers) a la respuesta para que el recurso sea forzado a poder ser descargado.
		 * HttpHeaders : 
		 * Importado de "org.springframework.http.HttpHeaders"
		 * 
		 * HttpHeaders.CONTENT_DISPOSITION : 
		 * Constante que equivale a escribir "Content-Disposition".
		 * 
		 * "attachment; filename=\"" : 
		 * attachment, es un tributo para forzar en la respiesta a que se descargue la imagen,
		 * de esta forma podemos incluirlo en el atributo "src" del elemento "<img>" en el navegador.
		 */
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}

}
