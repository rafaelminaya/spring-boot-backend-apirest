package com.bolsadeideas.springboot.backend.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
 * es decir, las clases que hayan sido anotadas con @Component, @Controller, @RestController, @Repository, @Service
 * 
 * CommandLineRunner 
 * Interfaz que por medio de su método a implementar "run()", permite ejecutar algo antes de arrancar la aplicación.
 */
@SpringBootApplication
public class SpringBootBackendApirestApplication implements CommandLineRunner {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}

	// Utilizaremos este método, propio de la interfaz "CommandLineRunner", para encriptar las contraseñas
	
	@Override
	public void run(String... args) throws Exception {

		// Indicamos la contaseña a encriptar	
		String password = "12345";
		
		// Generaremos 4 contrañas encriptadas para la misa contraseña "12345",
		// que luego pegaremos en el script de base de datos. 
		for(int i = 0; i< 4; i++) {
			String passwordBcrypt = this.passwordEncoder.encode(password);
			System.out.println(passwordBcrypt);
		}
		
		System.out.println("decodificando");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  
		encoder.matches("12345", "$2a$10$hiqpONv2Iw2JKiJIYw3k8.20F.yBufT6mbg6Gy8aMRKMHp1MeUaz.");
		
		BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();  
		encoder2.matches("12345", "$2a$10$DkXG/biXyAo4eXl/P9dHyeQY8NFiCER7NT7KWx9zv4oGofUkv9OSW");
		
		System.out.println("encoder: " + encoder.matches("12345", "$2a$10$hiqpONv2Iw2JKiJIYw3k8.20F.yBufT6mbg6Gy8aMRKMHp1MeUaz."));
		System.out.println("encoder2: "+ encoder2.matches("12345", "$2a$10$DkXG/biXyAo4eXl/P9dHyeQY8NFiCER7NT7KWx9zv4oGofUkv9OSW"));
		
	}

}
