package com.example.api.demo.service;

import com.example.api.demo.exception.RecursoNoEncontradoException;
import com.example.api.demo.model.Producto;
import com.example.api.demo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica de negocio del módulo de productos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Obtiene todos los productos registrados.
     *
     * @return lista de productos (vacía si no hay registros)
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     * @throws RecursoNoEncontradoException si no existe un producto con ese id
     */
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id " + id));
    }

    /**
     * Registra un nuevo producto. Cualquier id enviado se ignora.
     *
     * @param producto datos del producto a crear
     * @return el producto guardado con su id asignado
     */
    public Producto crear(Producto producto) {
        log.info("Creando producto: {}", producto.getNombre());
        producto.setId(null);
        return productoRepository.save(producto);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param id    identificador del producto a actualizar
     * @param datos nuevos valores del producto
     * @return el producto actualizado
     * @throws RecursoNoEncontradoException si no existe un producto con ese id
     */
    public Producto actualizar(Long id, Producto datos) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        log.info("Actualizando producto con id {}", id);
        return productoRepository.save(existente);
    }

    /**
     * Elimina un producto existente.
     *
     * @param id identificador del producto a eliminar
     * @throws RecursoNoEncontradoException si no existe un producto con ese id
     */
    public void eliminar(Long id) {
        productoRepository.delete(buscarPorId(id));
        log.info("Producto con id {} eliminado", id);
    }
}
