package com.maestria.gestion.autenticacion.usuarios.service.impl;

import org.springframework.stereotype.Service;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;  // Esta importación es la correcta para jjwt
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private static final String SECRET_KEY = "claveSuperSecretaMaestriaComputacionKeySecretMC";
    // username, 

    @SuppressWarnings("deprecation")
    @Override
    public String generateToken(String userId, KiraResponseDTO kiraUserInfo, String tokenOriginal, String username) {
        return Jwts.builder()
            .setSubject(userId)
            .claim("nombres", kiraUserInfo.getNombres())  // Agregar nombres
            .claim("apellidos", kiraUserInfo.getApellidos())  // Agregar apellidos
            .claim("correo", kiraUserInfo.getCorreo())  // Agregar correo
            .claim("telefono", kiraUserInfo.getTelefono())  // Agregar telefono
            .claim("codigoAcademico", kiraUserInfo.getCodigoAcademico())  // Agregar código académico
            .claim("tipoIdentificacion", kiraUserInfo.getTipoIdentificacion())  // Agregar tipo de identificación
            .claim("numeroIdentificacion", kiraUserInfo.getNumeroIdentificacion())  // Agregar número de identificación
            .claim("rol", kiraUserInfo.getRol())  // Agregar rol
            .claim(tokenOriginal, tokenOriginal)
            .claim(username, username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token válido por 1 hora
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }
}
