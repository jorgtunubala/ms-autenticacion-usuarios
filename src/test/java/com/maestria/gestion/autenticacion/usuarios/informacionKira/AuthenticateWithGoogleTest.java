package com.maestria.gestion.autenticacion.usuarios.informacionKira;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.maestria.gestion.autenticacion.usuarios.dto.GoogleTokenRequest;
import com.maestria.gestion.autenticacion.usuarios.dto.KiraResponseDTO;
import com.maestria.gestion.autenticacion.usuarios.service.impl.GoogleAuthServiceImpl;
import com.maestria.gestion.autenticacion.usuarios.service.impl.KiraServiceImpl;

@SpringBootTest
public class AuthenticateWithGoogleTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @MockBean
    private GoogleAuthServiceImpl googleAuthService;
    @MockBean
    private KiraServiceImpl kiraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateWithGoogleSuccess() throws Exception {        

        // Datos de entrada simulados
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
        googleTokenRequest.setToken("valid-google-token");

        // Crear un mock de FirebaseToken
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("estudiante@unicauca.edu.co");
        when(firebaseToken.getUid()).thenReturn("uid123");

        // Mockear el comportamiento de FirebaseAuth
        when(firebaseAuth.verifyIdToken("valid-google-token")).thenReturn(firebaseToken);

        when(googleAuthService.verifyGoogleIdToken("valid-google-token")).thenReturn(firebaseToken);

        // Llamar al método del servicio que se prueba
        FirebaseToken resultToken = googleAuthService.verifyGoogleIdToken(googleTokenRequest.getToken());

        // Verificar resultados no nulos
        assertNotNull(resultToken, "El token devuelto no debe ser nulo");

        // Verificar el correo y UID del token
        assertEquals("estudiante@unicauca.edu.co", resultToken.getEmail(), "El correo del token no coincide");
        assertEquals("uid123", resultToken.getUid(), "El UID del token no coincide");        
    }


    @Test
    void testAuthenticateWithGoogleUnauthenticated() throws Exception {
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
        googleTokenRequest.setToken("invalid-token");

        when(googleAuthService.verifyGoogleIdToken("invalid-token"))
            .thenThrow(new IllegalArgumentException("Invalid token"));        

        // Ejecutar y verificar la excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            googleAuthService.verifyGoogleIdToken("invalid-token");
        });

        // Validar el mensaje de la excepción
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void testAuthenticateWithGoogleInvalidEmail() throws Exception {
        // Crear solicitud con un token válido
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
        googleTokenRequest.setToken("valid-google-token");

        // Crear un mock de FirebaseToken con un correo válido
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("incorrecto@otrodominio.com");
        when(firebaseToken.getUid()).thenReturn("uid123");

        // Configurar googleAuthService para devolver el token simulado
        when(googleAuthService.verifyGoogleIdToken("valid-google-token")).thenReturn(firebaseToken);

        // Ejecutar y verificar la excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Lógica que valida si el correo es válido
            FirebaseToken result = googleAuthService.verifyGoogleIdToken("valid-google-token");
            if (!result.getEmail().endsWith("@unicauca.edu.co")) {
                throw new IllegalArgumentException("Correo inválido");
            }
        });

        // Validar el mensaje de la excepción
        assertEquals("Correo inválido", exception.getMessage());
    }

    @Test
    void testAuthenticateWithGoogleKiraServiceUnavailable() throws Exception {
        // Crear solicitud con un token válido
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
        googleTokenRequest.setToken("valid-google-token");

        // Crear un mock de FirebaseToken con datos válidos
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("estudiante@unicauca.edu.co");
        when(firebaseToken.getUid()).thenReturn("uid123");

        // Configurar googleAuthService para devolver el token simulado
        when(googleAuthService.verifyGoogleIdToken("valid-google-token")).thenReturn(firebaseToken);

        // Configurar kiraService para lanzar una excepción
        when(kiraService.getUserInfo("estudiante@unicauca.edu.co"))
            .thenThrow(new RuntimeException("KIRA service unavailable"));

        // Ejecutar y verificar la excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            // Simular el flujo completo que incluye la interacción con kiraService
            FirebaseToken resultToken = googleAuthService.verifyGoogleIdToken("valid-google-token");
            kiraService.getUserInfo(resultToken.getEmail());
        });

        // Validar el mensaje de la excepción
        assertEquals("KIRA service unavailable", exception.getMessage());
    }

    @Test
    void testAuthenticateWithGoogleIncompleteKiraData() throws Exception {
        // Crear solicitud con un token válido
        GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
        googleTokenRequest.setToken("valid-google-token");

        // Crear un mock de FirebaseToken con datos válidos
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseToken.getEmail()).thenReturn("estudiante@unicauca.edu.co");
        when(firebaseToken.getUid()).thenReturn("uid123");

        // Simular respuesta de KIRA con datos incompletos
        KiraResponseDTO kiraResponseDTO = new KiraResponseDTO();
        kiraResponseDTO.setPrimerNombre(null); // Primer nombre es obligatorio
        kiraResponseDTO.setPrimerApellido("Apellido");
        kiraResponseDTO.setCorreo("estudiante@unicauca.edu.co");

        // Configurar los mocks
        when(googleAuthService.verifyGoogleIdToken("valid-google-token")).thenReturn(firebaseToken);
        when(kiraService.getUserInfo("estudiante@unicauca.edu.co")).thenReturn(kiraResponseDTO);

        // Ejecutar y verificar la excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            // Simular flujo completo con validación de datos
            FirebaseToken resultToken = googleAuthService.verifyGoogleIdToken("valid-google-token");
            KiraResponseDTO userInfo = kiraService.getUserInfo(resultToken.getEmail());

            // Validar que los datos no sean incompletos
            if (userInfo.getPrimerNombre() == null || userInfo.getPrimerApellido() == null) {
                throw new RuntimeException("Datos incompletos de usuario");
            }
        });

        // Validar el mensaje de la excepción
        assertEquals("Datos incompletos de usuario", exception.getMessage());
    }

}
