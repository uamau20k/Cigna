# sucursales-service

## Descripción
Gestión de sucursales médicas del sistema de salud.

## Responsabilidad
Administra las sucursales donde se prestan los servicios médicos. Expone `/sucursales/{id}/exists` para validación desde otros servicios.

## Puerto
`9099`

## Base de datos
MySQL: `bd_sucursales`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/sucursales` | Listar todos |
| GET | `/sucursales/{id}` | Obtener por ID |
| GET | `/sucursales/{id}/exists` | Verificar si existe |
| GET | `/sucursales/v2` | Listar con HATEOAS |
| GET | `/sucursales/v2/{id}` | Obtener con HATEOAS |
| POST | `/sucursales` | Crear |
| PUT | `/sucursales/{id}` | Actualizar |
| DELETE | `/sucursales/{id}` | Eliminar |

## Comunicación entre servicios
- **Consume:** Ninguno
- **Es consumido por:** agenda-service

## Swagger UI
```
http://localhost:9099/doc/swagger-ui.html
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
cd sucursales-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up sucursales-service --build
```

## Ejecutar pruebas
```bash
cd sucursales-service
.\mvnw test
# Reporte JaCoCo: target/site/jacoco/index.html
```
