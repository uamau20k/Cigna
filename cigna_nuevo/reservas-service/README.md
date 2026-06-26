# reservas-service

## Descripción
Gestión de reservas para el sistema de comercio.

## Responsabilidad
Valida estados (PENDIENTE/CONFIRMADA/CANCELADA). Solo reservas PENDIENTE pueden cancelarse.

## Comunicación entre servicios
→ cliente-service (`/clientes/{id}/exists`)

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/reservas` | Listar todos |
| GET | `/reservas/{id}` | Obtener por ID |
| GET | `/reservas/v2` | Listar con HATEOAS |
| GET | `/reservas/v2/{id}` | Obtener con HATEOAS |
| POST | `/reservas` | Crear |
| PUT | `/reservas/{id}` | Actualizar |
| DELETE | `/reservas/{id}` | Eliminar |

## Puerto
`9096`

## Base de datos
MySQL: `bd_reservas`

## Swagger UI
```
http://localhost:9096/doc/swagger-ui.html
```
Autenticación: **Bearer JWT** (obtener token en `auth-service` → `POST /auth/login`)

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT compartida |
| `DATABASE_URL` | URL de la base de datos (solo perfil prod) |
| `DATABASE_USERNAME` | Usuario BD (solo perfil prod) |
| `DATABASE_PASSWORD` | Contraseña BD (solo perfil prod) |

## Ejecución local
```bash
# Con perfil de desarrollo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# O directamente (usa application.properties)
./mvnw spring-boot:run
```

## Ejecución con Docker
```bash
# Desde la raíz del proyecto
docker compose up reservas-service --build
```

## Despliegue en Railway/Render
1. Conectar el repositorio GitHub a Railway/Render
2. Configurar variables de entorno: `SECRET`, `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
3. Activar perfil: `SPRING_PROFILES_ACTIVE=prod`
4. Ejecutar: `./mvnw package -DskipTests && java -jar target/*.jar`

## Ejecutar tests y cobertura
```bash
./mvnw test
# Reporte JaCoCo en: target/site/jacoco/index.html
```
