package com.krakedev.proyectos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // Secreto con el que se firma el token (en producción iría en application.properties)
    private static final String SECRET = "nobodysecret";

    // Tiempo de expiración: 1 hora en milisegundos
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Genera un token JWT firmado con el username y el rol del usuario
    public String generarToken(String username, String rol) {
        return JWT.create()
                .withSubject(username)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // Verifica que el token sea válido y devuelve el token decodificado
    public DecodedJWT verificarToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
    }

    // Extrae el username del token
    public String obtenerUsername(String token) {
        return verificarToken(token).getSubject();
    }

    // Extrae el rol del token
    public String obtenerRol(String token) {
        return verificarToken(token).getClaim("rol").asString();
    }
}
