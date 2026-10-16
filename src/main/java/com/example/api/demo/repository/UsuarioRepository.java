package com.example.api.demo.repository;

import com.example.api.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad {@link Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Indica si ya existe un usuario registrado con el correo dado.
     *
     * @param correo correo electrónico a verificar
     * @return {@code true} si el correo ya está registrado
     */
    boolean existsByCorreo(String correo);

    /**
     * Indica si existe un usuario con el correo dado distinto al identificador indicado.
     * Se usa al actualizar para no chocar con el propio registro.
     *
     * @param correo correo electrónico a verificar
     * @param id     identificador del usuario que se excluye de la búsqueda
     * @return {@code true} si otro usuario ya usa ese correo
     */
    boolean existsByCorreoAndIdNot(String correo, Long id);
}
