package com.maestria.gestion.autenticacion.usuarios.controller;

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
        KiraResponseDTO kiraUserInfo = new KiraResponseDTO(); //kiraService.getUserInfo(email);
        kiraUserInfo.setApellidos("prueba");
        kiraUserInfo.setNombres("prueba");
        kiraUserInfo.setRol("prueba");
        kiraUserInfo.setCorreo(email);
        kiraUserInfo.setNumeroIdentificacion("10919202029");
        kiraUserInfo.setTipoIdentificacion("CC");
        kiraUserInfo.setTelefono("312837382");
        kiraUserInfo.setCodigoAcademico("1046170213456");


        // Generar el JWT para el usuario autenticado
        String jwtToken = jwtTokenService.generateToken(firebaseToken.getUid(), kiraUserInfo);

        // Responder al front-end con el JWT
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
}
