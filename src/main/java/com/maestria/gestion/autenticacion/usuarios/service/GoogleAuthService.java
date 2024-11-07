package com.maestria.gestion.autenticacion.usuarios.service;

import com.google.firebase.auth.FirebaseToken;

public interface GoogleAuthService {
    FirebaseToken verifyGoogleIdToken(String idToken) throws Exception;
}
