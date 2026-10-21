package com.krakedev.proyectos.entidades;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String cargo;

    @ManyToMany(mappedBy = "empleados")
    @JsonIgnore
    private List<Tarea> tareas = new ArrayList<>();

   
    public Empleado() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
	public Empleado(String nombre, String cargo, List<Tarea> tareas) {
		super();
		this.nombre = nombre;
		this.cargo = cargo;
		this.tareas = tareas;
	}


	// Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
}