package com.maestria.gestion.autenticacion.usuarios.service.impl;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.maestria.gestion.autenticacion.usuarios.service.GoogleAuthService;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    // Configura tu Firebase credentials
    @Value("${google.firebase.credentials}")
    private String firebaseCredentials;
    
    @Override
    public FirebaseToken verifyGoogleIdToken(String idToken) throws Exception {
        try {
            System.out.println("Ruta del archivo de credenciales: " + firebaseCredentials);
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new FileInputStream(firebaseCredentials));
            System.out.println("Credenciales cargadas correctamente." + googleCredentials.getAuthenticationType());

            // Verifica si FirebaseApp ya está inicializado
FirebaseApp firebaseApp = null;
try {
    firebaseApp = FirebaseApp.getInstance();
} catch (IllegalStateException e) {
    // Si no existe, inicializa FirebaseApp
    googleCredentials = GoogleCredentials.fromStream(new FileInputStream(firebaseCredentials));
    firebaseApp = FirebaseApp.initializeApp(new FirebaseOptions.Builder()
            .setCredentials(googleCredentials)
            .build());
}

            // Verificar el ID token
            System.out.println("Verificando ID Token...");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
            return firebaseAuth.verifyIdToken(idToken);

        } catch (IOException e) {
            System.err.println("Error al cargar las credenciales de Firebase: " + e.getMessage());
            throw new Exception("Error al cargar las credenciales de Firebase", e);
        } catch (IllegalStateException e) {
            System.err.println("Error al inicializar Firebase: " + e.getMessage());
            throw new Exception("Error al inicializar FirebaseApp", e);
        } catch (Exception e) {
            System.err.println("Error al verificar el ID Token: " + e.getMessage());
            throw new Exception("Error al verificar el ID Token", e);
        }
    }
}
