package com.maestria.gestion.autenticacion.usuarios.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseToken;
import com.maestria.gestion.autenticacion.usuarios.dto.DataAdicionalKira;
import com.maestria.gestion.autenticacion.usuarios.dto.GoogleTokenRequest;
import com.maestria.gestion.autenticacion.usuarios.dto.JwtResponse;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.security.JwtUtil;
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

    @Autowired
	JwtUtil jwtUtil;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest googleTokenRequest) throws Exception {
        // Verificar el token de Google
        FirebaseToken firebaseToken = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

        // Obtener la información del usuario de la API KIRA
        String email = firebaseToken.getEmail();

        //KiraResponseDTO kiraUserInfo = kiraService.getUserInfo(email);

        List<String> roles = Arrays.asList("ROLE_ESTUDIANTE");
        KiraResponseDTO kiraUserInfo = obtenerInfoProvisional(123L, "jorgetunubala", 1, "CC", 
                                                          "1111111111", "Perez", "Perez", 
                                                          "Pepito", "Pepito", email, 
                                                          "3001234567");

        String []emailDiv = email.split("@");
        
        // Generar el JWT para el usuario autenticado 

        String jwtTokenBearer = jwtUtil.generateJwtToken(emailDiv[0]);

        String jwtToken = jwtTokenService.generateToken(firebaseToken.getUid(), 
                kiraUserInfo, jwtTokenBearer, emailDiv[0]);

        // Responder al front-end con el JWT
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    private KiraResponseDTO obtenerInfoProvisional(Long oidTercero, String usuario, Integer oidTipoIdentificacion, 
                String tipoIdentificacion, String identificacion, 
                String primerApellido, String segundoApellido, 
                String primerNombre, String segundoNombre, 
                String correo, String celular) {
        
        List<DataAdicionalKira> lista = new ArrayList<>();

        // Agregar instancias a la lista
        lista.add(new DataAdicionalKira("Estudiante", "38", 
                    "104613010405", "Ingeniería de Sistemas", "ACTIVO"));
        
        KiraResponseDTO kiraUserInfo = new KiraResponseDTO(oidTercero, usuario, oidTipoIdentificacion, tipoIdentificacion,
            identificacion, primerApellido, segundoApellido, primerNombre,
            segundoNombre, correo, celular, lista);
        return kiraUserInfo;
    }
}
