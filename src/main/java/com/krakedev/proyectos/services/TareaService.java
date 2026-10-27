package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

import jakarta.transaction.Transactional;

@Service
public class TareaService {
	private final TareaRepository tarearepository;
	private final ProyectoRepository proyectorepository;
	private final EmpleadoRepository empleadorepository;

	public TareaService(TareaRepository tarearepository, ProyectoRepository proyectorepository,
			EmpleadoRepository empleadorepository) {
		super();
		this.tarearepository = tarearepository;
		this.proyectorepository = proyectorepository;
		this.empleadorepository = empleadorepository;
	}

	@Transactional
	public Tarea guardar(Tarea tarea) {
		if (tarea.getProyecto() == null) {
			throw new IllegalArgumentException("Debe indicar el proyecto de la tarea");
		}
		int proyectoId = tarea.getProyecto().getId();
		Proyecto proyecto = proyectorepository.findById(proyectoId)
				.orElseThrow(() -> new IllegalArgumentException("No existe el proyecto con ID " + proyectoId));
		List<Empleado> empleados = new ArrayList<>();
		if (tarea.getEmpleados() != null) {
			for (Empleado empleadoRecibido : tarea.getEmpleados()) {
				Empleado empleado = empleadorepository.findById(empleadoRecibido.getId()).orElseThrow(
						() -> new IllegalArgumentException("No existe el empleado con ID " + empleadoRecibido.getId()));
				empleados.add(empleado);
			}
		}
		tarea.setProyecto(proyecto);
		tarea.setEmpleados(empleados);
		return tarearepository.save(tarea);
	}

	@Transactional
	public List<Tarea> listar() {
		return tarearepository.findAll();
	}
	
	@Transactional
	public Tarea buscarPorId(int id) {
		return tarearepository.findById(id).orElse(null);
	}
	
	@Transactional
	public boolean eliminar(int id) {
		if (tarearepository.existsById(id)) {
			tarearepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional
	public Tarea actualizar(int id, Tarea datos) {
		Tarea tarea = buscarPorId(id);

		if (tarea != null) {
			tarea.setDescripcion(datos.getDescripcion());
			tarea.setFechaLimite(datos.getFechaLimite());
			tarea.setCostoEstimado(datos.getCostoEstimado());

			return tarearepository.save(tarea);
		}
		return null;
	}
}