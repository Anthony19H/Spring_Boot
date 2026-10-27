package com.krakedev.proyectos.services;

import java.util.List;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

public class ProyectoService {
	
	private final ProyectoRepository repository;
	
	public ProyectoService(ProyectoRepository repository) {
		super();
		this.repository = repository;
		
	}
	
	public Proyecto guardar(Proyecto proyecto) {
		
		return repository.save(proyecto);
	}
	public List<Proyecto> listar(){
		return repository.findAll();
	}
	
	public Proyecto buscarPorId(int id) {
	    return repository.findById(id).orElse(null);
	}

	public boolean eliminar(int id) {
	    if(repository.existsById(id)) {
	        repository.deleteById(id);
	        return true;
	    }else {
	        return false;
	    }
	}
	
	public Proyecto actualizar(int id, Proyecto datos) {
		Proyecto proyecto = buscarPorId(id);
		if(proyecto != null) {
			proyecto.setNombre(datos.getNombre());
			proyecto.setDescripcion(datos.getDescripcion());
			proyecto.setFechaInicio(datos.getFechaInicio());
			return repository.save(proyecto);
		}else {
			return null;
		}
	}
}
