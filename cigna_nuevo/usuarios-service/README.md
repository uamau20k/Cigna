# usuarios-service

## Descripción
Gestión de usuarios del sistema.

## Responsabilidad
Administra el CRUD de usuarios. Expone el endpoint `/usuarios/{id}/exists` que otros servicios consultan para validar que un usuario existe antes de crear compras, reservas o historiales.

## Puerto
`9091`

## Base de datos
MySQL: `bd_usuarios`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/usuarios` | Listar todos los usuarios |
| GET | `/usuarios/{id}` | Obtener usuario por ID |
| GET | `/usuarios/{id}/exists` | Verificar si existe (usado por otros servicios) |
| POST | `/usuarios` | Crear usuario |
| PUT | `/usuarios/{id}` | Actualizar usuario |
| DELETE | `/usuarios/{id}` | Eliminar usuario |

## Swagger UI
```
http://localhost:9091/doc/swagger-ui.html
```

## Persistencia
- **Liquibase** (`db.changelog.sql`): inserta usuarios iniciales al arrancar

## Comunicación
Es consumido por: `compras-service`, `reservas-service`, `historial-service`, `agenda-service`, `notificaciones-service`

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT para validar tokens |

## Ejecución local
```bash
cd usuarios-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up usuarios-service --build
```
