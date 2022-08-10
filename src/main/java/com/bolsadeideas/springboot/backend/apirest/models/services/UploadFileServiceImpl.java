package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	// Logger : Importado de "org.slf4j.Logger"
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	// Nombre del directorio donde se guardarán las imagénes
	private final static String DIRECTORIO_UPLOAD = "uploads";

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		// Obtenemos la ruta del nombre del archivo del parámetro
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());

		// Creamos un recurso que contendrá a la imagen
		Resource recurso = null;

		/*
		 * Instanciamos un objeto de tipo "UrlResource" por medio de su constructor,
		 * enviando la ruta del archivo parseado a "uri" ya que es el tipo de dato
		 * aceptado por la firma del constructor. Este constructor requiere de un
		 * try/catch el cual generaremos. Este lanzará una excepción
		 * "MalformedURLException" el cual se lanza cuando está mal formada la ruta URL
		 * del archivo.
		 */
		recurso = new UrlResource(rutaArchivo.toUri());
		
		// validamos que el recurso exista(haya sido registado correctamente)
		if (!recurso.exists() && !recurso.isReadable()) {

			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();

			recurso = new UrlResource(rutaArchivo.toUri());

			log.error("Error no se pudo cargar la imagen: " + nombreFoto);			
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
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
		Path rutaArchivo = getPath(nombreArchivo);
		log.info(rutaArchivo.toString());
		
		/*
		 * copy() : Función que permite copiar y pegar un archivo a la ruta escogida. Este método necesitará lanzar un try/catch, así que lo generamos.
		 * "rutaArchivo" Es la ruta donde donde se copiará el archivo a subir.
		 * 
		 */
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		if(nombreFoto != null && nombreFoto.length() > 0) {
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			
			//Convertirmos el "rutaFotoAnterior" en un archivo
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			
			//Eliminamos el archivo validándo su existencia (en caso exista y pueda ser leído)
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		// Obtenemos la ruta del nombre del archivo del parámetro
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
