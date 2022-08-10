package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;



public interface IUploadFileService {
	/*
	 * Método para cargar/mostrar la imagen.
	 * Importado de "org.springframework.core.io"
	 */
	public Resource cargar(String nombreFoto) throws MalformedURLException;
	
	//Método para copiar la imagen
	public String copiar(MultipartFile archivo) throws IOException;
	
	//Método que verifica que se haya eliminado la imagen
	public boolean eliminar(String nombreFoto);
	
	//Método para  obtener la ruta donde se guardará la foto
	public Path getPath(String nombreFoto);

}
