package com.maestria.gestion.autenticacion.usuarios.service.impl;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.KiraService;

@Service
public class KiraServiceImpl implements KiraService {

    private static final String KIRA_API_URL = "https://api.kira.com/userinfo"; // URL de la API KIRA

    @Override
    public KiraResponseDTO getUserInfo(String email) {
        // Llamar a la API KIRA para obtener la información del usuario
        RestTemplate restTemplate = new RestTemplate();
        String url = KIRA_API_URL + "?email=" + email;
        ResponseEntity<KiraResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, KiraResponseDTO.class);
        return response.getBody();
    }
    
}
