# usuarios-service

## Descripción
Microservicio de gestión de clientes del sistema.

## Responsabilidad
Administra el CRUD de clientes. Expone el endpoint `/clientes/{id}/exists` que otros servicios consultan para validar que un cliente existe antes de crear compras, reservas o historiales.

## Puerto
`9091`

## Base de datos
MySQL: `bd_usuarios`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/clientes` | Listar todos los clientes |
| GET | `/clientes/{id}` | Obtener cliente por ID |
| GET | `/clientes/{id}/exists` | Verificar si existe (usado por otros servicios) |
| POST | `/clientes` | Crear cliente |

### Ejemplo crear cliente
```json
POST /clientes
{
  "nombre": "Juan Pérez",
  "correo": "juan@ejemplo.cl"
}
```

## Swagger UI
```
http://localhost:9091/doc/swagger-ui.html
```

## Persistencia
- **Liquibase** (`db.changelog.sql`): inserta clientes iniciales al arrancar

## Comunicación
Es consumido por: `compra-service`, `reserva-service`, `historial-service`

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
