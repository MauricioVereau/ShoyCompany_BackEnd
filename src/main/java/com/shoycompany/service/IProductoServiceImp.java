package com.shoycompany.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shoycompany.model.Producto;
import com.shoycompany.model.TipoProducto;
import com.shoycompany.repository.IProductoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class IProductoServiceImp implements IProductoService{
	
	@Autowired
	private IProductoRepository proRepo;

	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) proRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return proRepo.findAll(pageable);
	}

	@Override
	@Transactional
	public Producto save(Producto producto) {
		return proRepo.save(producto);
	}

	@Override
	@Transactional
	public void delete(Long id) {
	  
		proRepo.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return proRepo.findById(id).orElse(null);
	}

	@Override
	public List<TipoProducto> findAllTipos() {
		return proRepo.findAllTipos();
	}

}
