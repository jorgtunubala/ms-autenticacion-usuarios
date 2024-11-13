package com.maestria.gestion.autenticacion.usuarios.common.util;

import java.util.List;

import com.maestria.gestion.autenticacion.usuarios.dto.DataAdicionalKira;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;

public class KiraUtil {

    /**
     * Obtiene el nombre completo concatenando el primer y segundo nombre, así como el primer y segundo apellido.
     * @param kiraUserInfo Objeto KiraResponseDTO que contiene la información del usuario.
     * @return El nombre completo del usuario.
     */
    public static String obtenerNombreCompleto(KiraResponseDTO kiraUserInfo) {
        return obtenerNombres(kiraUserInfo) + " " + obtenerApellidos(kiraUserInfo);
    }

    /**
     * Obtiene los nombres concatenando el primer y segundo nombre.
     * @param kiraUserInfo Objeto KiraResponseDTO que contiene la información del usuario.
     * @return Los nombres del usuario.
     */
    public static String obtenerNombres(KiraResponseDTO kiraUserInfo) {
        return kiraUserInfo.getPrimerNombre() + 
               (kiraUserInfo.getSegundoNombre() != null ? " " + kiraUserInfo.getSegundoNombre() : "");
    }

    /**
     * Obtiene los apellidos concatenando el primer y segundo apellido.
     * @param kiraUserInfo Objeto KiraResponseDTO que contiene la información del usuario.
     * @return Los apellidos del usuario.
     */
    public static String obtenerApellidos(KiraResponseDTO kiraUserInfo) {
        return kiraUserInfo.getPrimerApellido() + 
               (kiraUserInfo.getSegundoApellido() != null ? " " + kiraUserInfo.getSegundoApellido() : "");
    }

    /**
     * Obtiene el código del primer elemento en la lista de programas adicionales del usuario.
     * @param kiraUserInfo Objeto KiraResponseDTO que contiene la información del usuario.
     * @return El código del primer programa, o null si la lista está vacía.
     */
    public static String obtenerCodigoPrograma(KiraResponseDTO kiraUserInfo) {
        List<DataAdicionalKira> programas = kiraUserInfo.getProgramas();
        return (programas != null && !programas.isEmpty()) ? programas.get(0).getCodigo() : null;
    }

    /**
     * Obtiene el rol del primer elemento en la lista de programas adicionales del usuario.
     * @param kiraUserInfo Objeto KiraResponseDTO que contiene la información del usuario.
     * @return El rol del primer programa, o null si la lista está vacía.
     */
    public static String obtenerRolPrograma(KiraResponseDTO kiraUserInfo) {
        List<DataAdicionalKira> programas = kiraUserInfo.getProgramas();
        
        if (programas != null && !programas.isEmpty()) {
            String rol = programas.get(0).getRol();
            
            switch (rol) {
                case "Estudiante":
                    return "ROLE_ESTUDIANTE";
                case "Docente":
                    return "ROLE_DOCENTE";
                case "Coordinador":
                    return "ROLE_COORDINADOR";
                default:
                    return null;
            }
        }
        
        return null; // Si la lista está vacía o es nula
    }
}