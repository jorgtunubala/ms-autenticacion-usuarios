package com.maestria.gestion.autenticacion.usuarios.service;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;

public interface JwtTokenService {
    
    String generateToken(String userId, KiraResponseDTO kiraUserInfo, String tokenOriginal, String username);
}
