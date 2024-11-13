package com.maestria.gestion.autenticacion.usuarios.service.impl;

import org.springframework.stereotype.Service;

import com.maestria.gestion.autenticacion.usuarios.common.util.KiraUtil;
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
            .claim("nombres", KiraUtil.obtenerNombres(kiraUserInfo))  
            .claim("apellidos", KiraUtil.obtenerApellidos(kiraUserInfo)) 
            .claim("correo", kiraUserInfo.getCorreo())  
            .claim("telefono", kiraUserInfo.getCelular()) 
            .claim("codigoAcademico", kiraUserInfo.getTipoIdentificacion()) 
            .claim("tipoIdentificacion", kiraUserInfo.getTipoIdentificacion())  
            .claim("numeroIdentificacion", kiraUserInfo.getIdentificacion())  
            .claim("rol", KiraUtil.obtenerRolPrograma(kiraUserInfo))
            .claim(tokenOriginal, tokenOriginal)
            .claim(username, username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token válido por 1 hora
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }
}
