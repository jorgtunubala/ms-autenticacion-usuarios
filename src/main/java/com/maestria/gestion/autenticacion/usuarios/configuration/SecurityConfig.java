package com.maestria.gestion.autenticacion.usuarios.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para pruebas
                .authorizeRequests(requests -> requests
                        .requestMatchers("/api/auth/google").permitAll() // Permitir el acceso sin autenticación a este endpoint
                        .anyRequest().authenticated()); // Requiere autenticación para cualquier otro endpoint
        return http.build();
    }
}