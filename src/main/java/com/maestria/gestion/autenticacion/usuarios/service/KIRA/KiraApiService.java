package com.maestria.gestion.autenticacion.usuarios.service.KIRA;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;

public interface KiraApiService {
    
    KiraResponseDTO obtenerUsuarioPorCorreo(String correoUniversitario);
}
