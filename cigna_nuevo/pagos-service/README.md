# pagos-service

## Descripción
Gestión de pagos para el sistema de comercio.

## Responsabilidad
Calcula IVA (19%) y aplica descuentos sobre el valor neto.

## Comunicación entre servicios
→ compra-service (`/compras/{id}/exists`)

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/pagos` | Listar todos |
| GET | `/pagos/{id}` | Obtener por ID |
| GET | `/pagos/v2` | Listar con HATEOAS |
| GET | `/pagos/v2/{id}` | Obtener con HATEOAS |
| POST | `/pagos` | Crear |
| PUT | `/pagos/{id}` | Actualizar |
| DELETE | `/pagos/{id}` | Eliminar |

## Puerto
`9095`

## Base de datos
MySQL: `bd_pagos`

## Swagger UI
```
http://localhost:9095/doc/swagger-ui.html
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
docker compose up pagos-service --build
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
