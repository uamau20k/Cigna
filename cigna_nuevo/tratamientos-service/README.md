# tratamientos-service

## Descripción
Catálogo de tratamientos médicos disponibles en el sistema.

## Responsabilidad
Administra tratamientos médicos con nombre, descripción, duración en días y medicamentos asociados.

## Puerto
`9103`

## Base de datos
MySQL: `bd_tratamientos`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/tratamientos` | Listar todos |
| GET | `/tratamientos/{id}` | Obtener por ID |
| GET | `/tratamientos/{id}/exists` | Verificar si existe |
| GET | `/tratamientos/v2` | Listar con HATEOAS |
| GET | `/tratamientos/v2/{id}` | Obtener con HATEOAS |
| POST | `/tratamientos` | Crear |
| PUT | `/tratamientos/{id}` | Actualizar |
| DELETE | `/tratamientos/{id}` | Eliminar |

## Comunicación entre servicios
- **Consume:** Ninguno
- **Es consumido por:** historial-service

## Swagger UI
```
http://localhost:9103/doc/swagger-ui.html
```
> Autenticación: clic en **Authorize** → ingresar `Bearer {token}`

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT compartida |
| `DATABASE_URL` | URL de base de datos (perfil prod) |
| `DATABASE_USERNAME` | Usuario BD (perfil prod) |
| `DATABASE_PASSWORD` | Contraseña BD (perfil prod) |

## Persistencia
- **Liquibase** (`db.changelog-001-*.sql`): inserta datos iniciales al arrancar
- **DataLoader** (Datafaker): genera 5 registros adicionales de prueba

## Ejecución local
```bash
cd tratamientos-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up tratamientos-service --build
```

## Ejecutar pruebas
```bash
cd tratamientos-service
.\mvnw test
# Reporte JaCoCo: target/site/jacoco/index.html
```
