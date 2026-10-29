package com.krakedev.proyectos.controllers;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    // POST /api/auth/registrar
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            nuevo.setPassword("[PROTEGIDA]");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    // POST /api/auth/login — devuelve el token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        Optional<Usuario> usuarioOpt = usuarioService.login(username, password);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Genera el token con el username y el rol
            String token = jwtUtil.generarToken(usuario.getUsername(), usuario.getRol());
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Login exitoso",
                    "token", token,
                    "username", usuario.getUsername(),
                    "rol", usuario.getRol()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales incorrectas");
    }

    // POST /api/auth/logout — invalida el token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        // El header llega como "Bearer <TOKEN>", extraemos solo el token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.invalidarToken(token);
            return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Token no proporcionado o formato incorrecto");
    }

    // GET /api/auth/perfil — devuelve los datos del usuario autenticado
    @GetMapping("/perfil")
    public ResponseEntity<?> perfil() {
        // Lee la identidad directamente del contexto de seguridad de Spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String rol = authentication.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(Map.of(
                "username", username,
                "rol", rol
        ));
    }
}
