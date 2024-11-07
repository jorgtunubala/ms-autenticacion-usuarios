package com.maestria.gestion.autenticacion.usuarios.dto;

import java.util.List;

import lombok.Data;

@Data
public class KiraResponseDTO {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String codigoAcademico;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private List<String> rol;
}
