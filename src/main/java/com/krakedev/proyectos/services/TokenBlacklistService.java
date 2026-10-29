package com.krakedev.proyectos.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    // Conjunto de tokens invalidados (en memoria, seguro para multi-hilo)
    private final Set<String> tokensInvalidados = ConcurrentHashMap.newKeySet();

    // Agrega un token a la lista negra (logout)
    public void invalidarToken(String token) {
        tokensInvalidados.add(token);
    }

    // Verifica si un token está en la lista negra
    public boolean estaInvalidado(String token) {
        return tokensInvalidados.contains(token);
    }
}
