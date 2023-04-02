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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	// ATRIBUTOS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	
	private String observacion;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	/*
	 * @JoinColumn(name = "cliente_id")
	 * Esto ya no es necesario, ya que fue configurado en la clase "Cliente".
	 * Fue configurado con el  mappedBy = "cliente" en el atributo private List<Factura> facturas; de la clase "Cliente"
	 *
	 * @JsonIgnoreProperties({"facturas"})
	 * De esta forma ignoraremos a la propiedad "facturas" del JSON obtenido de la clase "Cliente", generado por la relación con este atributo "cliente"
	 * 
	 * allowSetters = true : 
	 * Permite setters, 
	 */
	@JsonIgnoreProperties(value = {"facturas", "hibernateLazyInitializer", "handler"}, allowSetters = true)	
	@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;
	
	@PrePersist
	public void prePersiste() {
		this.createAt = new Date();
	}
	
	
	// CONSTRUCTOR
	public Factura() {
		this.items = new ArrayList<>();
	}
	
	// MÉTODOS
	// El método empezará con "get.." para que sea incluido en el JSON
	public Double getTotal() {
		
		//return this.items.stream().mapToDouble(item -> item.getImporte().doubleValue()).sum();
		
		Double total = 0.00;
		for(ItemFactura item: items) {
			total += item.getImporte();
		}		
		return total;
		
		
	}


	// GETTERS AND SETTERS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	
	public List<ItemFactura> getItems() {
		return items;
	}

	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}


	private static final long serialVersionUID = 1L;

}
