package com.krakedev.proyectos.controllers;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST /api/auth/registrar
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            // No devolvemos la contraseña hasheada en la respuesta
            nuevo.setPassword("[PROTEGIDA]");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        Optional<Usuario> usuarioOpt = usuarioService.login(username, password);

        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Login exitoso",
                    "username", usuarioOpt.get().getUsername(),
                    "rol", usuarioOpt.get().getRol()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales incorrectas");
    }
}
