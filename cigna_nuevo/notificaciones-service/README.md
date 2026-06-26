# notificaciones-service

## Descripción
Gestión de notificaciones enviadas a pacientes.

## Responsabilidad
Registra y gestiona notificaciones (EMAIL, SMS, PUSH) dirigidas a pacientes. Valida existencia del paciente vía WebClient.

## Puerto
`9102`

## Base de datos
MySQL: `bd_notificaciones`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/notificaciones` | Listar todos |
| GET | `/notificaciones/{id}` | Obtener por ID |
| GET | `/notificaciones/{id}/exists` | Verificar si existe |
| GET | `/notificaciones/v2` | Listar con HATEOAS |
| GET | `/notificaciones/v2/{id}` | Obtener con HATEOAS |
| POST | `/notificaciones` | Crear |
| PUT | `/notificaciones/{id}` | Actualizar |
| DELETE | `/notificaciones/{id}` | Eliminar |

## Comunicación entre servicios
- **Consume:** usuarios-service
- **Es consumido por:** Ninguno

## Swagger UI
```
http://localhost:9102/doc/swagger-ui.html
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
cd notificaciones-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up notificaciones-service --build
```

## Ejecutar pruebas
```bash
cd notificaciones-service
.\mvnw test
# Reporte JaCoCo: target/site/jacoco/index.html
```
