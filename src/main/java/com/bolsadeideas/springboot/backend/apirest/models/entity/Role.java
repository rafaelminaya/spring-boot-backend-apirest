package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * @Entity Permite marcar a la clase como de persistencia, 
 * es decir, una clase que está mapeada a una tabla.
 * 
 * @Table Opcionalmente para indicar el nombre de la tabla,
 * si se omite tomará por defecto el nombre de la clase
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// En Spring security todo rol tiene un prefijo de "ROL_"
	@Column(unique = true, length = 20)
	private String nombre;
	
	/*
	 * OPCIONAL: Implementaremos lo sgte. solo en caso necesitemos listar a los usuarios dentro de los roles
	 * Para que sea una relación bidireccional, el dueño de la relación será el "Usuario".
	 * Así que el "Role" tendría la "relación inversa" asignado por la propiedad "mappedBy"
	 * mappedBy = "roles" : Será el nombre del atributo en la clase "Usuario", 
	 * donde también tiene la anotación "@ManyToMany".
	 */
	/*
	@ManyToMany(mappedBy = "roles")
	private List<Usuario> usuarios;
	*/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
