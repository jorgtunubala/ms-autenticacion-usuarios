package com.maestria.gestion.autenticacion.usuarios.dto;

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
    private String rol;
}
