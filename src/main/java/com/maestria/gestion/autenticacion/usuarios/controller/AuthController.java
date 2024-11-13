package com.maestria.gestion.autenticacion.usuarios.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseToken;
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

    @Autowired
	AuthenticationManager authenticationManager;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest googleTokenRequest) throws Exception {
        // Verificar el token de Google
        FirebaseToken firebaseToken = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

        // Obtener la información del usuario de la API KIRA
        String email = firebaseToken.getEmail();

        KiraResponseDTO kiraUserInfo = kiraService.getUserInfo(email);

        String []emailDiv = email.split("@");
        
        // Generar el JWT para el usuario autenticado 

        String jwtTokenBearer = jwtUtil.generateJwtToken(emailDiv[0]);

        String jwtToken = jwtTokenService.generateToken(firebaseToken.getUid(), 
                kiraUserInfo, jwtTokenBearer, emailDiv[0]);

        // Responder al front-end con el JWT
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    /*private KiraResponseDTO obtenerInfoProvisional(String email, String apellidos, String nombres
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
    }*/
}
