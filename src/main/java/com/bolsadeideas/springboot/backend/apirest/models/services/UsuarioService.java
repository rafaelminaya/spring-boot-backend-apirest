package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;

/*
 * UserDetailsService : Interfaz propia de "Spring Security"
 * IUsuarioService : Interfaz propia, que implementa el último método "findByUsername()"
 */
@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {
	
	//Log para escribir errores en la consola
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	/// Inyectamos el repositorio de la clase Usuario
	@Autowired
	private IUsuarioDao usuarioDao;

	// UserDetails : Interfaz que representa al usuario de "Spring Security".
	// Almacenaremos como usuario de "Spring security" al obtenido de la base de datos.
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {	
		
		// Obtenemos el usuario por medio del "username"
		Usuario usuario = usuarioDao.findByUsername(username);		
		
		//En caso no encuentre el usuario lanzamos una excepción propia de "Spring Security".
		if(usuario == null) {
			logger.error("Error en el login: No existe el usuario '" + username + "' en el sitema.");
			
			throw new UsernameNotFoundException("Error en el login: No existe el usuario '" + username + "' en el sistema.");
		}

		// Los roles deben ser asignados del tipo "GantedAuthorities", para poder
		// enviarlos al constructor del "User" propio de "Spring Scurity".
		// Así que poder medio del método "map()" del API Stream convertimos nuestro
		// arreglo de tipo "Role" a un arreglo de tipo "SimpleGrantedAuthority" el cual
		// es una implementación de la interfaz "GrantedAuthority" a devolver.
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info(authority.getAuthority()))
				.collect(Collectors.toList());

		// User : Clase propia de "Spring Security" que implementa la interfaz
		// "UserDetails".
		// Usaremos uno de sus constructores para devolver una instancia con nuestra
		// información personalizada del usuario.
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}

}
