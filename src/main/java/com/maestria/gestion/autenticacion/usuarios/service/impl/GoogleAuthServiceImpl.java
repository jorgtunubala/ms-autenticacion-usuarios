package com.maestria.gestion.autenticacion.usuarios.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.maestria.gestion.autenticacion.usuarios.exception.CustomException;
import com.maestria.gestion.autenticacion.usuarios.service.GoogleAuthService;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    // Configurar Firebase credentials
    @Value("${google.firebase.credentials}")
    private String firebaseCredentials;

    @Override
    public FirebaseToken verifyGoogleIdToken(String idToken) throws Exception {
        try {
            System.out.println("Construir credenciales dinamicamente... ");
            // Convertir la cadena JSON a InputStream
            ByteArrayInputStream credentialsStream = new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));

            // Crear credenciales desde el flujo
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(credentialsStream);

            System.out.println("Credenciales cargadas correctamente.");

            // Configurar el transporte para Firebase (NetHttpTransport usa HTTP/1.1)
            HttpTransport httpTransport = new NetHttpTransport();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(googleCredentials)
                    .setHttpTransport(httpTransport)
                    .build();

            // Verifica si FirebaseApp ya está inicializado
            FirebaseApp firebaseApp;
            try {
                firebaseApp = FirebaseApp.getInstance();
            } catch (IllegalStateException e) {
                firebaseApp = FirebaseApp.initializeApp(options);
            }

            // Verificar el ID token
            System.out.println("Verificando ID Token...");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            return firebaseAuth.verifyIdToken(idToken);

        } catch (IOException e) {
            System.err.println("Error al cargar las credenciales de Firebase: " + e.getMessage());
            throw new CustomException("Error al cargar las credenciales de Firebase", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.err.println("Error al verificar el ID Token: " + e.getMessage());
            throw new CustomException("Error al verificar el ID Token. Usuario no autenticado", HttpStatus.UNAUTHORIZED);
        }
    }

}
 