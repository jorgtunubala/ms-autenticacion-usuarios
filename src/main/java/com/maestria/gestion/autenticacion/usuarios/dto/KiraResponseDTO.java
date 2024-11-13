package com.maestria.gestion.autenticacion.usuarios.dto;

import java.util.List;

import lombok.Data;

@Data
public class KiraResponseDTO {
    private Long oidTercero;
    private String usuario;
    private Integer oidTipoIdentificacion;
    private String tipoIdentificacion;
    private String identificacion;
    private String primerApellido;
    private String segundoApellido;
    private String primerNombre;
    private String segundoNombre;
    private String correo;
    private String celular;
    private List<DataAdicionalKira> programas;
}
