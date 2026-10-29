package com.krakedev.proyectos.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Leer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza con "Bearer ", dejar pasar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token quitando el prefijo "Bearer "
        String token = authHeader.substring(7);

        // 4. Verificar si el token está en la lista negra (logout)
        if (blacklistService.estaInvalidado(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalidado. Por favor inicia sesión nuevamente.");
            return;
        }

        try {
            // 5. Verificar y decodificar el token
            DecodedJWT decodedJWT = jwtUtil.verificarToken(token);
            String username = decodedJWT.getSubject();
            String rol = decodedJWT.getClaim("rol").asString();

            // 6. Crear la autoridad con el prefijo ROLE_ que exige Spring Security
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

            // 7. Registrar al usuario en el contexto de seguridad de Spring
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTVerificationException e) {
            // Token inválido o expirado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido o expirado.");
            return;
        }

        // 8. Continuar con la petición
        filterChain.doFilter(request, response);
    }
}
