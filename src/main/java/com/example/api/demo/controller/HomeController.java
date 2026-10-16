package com.example.api.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de bienvenida para verificar que la API está en ejecución.
 */
@RestController
public class HomeController {

    /**
     * Responde un saludo en la raíz de la aplicación.
     *
     * @return el texto {@code "Hola!"}
     */
    @GetMapping("/")
    public String saludar() {
        return "Hola!";
    }
}
