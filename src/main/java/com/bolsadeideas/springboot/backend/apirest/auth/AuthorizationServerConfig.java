package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/*
 * Configuración del "Authorization Server", encagardo de todo el proceso de autenticación por el lado de OAuth2,
 * como login, crear el token, validar token, etc.
 *  
 * @EnableAuthorizationServer : 
 * Permite habilitar esta configuración para el uso del "Authorization Server"
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	// Hacemos la implementación para que funcione con JWT
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// Inyectamos el "authentication manager" con todo lo configurado previamente.
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	// Implementaremos 3 métodos de configuración, del padre "AuthorizationServerConfigurerAdapter"

	// 1) Configuración de permisos hacia nuestros Endpoints pero SOLO de las rutas hacia "Spring Security OAuth2", las cuales deben ser públicas para que cualquier usuario inicie sesión.
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		/*
		 * Crearemos 2 rutas/enpoints, que por defecto estarán protegidas por la autenticación vía "http basic", 
		 * al usar las credenciales del cliente/aplicación que son el "cliente id" y el "secret", enviados en las cabeceras de las peticiones.
		 * 
		 * tokenKeyAccess() : 
		 * Permite dar permisos, generando el token una vez autenticado.
		 *  
		 * "permitAll()" : 
		 * Método, propio de Spring Security, que da permiso a cualquier usuario para poder atutenticarse.
		 * Por defecto en el enpoint "/oauth/token"
		 * 
		 * checkTokenAccess() :
		 * Valida el token que se recibe del cliente. 
		 * Es un permiso al endpoint que se encarga de validar el token.
		 * Por defecto en el enpoint "/oauth/check_token"
		 * 
		 * isAuthenticated() : 
		 * Método propio de Spring security, para indicar que Solo pueden acceder a esta ruta los clientes autenticados.
		 * 
		 */		
		security
		.tokenKeyAccess("permitAll()") // http://localhost:8080/oauth/token
		.checkTokenAccess("isAuthenticated()"); //
		//super.configure(security);
	}

	/*
	 * 2) Configuraremos nuestros clientes o aplicaciones que accederán a nuestra API Rest. 
	 * En este caso tendremos 1 solo cliente que es la aplicación en angular.
	 * En caso tengamos más aplicaciones que consuman nuesta API Rest, tenemos que registrarlos uno por uno, con el "cliente id" y "contraseña"/"código secreto".
	 * Así que registraremos a nuestra aplicación angular con sus credenciales.
	 * 
	 * Oauth2, proporciona mayor seguridad no solo autenticando con los usuarios del backend, 
	 * sino también con las credenciales de la aplicación frontend que se va a conectar, es decir, una doble autenticación. 
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Creamos un nuevo cliente. Todo lo sgte. se hace por cada cliente/aplicación.
		clients
		.inMemory() // Seleccionamos de momento el almacenamiento en memoria
		.withClient("angularapp") // Creamos un cliente, el argumento será considerado un "cliente id", equivalente a decir "username" de una común credencial de un usuario.
		.secret(this.passwordEncoder.encode("12345")) // Asignamos una contraseña, pero encriptada, usando de nuestra dependencia agregada.
		.scopes("read", "write")  // Alcance/scope, es decir, el permiso que tendrá el cliente/aplicación para poder leer, crear, actualizar y eliminar registros, todo esto presentado por "read" y "write"
		.authorizedGrantTypes("password", "refresh_token") // Método que asigna el tipo de consesión que tendrá nuestra autenticación, es decir, cómo vamos a obtener el token, en este caso por medio de una "password" que es cuando los usuarios existen en el backend. "refresh_token" permite obtener un token de acceso renovado, sin tener que iniciar sesión nuevamente através de esta palabra
		.accessTokenValiditySeconds(3600)  // Configuración de la validez, en cuánto tiempo va a caducar el token en segundos. "3600" es "1" hora.
		.refreshTokenValiditySeconds(3600); // Configuración del tiempo de validez del "refresh_token"
 
		//super.configure(clients);
	}

	// 3) Método de configuración que se encarga del proceso de autenticación(valida de usuario y contraseña, genera y entrega el token ).
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	
		// Traemos la información adicional para token por medio de la dependencia inyectada.
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.infoAdicionalToken, this.accessTokenConverter()));
		
		/*
		 * authenticationManager() : 
		 * Registra el "authentication manager.
		 * 
		 * tokenStore() : 
		 * Método opcional, ya que por defecto se invoca en la instancia de la clase "AuthorizationServerEndpointsConfigurer".
		 * 
		 * accessTokenConverter() : 
		 * Registramos "accessTokenConverter" que es un componente que debemos implementar, 
		 * encargado de manejar varias cosas relacionadas al token, 
		 * por ejemplo :
		 * 1) Almacena los datos. 
		 * 2) Decodifica el token, verificando que sea válido.
		 * 
		 * tokenEnhancer()
		 * Agrega información adicional al token.
		 * 
		 */
		endpoints.authenticationManager(this.authenticationManager)
		.tokenStore(this.tokenStore())
		.accessTokenConverter(this.accessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
		
		//super.configure(endpoints);
	}

	// Método que crea el token, por defecto ya se encuentra su la implementación de la clase 
	// "AuthorizationServerEndpointsConfigurer" al ser instanciado, en este caso por el contenedor de spring al ser un "bean".
	// El método en su implementación original verifica que haya/tenga una instancia/implementación de "JwtAccessTokenConverter", 
	// de no haberla, crea el token de tipo "JwtTokenStore".
	@Bean
	public JwtTokenStore tokenStore() {		
		return new JwtTokenStore(this.accessTokenConverter());
	}

	// Método personalizado que enviaremos como argumento dentro del otro método "accessTokenConverter".
	// JwtAccessTokenConverter : Método que contiene la implementación de JWT, propia de "Spring Security".
	// Para poder traducir toda la información de la autenticación con OAuth2, para codificar y decodificar un token.
	// Un token que usualmente contiene los datos del usuario y roles.
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		
		/*
		 * RSA : 
		 * Sistema criptográfico de clave pública y privada para cifrar y firmar de forma digital.
		 * Con la privada se firma el token y con la pública se valida que el token sea auténtico.
		 * 
		 * setSigningKey() :
		 * Método del propio "JwtAccessTokenConverter" para asignar la clave secreta del token.
		 * Con esta llave secreta se generará nuestro token
		 * También esta llave secreta será usada para la desencriptación del token.
		 * Será un RSA privada.
		 * 
		 * setVerifierKey()
		 * Verifica la clave secreta, el cual será un RSA pública
		 */
		jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVATE);
		jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLIC);
		
		return jwtAccessTokenConverter;
	}
	
	
	
	

}
