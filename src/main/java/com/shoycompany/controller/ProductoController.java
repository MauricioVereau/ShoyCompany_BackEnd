package com.shoycompany.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoycompany.model.Producto;
import com.shoycompany.model.TipoProducto;
import com.shoycompany.service.IProductoService;
import com.shoycompany.service.UploadFileService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ProductoController {
	
	@Autowired
	public IProductoService proService;
	
	@Autowired
	private UploadFileService uploadService;
	
	@GetMapping("/productos")
	public List<Producto> index(){
		return proService.findAll();
	}
	
	@GetMapping("/productos/page/{page}")
	public Page<Producto> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);
		return proService.findAll(pageable);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("productos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			producto = proService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if(producto == null) {
			response.put("mensaje", "El Product ID:".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Producto>(producto,HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/productos")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Producto producto) {
		
		Producto productoNuevo = null; 
		Map<String, Object> response = new HashMap<>();
		
		try {
			productoNuevo = proService.save(producto);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la insercion en la base");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Producto ha sido creado con exito");
		response.put("producto", productoNuevo);
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> update(@RequestBody Producto producto,@PathVariable Long id) {
		Producto productoActual = proService.findById(id);
		Producto productoUpdated=null;
		Map<String, Object> response = new HashMap<>();
		
		if(productoActual == null) {
			response.put("mensaje", "Error : El Producto ID:".concat(id.toString().concat("no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			productoActual.setNombre(producto.getNombre());
			productoActual.setDescripcion(producto.getDescripcion());
			productoActual.setStock(producto.getStock());
			productoActual.setCreateAt(producto.getCreateAt());
			productoActual.setTipoProducto(producto.getTipoProducto());
		
			productoUpdated = proService.save(productoActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Producto ha sido actualizado con exito");
		response.put("producto", productoUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/productos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Producto producto = proService.findById(id);
			String nombreFotoAnterior = producto.getFoto();
			
			uploadService.eliminar(nombreFotoAnterior);
		
		proService.delete(id);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar en la base");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Producto ha sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.OK);
	
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/productos/upload")
	public ResponseEntity<?>upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id")Long id){
		Map<String, Object> response = new HashMap<>();
		
		Producto producto = proService.findById(id);
		
		if(!archivo.isEmpty()){
	           String nombreArchivo = null;
	            try {
				nombreArchivo = uploadService.copiar(archivo);
				} catch (IOException e) {
					response.put("mensaje", "Error al subir la imagen");
					response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
					return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				}
	              
	              String nombreFotoAnterior = producto.getFoto();
	              uploadService.eliminar(nombreFotoAnterior);
	              
	          
	              producto.setFoto(nombreArchivo);
	              proService.save(producto);
	              
	              response.put("cliente", producto);
	              response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}
		
		return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
	}
	
	

	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Resource recurso=null;
		
		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/productos/tipos")
	public List<TipoProducto> listarTipos(){
		return proService.findAllTipos();
	}


}
