package com.krakedev.proyectos.controllers;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;
 
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
 
    private final ProyectoService service;
 
    public ProyectoController(ProyectoService service) {
        this.service = service;
    }
 
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Proyecto proyecto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.guardar(proyecto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar proyecto: " + e.getMessage());
        }
    }
 
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Proyecto> proyectos = service.listar();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al listar proyectos: " + e.getMessage());
        }
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        try {
            Proyecto proyecto = service.buscarPorId(id);
 
            if (proyecto == null) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.ok(proyecto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar proyecto: " + e.getMessage());
        }
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Proyecto datos) {
        try {
            Proyecto proyecto = service.actualizar(id, datos);
 
            if (proyecto == null) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.ok(proyecto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar proyecto: " + e.getMessage());
        }
    }
 
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            if (!service.eliminar(id)) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al eliminar proyecto: " + e.getMessage());
        }
    }
}