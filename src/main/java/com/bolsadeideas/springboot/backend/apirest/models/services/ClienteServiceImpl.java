package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IClienteDao;
import com.bolsadeideas.springboot.backend.apirest.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.backend.apirest.models.dao.IProductoDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Factura;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Producto;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Region;

/*
 * @Service: 
 * Permite guardar en el contenedor de spring como un componente de servicio en spring 
 * Podemos inyectar este objeto(bean de spring), en este caso en el controlador.
 */
@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Override
	//Esta anotación se podría omitir puesto que "CrudRepository" ya vienen con  transaccionabilidad.
	//Pero aún así es recomendado anotarlo en el service ya que sobre escribe la que está en el "CrudRepository"
	//para así tener el control y hacerlo de una forma más explícita.
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {		
		//return (List<Cliente>) clienteDao.findAll();
		//return (List<Cliente>) clienteDao.findOrderByIdDesc();
		return (List<Cliente>) clienteDao.findAllByOrderByIdDesc();
	}
	
	//Implementación de listado paginado
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable page) {
		return clienteDao.findAll(page);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {	
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		return clienteDao.findAllRegiones();
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return this.facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Factura SaveFactura(Factura factura) {		
		return this.facturaDao.save(factura);
	}

	@Override
	@Transactional
	public void deleteFacturaById(Long id) {
		this.facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findProductoByNombre(String termino) {	
		return this.productoDao.findByNombre(termino);
	}	

}
