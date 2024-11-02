package com.maestria.gestion.autenticacion.usuarios.service.KIRA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;

@Service
public class KiraApiServiceImpl implements KiraApiService {

    @Value("${kira.api-url}")
    private String kiraApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public KiraResponseDTO obtenerUsuarioPorCorreo(String correoUniversitario) {
        // Construimos la URL con el parámetro de consulta
        String url = UriComponentsBuilder.fromHttpUrl(kiraApiUrl)
            .queryParam("correo", correoUniversitario)
            .toUriString();

        // Realizamos la llamada a la API
        KiraResponseDTO response = restTemplate.getForObject(url, KiraResponseDTO.class);
        
        return response;
    }
    
}
