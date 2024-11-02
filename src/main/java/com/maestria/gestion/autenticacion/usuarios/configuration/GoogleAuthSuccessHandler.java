package com.maestria.gestion.autenticacion.usuarios.configuration;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GoogleAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final String allowedDomain = "unicauca.edu.co"; 

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
        String email = oauthUser.getEmail();

        if (email.endsWith("@" + allowedDomain)) {
            response.sendRedirect("/home");
        } else {
            response.sendRedirect("/access-denied");
        }
    }
    
}
