package com.maestria.gestion.autenticacion.usuarios.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maestria.gestion.autenticacion.usuarios.common.util.KiraUtil;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.JwtTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;  // Esta importación es la correcta para jjwt
import java.util.Date;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${app.keySecret}")
	private String keySecret;

    @SuppressWarnings("deprecation")
    @Override
    public String generateToken(String userId, KiraResponseDTO kiraUserInfo, String tokenOriginal, String username) {
        return Jwts.builder()
            .setSubject(userId)
            .claim("nombres", KiraUtil.obtenerNombres(kiraUserInfo))  
            .claim("apellidos", KiraUtil.obtenerApellidos(kiraUserInfo)) 
            .claim("correo", kiraUserInfo.getCorreo())  
            .claim("telefono", kiraUserInfo.getCelular()) 
            .claim("codigoAcademico", KiraUtil.obtenerCodigoPrograma(kiraUserInfo)) 
            .claim("tipoIdentificacion", kiraUserInfo.getTipoIdentificacion())  
            .claim("numeroIdentificacion", kiraUserInfo.getIdentificacion())  
            .claim("rol", KiraUtil.obtenerRolPrograma(kiraUserInfo))
            .claim(tokenOriginal, tokenOriginal)
            .claim(username, username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(SignatureAlgorithm.HS256, keySecret)
            .compact();
    }
}
