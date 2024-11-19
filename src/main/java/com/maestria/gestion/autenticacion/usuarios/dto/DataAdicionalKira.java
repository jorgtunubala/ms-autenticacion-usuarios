package com.maestria.gestion.autenticacion.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAdicionalKira {
    private String rol;
    private String programaDepartamento;
    private String codigo;
    private String nombre;
    private String estado;
}
