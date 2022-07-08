package com.bolsadeideas.springboot.backend.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * @SpringBootApplication
 * Anotación más importante de la aplicación, formada por 3 anotaciones importantes:
 * 
 * @SpringBootConfiguration : 
 * Configuración automática de spring y puedemos sobre escribir en el archivo "application.properties".
 * 
 * @EnableAutoConfiguration
 * Permite habilitar la auto configuración.
 * 
 * @ComponentScan
 * Permite registrar y buscar todas los componentes de spring en el contenedor, 
 * es decir, las clases ue hayan sido anotadas con @Component, @Controller, @RestController, @Repository, @Service
 * 
 */
@SpringBootApplication
public class SpringBootBackendApirestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}

}
