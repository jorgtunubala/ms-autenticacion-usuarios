package com.maestria.gestion.autenticacion.usuarios.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseToken;
import com.maestria.gestion.autenticacion.usuarios.dto.GoogleTokenRequest;
import com.maestria.gestion.autenticacion.usuarios.dto.JwtResponse;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.GoogleAuthService;
import com.maestria.gestion.autenticacion.usuarios.service.JwtTokenService;
import com.maestria.gestion.autenticacion.usuarios.service.KiraService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private KiraService kiraService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest googleTokenRequest) throws Exception {
        // Verificar el token de Google
        FirebaseToken firebaseToken = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

        // Obtener la información del usuario de la API KIRA
        String email = firebaseToken.getEmail();

        KiraResponseDTO kiraUserInfo = null;
        if (email.contains("maestriacomputacion")) {
            List<String> rol = Arrays.asList("ROLE_COORDINADOR");
            kiraUserInfo = obtenerInfoProvisional(email, "Ordoñez Erazo", "Hugo Armando", "8209800", 
                "62789191", "CC", "10675432", rol);
        } else if (email.contains("lmorozco")) {
            List<String> rol = Arrays.asList("ROLE_DOCENTE");
            kiraUserInfo = obtenerInfoProvisional(email, "Orozco Garcia", "Laura Maria", "8209800", 
                "62789191", "CC", "48675324", rol);
        } else if (email.contains("cgonzals")) {
            List<String> rol = Arrays.asList("ROLE_DOCENTE");
            kiraUserInfo = obtenerInfoProvisional(email, "Gonzáles S.", "Carolina", "8209800", 
                "62789191", "CC", "48675324", rol);
        } else if (email.contains("tmcristian")) {
            List<String> rol = Arrays.asList("ROLE_ESTUDIANTE");
            kiraUserInfo = obtenerInfoProvisional(email, "Tobar Mosquera", "Christian David", "8209800", 
                "1046170267546", "CC", "1061789564", rol);
        } else if (email.contains("jorgetunubala")) {
            List<String> rol = Arrays.asList("ROLE_ESTUDIANTE");
            kiraUserInfo = obtenerInfoProvisional(email, "Tunubalá Ramírez", "Jorge Alfredo", "3105913503", 
                "104613010405", "CC", "1061786321", rol);
        } else {
            List<String> rol = Arrays.asList("ROLE_ESTUDIANTE");
            kiraUserInfo = obtenerInfoProvisional(email, "Prueba", "Prueba", "8209800", 
                "62789191", "CC", "1061785435", rol);
        }

        // Generar el JWT para el usuario autenticado 
        String []emailDiv = email.split("@");
        String jwtToken = jwtTokenService.generateToken(firebaseToken.getUid(), 
                kiraUserInfo, googleTokenRequest.getToken(), emailDiv[0]);

        // Responder al front-end con el JWT
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    private KiraResponseDTO obtenerInfoProvisional(String email, String apellidos, String nombres
            , String telefono, String codigoAcademico, String tipoIdentificacion, String numeroIdentificacion
            , List<String> rol){
        KiraResponseDTO kiraUserInfo = new KiraResponseDTO(); //kiraService.getUserInfo(email);
        kiraUserInfo.setApellidos(apellidos);
        kiraUserInfo.setNombres(nombres);
        kiraUserInfo.setCorreo(email);
        kiraUserInfo.setTelefono(telefono);
        kiraUserInfo.setCodigoAcademico(codigoAcademico);
        kiraUserInfo.setTipoIdentificacion(tipoIdentificacion);
        kiraUserInfo.setNumeroIdentificacion(numeroIdentificacion);
        kiraUserInfo.setRol(rol);
        return kiraUserInfo;
    }
}
