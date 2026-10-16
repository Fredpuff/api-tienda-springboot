package com.example.api.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando un recurso solicitado no existe.
 * Se traduce en una respuesta HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje detalle del recurso no encontrado
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
