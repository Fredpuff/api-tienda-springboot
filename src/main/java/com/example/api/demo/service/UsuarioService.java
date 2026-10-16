package com.example.api.demo.service;

import com.example.api.demo.exception.RecursoNoEncontradoException;
import com.example.api.demo.model.Usuario;
import com.example.api.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio del módulo de usuarios.
 * Garantiza que el correo electrónico sea único.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios (vacía si no hay registros)
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado
     * @throws RecursoNoEncontradoException si no existe un usuario con ese id
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
    }

    /**
     * Registra un nuevo usuario validando que el correo no esté en uso.
     *
     * @param usuario datos del usuario a crear
     * @return el usuario guardado con su id asignado
     * @throws IllegalArgumentException si el correo ya está registrado
     */
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + usuario.getCorreo());
        }
        log.info("Creando usuario: {}", usuario.getCorreo());
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id    identificador del usuario a actualizar
     * @param datos nuevos valores del usuario
     * @return el usuario actualizado
     * @throws RecursoNoEncontradoException si no existe un usuario con ese id
     * @throws IllegalArgumentException     si el nuevo correo pertenece a otro usuario
     */
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = buscarPorId(id);
        if (usuarioRepository.existsByCorreoAndIdNot(datos.getCorreo(), id)) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo " + datos.getCorreo());
        }
        existente.setNombre(datos.getNombre());
        existente.setCorreo(datos.getCorreo());
        existente.setContrasena(datos.getContrasena());
        existente.setRol(datos.getRol());
        log.info("Actualizando usuario con id {}", id);
        return usuarioRepository.save(existente);
    }

    /**
     * Elimina un usuario existente.
     *
     * @param id identificador del usuario a eliminar
     * @throws RecursoNoEncontradoException si no existe un usuario con ese id
     */
    public void eliminar(Long id) {
        usuarioRepository.delete(buscarPorId(id));
        log.info("Usuario con id {} eliminado", id);
    }
}
