package com.bolsadeideas.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable{
		
	// ATRIBUTOS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * message : 
	 * -Atributo que permite enviar un mensaje personalizado en las anotaciones (@NotEmpty, @Size, @Email) de validadción.
	 * - Esto ya que los mensajes suelen ser generados en inglés, aunque por defecto lo ya lo tenemos en español.
	 */
	@NotEmpty(message = "no puede estar vacío.")	
	@Size(min = 4, max = 255)
	@Column(nullable = false)
	private String nombre;
	
	@NotEmpty(message = "no puede estar vacío.")
	@Size(min = 3, max = 255, message = "el tamaño tiene que estar entre 3 y 255")
	private String apellido;
	
	@NotEmpty(message = "no puede estar vacío.")
	@Email(message = "no es una direccón de correo bien formada") //validación de java para un formato correcto de email.
	@Column(nullable = false, unique = true)
	private String email;
	
	/* @NotNull Validación para indicar que el campo es requerido. Usado para
	 * atributos diferentes al tipo de dato "String".
	 */
	@NotNull(message = "no puede estar vacio") 
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE) //Indicamos que será equivalente a tipo DATE de SQL
	private Date createAt;
	
	/*
	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}
	*/
	
	private String foto;
	
	/*
	 * LAZY : Genera un proxy, que es un puente hacia el objeto "region" para acceder a sus datos, este proxy generará otros atributos adicionales 
	 * propios del framework. Estos otros atributos debemos quitarlos del JSON, los cuales son "hibernateLazyInitializer" y "handler"
	 */
	@NotNull(message = "no puede estar vacía")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;
	
	// De esta forma ignoraremos a la propiedad "cliente" del JSON obtenido de la clase "Factura", generado por la relación con este atributo "facturas"
	// allowSetters = true : Permite setters, 
	@JsonIgnoreProperties(value = {"cliente", "hibernateLazyInitializer", "handler"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<Factura> facturas;
	
	// CONSTRUCTOR
	public Cliente() {
		this.facturas = new ArrayList<>();
	}
	// GETTERS AND SETTERS
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
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}

	
	public List<Factura> getFacturas() {
		return facturas;
	}
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}


	private static final long serialVersionUID = 1L;
}
