# agenda-service

## Descripción
Gestión de la agenda de disponibilidad de médicos por sucursal.

## Responsabilidad
Administra los bloques horarios disponibles de cada médico en cada sucursal. Valida existencia de usuario y sucursal vía WebClient.

## Puerto
`9101`

## Base de datos
MySQL: `bd_agenda`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/agendas` | Listar todos |
| GET | `/agendas/{id}` | Obtener por ID |
| GET | `/agendas/{id}/exists` | Verificar si existe |
| GET | `/agendas/v2` | Listar con HATEOAS |
| GET | `/agendas/v2/{id}` | Obtener con HATEOAS |
| POST | `/agendas` | Crear |
| PUT | `/agendas/{id}` | Actualizar |
| DELETE | `/agendas/{id}` | Eliminar |

## Comunicación entre servicios
- **Consume:** usuarios-service, sucursales-service
- **Es consumido por:** reservas-service

## Swagger UI
```
http://localhost:9101/doc/swagger-ui.html
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
cd agenda-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up agenda-service --build
```

## Ejecutar pruebas
```bash
cd agenda-service
.\mvnw test
# Reporte JaCoCo: target/site/jacoco/index.html
```
