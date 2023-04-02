package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUsuarioService;

// TokenEnhancer : Interfaz que significa "token potenciador" para agregar más información al token.
@Component
public class InfoAdicionalToken implements TokenEnhancer {
	
	@Autowired
	private IUsuarioService usuarioService;

	// Método implementado por la interfaz
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		// Implementamos un objeto de tipo "Map" para asignarle al parámetro
		// "accessToken"

		Map<String, Object> info = new HashMap<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		//authentication : Parámetro que obtiene el usuario ya autenticado, y con "getName()" el nombre del usuario.
		info.put("info_adicional", "Hola que tal!: " + authentication.getName());
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		info.put("email", usuario.getEmail());
		
		/*
		 * DefaultOAuth2AccessToken : 
		 * Método que implementa la interfaz "OAuth2AccessToken"
		 * Convertiremos la interfaz a su tipo concreto para poder acceder al método "setAdditionalInformation()"
		 * 
		 * setAdditionalInformation() : 
		 * Método para poder agregar información adicional al token.
		 */
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}
