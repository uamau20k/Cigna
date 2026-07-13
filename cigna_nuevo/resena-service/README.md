# resena-service

## Descripción
Gestión de reseñas y valoraciones de servicios médicos para el sistema de comercio.

## Responsabilidad
Permite a un usuario reseñar (calificación 1-5 + comentario opcional) un servicio que haya comprado. Valida que no exista ya una reseña del mismo usuario para el mismo servicio, y verifica la existencia del usuario, del servicio y de la compra antes de registrar la reseña. Expone el listado de reseñas por servicio/usuario y el cálculo del promedio de calificación de un servicio.

## Comunicación entre servicios
→ usuarios-service (`/usuarios/{id}/exists`)
→ servicios-service (`/servicios/{id}/exists`)
→ compras-service (`/compras/usuario/{idUsuario}/servicio/{idServicio}/existe`)

**Es consumido por:** Ninguno

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/resenas` | Listar todas |
| GET | `/resenas/{id}` | Obtener por ID |
| GET | `/resenas/{id}/exists` | Verificar existencia |
| GET | `/resenas/servicio/{idServicio}` | Listar por servicio |
| GET | `/resenas/usuario/{idUsuario}` | Listar por usuario |
| GET | `/resenas/servicio/{idServicio}/promedio` | Promedio de calificación del servicio |
| GET | `/resenas/v2` | Listar con HATEOAS |
| GET | `/resenas/v2/{id}` | Obtener con HATEOAS |
| POST | `/resenas` | Crear |
| PUT | `/resenas/{id}` | Actualizar (calificación/comentario) |
| DELETE | `/resenas/{id}` | Eliminar |

## Puerto
`9104`

## Base de datos
MySQL: `bd_resenas`

## Swagger UI
```
http://localhost:9104/doc/swagger-ui.html
```
Autenticación: **Bearer JWT** (obtener token en `auth-service` → `POST /auth/login`)

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT compartida |

## Ejecución local
```bash
cd resena-service
./mvnw spring-boot:run
```

## Ejecución con Docker
```bash
# Desde la raíz del proyecto
docker compose up resena-service --build
```

## Ejecutar tests y cobertura
```bash
cd resena-service
./mvnw test
# Reporte JaCoCo en: target/site/jacoco/index.html
```
