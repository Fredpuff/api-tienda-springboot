package com.example.api.demo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración que recorren la matriz de casos de la guía
 * (Tabla 3) para los módulos de productos y usuarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiCrudTests {

    @Autowired
    private MockMvc mvc;

    private static final String PRODUCTO = """
            {"nombre": "Teclado mecánico", "descripcion": "Switches rojos, retroiluminado", "precio": 189900, "stock": 15}
            """;
    private static final String PRODUCTO_2 = """
            {"nombre": "Mouse inalámbrico", "descripcion": "2.4 GHz", "precio": 59900, "stock": 30}
            """;
    private static final String USUARIO = """
            {"nombre": "Ana Pérez", "correo": "ana@correo.com", "contrasena": "secreta123", "rol": "ADMIN"}
            """;
    private static final String USUARIO_2 = """
            {"nombre": "Luis Gómez", "correo": "luis@correo.com", "contrasena": "clave456", "rol": "CLIENTE"}
            """;

    @Test
    @Order(1)
    void crudProductos() throws Exception {
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(PRODUCTO))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(PRODUCTO_2))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/productos")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        mvc.perform(get("/api/productos/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Teclado mecánico"));
        mvc.perform(get("/api/productos/999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        mvc.perform(put("/api/productos/1").contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCTO.replace("189900", "175000")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.precio").value(175000.0));
        mvc.perform(put("/api/productos/999").contentType(MediaType.APPLICATION_JSON).content(PRODUCTO))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/productos/2")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/productos/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/productos")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content("{\"precio\": -1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    void crudUsuarios() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(USUARIO))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(USUARIO_2))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/usuarios")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].contrasena").doesNotExist());
        mvc.perform(get("/api/usuarios/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("ana@correo.com"))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
        mvc.perform(get("/api/usuarios/999")).andExpect(status().isNotFound());
        mvc.perform(put("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                        .content(USUARIO.replace("ADMIN", "SUPERVISOR")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.rol").value("SUPERVISOR"));
        mvc.perform(put("/api/usuarios/999").contentType(MediaType.APPLICATION_JSON).content(USUARIO))
                .andExpect(status().isNotFound());
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(USUARIO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ya existe un usuario con el correo ana@correo.com"));
        mvc.perform(put("/api/usuarios/2").contentType(MediaType.APPLICATION_JSON).content(USUARIO))
                .andExpect(status().isBadRequest());
        mvc.perform(delete("/api/usuarios/2")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/usuarios/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/usuarios")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }
}
