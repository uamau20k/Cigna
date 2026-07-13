# servicios-service

## Descripción
Catálogo de servicios médicos disponibles en el sistema.

## Responsabilidad
Administra el catálogo de servicios (consultas, especialidades, procedimientos) con su precio y duración en minutos.

## Puerto
`9100`

## Base de datos
MySQL: `bd_servicios`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/servicios` | Listar todos |
| GET | `/servicios/{id}` | Obtener por ID |
| GET | `/servicios/{id}/exists` | Verificar si existe |
| GET | `/servicios/v2` | Listar con HATEOAS |
| GET | `/servicios/v2/{id}` | Obtener con HATEOAS |
| POST | `/servicios` | Crear |
| PUT | `/servicios/{id}` | Actualizar |
| DELETE | `/servicios/{id}` | Eliminar |

## Comunicación entre servicios
- **Consume:** Ninguno
- **Es consumido por:** agenda-service, reservas-service, compras-service, resena-service

## Swagger UI
```
http://localhost:9100/doc/swagger-ui.html
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
cd servicios-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up servicios-service --build
```

## Ejecutar pruebas
```bash
cd servicios-service
.\mvnw test
# Reporte JaCoCo: target/site/jacoco/index.html
```
