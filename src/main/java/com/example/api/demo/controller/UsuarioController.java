package com.example.api.demo.controller;

import com.example.api.demo.model.Usuario;
import com.example.api.demo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que expone el CRUD de usuarios en {@code /api/usuarios}.
 * La contraseña nunca se incluye en las respuestas.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista todos los usuarios.
     *
     * @return 200 con el arreglo de usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Consulta un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return 200 con el usuario, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario cuerpo JSON con los datos del usuario
     * @return 201 con el usuario creado, o 400 si el correo ya existe o los datos son inválidos
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id      identificador del usuario
     * @param usuario cuerpo JSON con los nuevos datos
     * @return 200 con el usuario actualizado, o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    /**
     * Elimina un usuario.
     *
     * @param id identificador del usuario
     * @return 204 sin contenido, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
