package com.maestria.gestion.autenticacion.usuarios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GoogleController {
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/error")
    @ResponseBody
    public String handleError() {
        return "Ha ocurrido un error. Por favor, inténtalo de nuevo más tarde.";
    }
}
