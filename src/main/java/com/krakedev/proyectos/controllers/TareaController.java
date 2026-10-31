package com.krakedev.proyectos.controllers;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;
 
@RestController
@RequestMapping("/api/tareas")
public class TareaController {
 
    private final TareaService service;
 
    public TareaController(TareaService service) {
        this.service = service;
    }
 
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
        try {
            Tarea guardada = service.guardar(tarea);
 
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(guardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar tarea: " + e.getMessage());
        }
    }
 
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Tarea> tareas = service.listar();
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al listar tareas: " + e.getMessage());
        }
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        try {
            Tarea tarea = service.buscarPorId(id);
 
            if (tarea == null) {
                return ResponseEntity.notFound().build();
            }
 
            return ResponseEntity.ok(tarea);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al buscar tarea: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Tarea datos
    ){
        try {
            Tarea tarea = service.actualizar(id, datos);
            if(tarea == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(tarea);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar proyecto: " + e.getMessage());
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
                    .body("Error al eliminar proyecto: " + e.getMessage());
        }
    }
}