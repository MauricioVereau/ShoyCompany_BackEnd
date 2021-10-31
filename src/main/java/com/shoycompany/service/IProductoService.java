package com.shoycompany.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shoycompany.model.Producto;
import com.shoycompany.model.TipoProducto;

@Service
public interface IProductoService {
	
	public List<Producto>findAll();
	public Page<Producto>findAll(Pageable pageable);
	public Producto save(Producto producto);
	public void delete(Long id);
	public Producto findById(Long id);
	public List<TipoProducto>findAllTipos();

}
