package com.example.api.demo.repository;

import com.example.api.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad {@link Producto}.
 * Hereda de {@link JpaRepository} las operaciones CRUD básicas.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
