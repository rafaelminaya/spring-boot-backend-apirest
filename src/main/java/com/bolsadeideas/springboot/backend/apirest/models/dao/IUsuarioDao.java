package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	// QUERY METHODS (Atributo email no existe, por eso lo comentamos, es solo ejemplo)
	/*
	 * find : Select
	 * By : Where
	 * Username : username
	 */
	public Usuario findByUsername(String username);
	
	/*
	 * find : Select
	 * By : Where
	 * Username : username
	 * And : and
	 * Email : email
	 */

	//public Usuario findByUsernameAndEmail(String username, String email);

	// JPQL (Atributo email no existe, por eso lo comentamos, es solo ejemplo)
	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername2(String username);
	
	//@Query("select u from Usuario u where u.username=?1 and u.email=?2")
	//public Usuario findByUsernameAndEmail2(String username, String email);

}
