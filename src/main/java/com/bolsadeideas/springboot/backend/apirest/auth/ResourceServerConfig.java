package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
 * Configuración del "Resource Server", encargado de dar acceso de los recursos de nuestra aplicación a los clientes, 
 * siempre y cuando el token enviado por el cliente sea válido.
 * 
 * @EnableResourceServer :
 * Permite habilitar esta configuración como un "Resource Server".
 *  
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	
	/* 
	 * Implementamos un solo método del padre "ResourceServerConfigurerAdapter", 
	 * El cual permite implementar todas las reglas de seguridad de nuestros enpoints, 
	 * es decir, de nuestras rutas hacia los recursos.
	 * Ejemplo: listado de clientes, etc.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		/*
		 * Debemos empezar configurando/dando permisos a las rutas más específicas y hacia abajo con las más genéricas.
		 * 
		 * antMatchers() : 
		 * Permite indicar las rutas públicas, primer parámetro indica el método http, de no indicar se toman todos los métodos para la ruta indicada.
		 * 
		 * permitAll() :
		 * Indica que las rutas previas son permitidas para todos
		 * Pudo haber sido otro método como "authenticated()" solo para autenticados, hasRole() para un rol en específico o hasAnyRole() para varios roles.
		 * 
		 * hasAnyRole("USER", "ADMIN") : 
		 * Añade uno o más roles.
		 * Los roles pueden ser "USER" y "ADMIN", ya que el método por debajo añade el prefijo "ROL_"
		 * 
		 * hasRole("ADMIN") : 
		 * Añade solamente un rol.
		 * 
		 * anyRequest().authenticated()
		 * Indica que cualquier otra ruta no especificada previamente, es únicamente para usuarios autenticados independientemente del rol. 
		 * Esto siempre se debe poner al final de todas las reglas.
		 */
		
		// 1° Opción - Únicamente con reglas/permisos
		/*
		http
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "api/uploads/img/**").permitAll()
		.antMatchers(HttpMethod.GET, "/api/clientes/regiones").hasRole("ADMIN")
		.antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes/upload").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
		.antMatchers("/api/clientes/**").hasRole("ADMIN")
		.anyRequest().authenticated();
		*/
		
		/*
		 *  2° Opción - Con reglas/permisos y anotaciones.
		 *  Solo llevaremos las reglas/permisos de los ROLES a anotaciones.
		 *  Así que debemos mantener las rutas públicas junto a la última línea que indica que cualquier otro "request" requiere atutenticación.
		 *  Debemos habilitar el uso de la anotación "@Secured" en la clase "SpringSecurityConfig"
		 */		
		http
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "/api/uploads/img/**", "/images/**").permitAll()
		//.antMatchers("/api/clientes/{id}").permitAll()
		//.antMatchers("/api/facturas/**").permitAll()
		.anyRequest().authenticated()
		.and() // Permite volver al objeto principal de tipo "HttpSecurity"
		.cors().configurationSource(this.corsConfigurationSource()); // Indicamos el método que contiene la "configuración de cors"
		
		
		//super.configure(http);
	}

	// Configuración de CORS, necesaria en Spring Security en adición con los enpoints que ya teníamos configurado.
	// Documentación : Spring.io -> spring security -> Learn -> Reference Doc. (versión más actual) -> digitamos cors en el buscador -> vemos el bloque de código.
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		// Creamos la instancia de la "configuración de cors"
		CorsConfiguration configuration  = new CorsConfiguration();
		
		// Configuración de el(lo)s dominio(s) de las aplicaciones clientes separados por comas, en este caso solo una, la de angular.
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		// Configuración de los métodos http permitidos. Options ya que en algunos navegadores al solicitar del token es enviada por este método.
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		
		// Configuración para permitir las credenciales
		configuration.setAllowCredentials(true);
		// Configuración para indicar qué cabeceras serán permitidas
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		// Registraremos esta "configuración de cors" para todas nuestras rutas/enpoints del backend
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Registraremos esta "configuración de cors" para todas nuestras rutas/enpoints del backend
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
	/*
	 *  Creamos un filtro de CORS, pasándole la "configuración de cors" para poder registrarlo 
	 *  dentro del stack del conjunto de filtros que maneja Spring Framework dándole una prioridad alta.
	 *  
	 *  Configuramos/registramos un filtro en la prioridad más alta de los filtros de spring.
	 *  
	 *  También para registrar la "configuración de cors" y se aplique tanto al "Servidor de Autorización" 
	 *  cuando accedamos a los enpoints para autenticarnos y generar el token (oauth/token) y 
	 *  también cada vez que queramos validar nuestro token para acceder a los recursos.
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		// Por argumento al constructor le enviamos una instancia de "CorsFilter"
		// Por argumento le enviamos la "configuración de cors
		 FilterRegistrationBean<CorsFilter> bean = new  FilterRegistrationBean<CorsFilter>(new CorsFilter(this.corsConfigurationSource()));
		 // Damos la más alta prioridad.
		 bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		 return bean;
		 
	}

}
