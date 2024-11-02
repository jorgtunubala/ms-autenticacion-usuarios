package com.maestria.gestion.autenticacion.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.KIRA.KiraApiService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private KiraApiService kiraApiService;

    @GetMapping("/obtener-informacion")
    public KiraResponseDTO obtenerInformacionUsuario(@RequestParam String correo) {
        return kiraApiService.obtenerUsuarioPorCorreo(correo);
    }
}
