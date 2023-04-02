package com.bolsadeideas.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 *  Configuracion de spring security con respecto al authentication manager.
 *  
 *  @EnableGlobalMethodSecurity(securedEnabled = true)
 *  Permite el uso de la anotación "@Secured" en cada ruta de los controladores para especificar el rol a cada una.
 *  
 */
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	// Inyectamos un atributo de la interfaz "UserDetailsService", propia de "Spring
	// Security" el cual es implementada en nuestra clase "UsuarioService".
	@Autowired
	private UserDetailsService userDetailsService;

	// @Bean : Registra un método como un componente de Spring y poder inyectarlo en
	// cualquier clase de la aplicación
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 *  Métodos sobre escritos de la clase padre "WebSecurityConfigurerAdapter"
	 *  Es Autowired ya que tenemos que inyectar este componente de Spring, lo inyectaremos vía argumento
	 */
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
		//super.configure(auth);
	}

	// Método que representa al "authentication manager"
	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	

	/*
	 * Método copiado y pegado de la clase "ResourceServerConfig", ya que acá también vamos a agregar reglas a "HttpSecurity" 
	 * para nuestros endpoints pero por le lado de "Spring". 
	 * 
	 * En el otro lado es por el lado de "OAuth2".
	 * 
	 * Sin embargo acá, del lado de Spring, debemos deshabilitar algunas protecciones como el "csrf (Cross-site request forgery)
	 * 
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//No indicaremos rutas con permisos, ya que eso será trabajado por el "Servidor de recursos".
		http
		.authorizeRequests()		
		.anyRequest().authenticated() //Indica que cualquier otra ruta es únicamente para usuarios autenticados. Esto siempre se debe poner al final de todas las reglas.
		.and()
		.csrf().disable() // csrf protege nuestro formulario por medio de un token y lo deshabilitamos ya que nuestros formularios están en una aplicación aparte con Angular.
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Indicamos que no se usarán sesiones, es decir sin estado(STATELESS).
		
		//super.configure(http);
	}

}
