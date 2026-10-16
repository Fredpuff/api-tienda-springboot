package com.example.api.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global que convierte las excepciones de la API
 * en respuestas JSON con el formato {@code {"error": "mensaje"}}.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    /**
     * Responde 404 cuando el recurso solicitado no existe.
     *
     * @param ex excepción lanzada por la capa de servicio
     * @return respuesta 404 con el mensaje de error
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    /**
     * Responde 400 cuando se viola una regla de negocio (por ejemplo, correo duplicado).
     *
     * @param ex excepción con el detalle de la regla incumplida
     * @return respuesta 400 con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> solicitudInvalida(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Responde 400 cuando el cuerpo de la petición no supera las validaciones.
     *
     * @param ex excepción con los campos inválidos
     * @return respuesta 400 con el error de cada campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    /**
     * Responde 400 cuando el cuerpo JSON está mal formado o falta.
     *
     * @param ex excepción de lectura del cuerpo
     * @return respuesta 400 con un mensaje genérico
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "El cuerpo de la petición no es un JSON válido"));
    }
}
