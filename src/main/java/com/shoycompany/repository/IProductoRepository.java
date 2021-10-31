package com.shoycompany.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shoycompany.model.Producto;
import com.shoycompany.model.TipoProducto;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
	
	@Query("from TipoProducto")
	public List<TipoProducto>findAllTipos();

}
