package com.krakedev.proyectos.controllers;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;
 
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
 
    private final EmpleadoService service;
 
    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }
 
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Empleado empleado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(empleado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar empleado: " + e.getMessage());
        }
    }
 
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Empleado> empleados = service.listar();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al listar empleados: " + e.getMessage());
        }
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        try {
            Empleado empleado = service.buscarPorId(id);
 
            if (empleado == null) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.ok(empleado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar empleado: " + e.getMessage());
        }
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Empleado datos) {
        try {
            Empleado empleado = service.actualizar(id, datos);
 
            if (empleado == null) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.ok(empleado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar empleado: " + e.getMessage());
        }
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            if (!service.eliminar(id)) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar empleado: " + e.getMessage());
        }
    }
}