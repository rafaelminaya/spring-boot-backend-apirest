package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/*
 * @Entity Permite marcar a la clase como de persistencia, 
 * es decir, una clase que está mapeada a una tabla.
 * 
 * @Table Opcionalmente para indicar el nombre de la tabla,
 * si se omite tomará por defecto el nombre de la clase.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {


	/*
	 * @Id Indica a JPA que este atributo es la llave primaria.
	 * 
	 * @GeneratedValue Indica la estrategia en cómo se generará este valor.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * @Column Da información en la mata data, como nombre de la columna y
	 * validaciones
	 */
	@Column(unique = true, length = 20)
	private String username;

	@Column(length = 60)
	private String password;

	private Boolean enabled;
	
	private String nombre;
	private String apellido;
	
	@Column(unique = true)
	private String email;

	/*
	 * FetchType.LAZY Carga perezoza
	 * 
	 * CascadeType.ALL Para que cada vez que se guarde, modifique o edite a un
	 * usuario, esto también afecte a su respectivo rol.
	 * 
	 * @ManyToMany : Relación muchos a muchos.
	 * 
	 * Por defecto se creará una tabla intermedia, para unir a "Usuario" con "Role".
	 * 
	 * Esta tabla intermedia se llamará "usuarios_roles", ya que concatena con un
	 * guion bajo "_" el nombre asignado de la tabla actual "usuarios" y el nombre
	 * del atributo local "roles" que lo relaciona con la otra tabla.
	 * 
	 * La tabla contendrá los IDs de cada clase.
	 * 
	 * Las llaves por defecto se llamarán:
	 * 
	 * "usuario_id" : Nombre de la clase "usuario" y el nombre del atributo "id" que
	 * representa la PK.
	 * 
	 * "roles_id": Nombre del atributo local "roles", que lo relaciona con la otra
	 * tabla, y el nombre del atributo "id" que representa la PK.
	 * 
	 * 
	 * @JoinTable Permite personalizar el nombre de la tabla intermedia del muchos a
	 * muchos. 
	 * name : Nombre de la tabla intermedia. 
	 * joinColumns : Indica el nombre de la primera llave primaria, en referencia de la tabla actual "usuarios".
	 * inverseJoinColumns : Indica el nombre de la segunda llave primaria, en referencia de la tabla relacionada "roles".
	 * 
	 * uniqueConstraints : Indica ciertos campos en conjunto sean únicos, en este
	 * caso "usuario_id" y "role_id", así un mismo usuario no tenga el mismo rol repetido.
	 * 
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = {
			@UniqueConstraint(columnNames = { "usuario_id", "role_id" }) })
	private List<Role> roles;
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id+", "+this.username+", "+this.enabled;
	}

	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
