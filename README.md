# API Tienda – Productos y Usuarios

API REST desarrollada con Spring Boot que expone el CRUD completo (crear, consultar, actualizar y eliminar) para las entidades **Producto** y **Usuario**, con arquitectura por capas (controlador → servicio → repositorio → modelo), manejo global de excepciones y Lombok en todas las clases.

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java (JDK) | 25 |
| Spring Boot | 4.1.1 (Web MVC, Data JPA, Validation) |
| Lombok | gestionada por Spring Boot |
| Base de datos | PostgreSQL 17 (contenedor Docker) |
| Pruebas | JUnit 5 + MockMvc + H2 en memoria |
| Gestor de dependencias | Apache Maven (wrapper `mvnw`) |

## Estructura

```
com.example.api.demo
├── controller   HomeController, ProductoController, UsuarioController
├── service      ProductoService, UsuarioService
├── repository   ProductoRepository, UsuarioRepository
├── model        Producto, Usuario
└── exception    RecursoNoEncontradoException, ManejadorGlobalExcepciones
```

## Configuración de la base de datos

La base de datos corre en Docker. El contenedor se publica en el puerto **5433** porque el 5432 lo usa el PostgreSQL instalado en Windows.

```bash
docker compose up -d
```

Esto crea la base `tiendaya` con usuario `tiendaya` / contraseña `tiendaya123`. Las tablas `productos` y `usuarios` se crean automáticamente (`spring.jpa.hibernate.ddl-auto=update`).

Los datos de conexión se pueden cambiar con variables de entorno sin tocar el código:

| Variable | Valor por defecto |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/tiendaya` |
| `DB_USER` | `tiendaya` |
| `DB_PASSWORD` | `tiendaya123` |
| `SERVER_PORT` | `8080` |

## Ejecución

```bash
./mvnw clean install          # compila y ejecuta las pruebas (usan H2, no necesitan Docker)
./mvnw spring-boot:run        # inicia la API en http://localhost:8080
```

En Windows se usa `mvnw.cmd` en lugar de `./mvnw`.

## Endpoints

### Productos – `/api/productos`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/api/productos` | Lista todos los productos | 200 |
| GET | `/api/productos/{id}` | Consulta un producto por id | 200 / 404 |
| POST | `/api/productos` | Crea un producto | 201 / 400 |
| PUT | `/api/productos/{id}` | Actualiza un producto | 200 / 400 / 404 |
| DELETE | `/api/productos/{id}` | Elimina un producto | 204 / 404 |

Petición (POST / PUT):

```json
{
  "nombre": "Teclado mecánico",
  "descripcion": "Switches rojos, retroiluminado",
  "precio": 189900,
  "stock": 15
}
```

Respuesta (201):

```json
{
  "id": 1,
  "nombre": "Teclado mecánico",
  "descripcion": "Switches rojos, retroiluminado",
  "precio": 189900.0,
  "stock": 15
}
```

### Usuarios – `/api/usuarios`

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/api/usuarios` | Lista todos los usuarios | 200 |
| GET | `/api/usuarios/{id}` | Consulta un usuario por id | 200 / 404 |
| POST | `/api/usuarios` | Crea un usuario | 201 / 400 |
| PUT | `/api/usuarios/{id}` | Actualiza un usuario | 200 / 400 / 404 |
| DELETE | `/api/usuarios/{id}` | Elimina un usuario | 204 / 404 |

Reglas de negocio:
- El correo es único: crear o actualizar con un correo ya registrado responde **400**.
- La contraseña se recibe en las peticiones pero **nunca** aparece en las respuestas.

Petición (POST / PUT):

```json
{
  "nombre": "Ana Pérez",
  "correo": "ana@correo.com",
  "contrasena": "secreta123",
  "rol": "ADMIN"
}
```

Respuesta (201):

```json
{
  "id": 1,
  "nombre": "Ana Pérez",
  "correo": "ana@correo.com",
  "rol": "ADMIN"
}
```

### Errores

Todos los errores responden JSON:

```json
{ "error": "Producto no encontrado con id 999" }
```

Los errores de validación devuelven un mensaje por campo, por ejemplo `{ "nombre": "El nombre es obligatorio" }`.

## Pruebas

- `src/test/java/.../ApiCrudTests.java` recorre la matriz de casos de la guía para ambos módulos.
- `postman/API_Tienda.postman_collection.json` es la colección de Postman con las carpetas **Productos** y **Usuarios**.

## Repositorio

https://github.com/Fredpuff/api-tienda-springboot

## Autor

Frederick Alexandro Abril Mogollón
