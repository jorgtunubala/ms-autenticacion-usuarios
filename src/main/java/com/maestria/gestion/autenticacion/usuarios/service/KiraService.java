package com.maestria.gestion.autenticacion.usuarios.service;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;

public interface KiraService {
    
    KiraResponseDTO getUserInfo(String username);
}
