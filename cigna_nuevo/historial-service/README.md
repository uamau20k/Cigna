# historial-service

## Descripción
Gestión de historial clínico para el sistema de comercio.

## Responsabilidad
Registra diagnósticos y tratamientos asociados a un usuario. Valida la existencia del usuario antes de crear o actualizar un registro.

## Comunicación entre servicios
→ usuarios-service (`/usuarios/{id}/exists`)

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/historial` | Listar todos |
| GET | `/historial/{id}` | Obtener por ID |
| GET | `/historial/{id}/exists` | Verificar existencia |
| GET | `/historial/usuario/{idUsuario}` | Listar por usuario |
| GET | `/historial/v2` | Listar con HATEOAS |
| GET | `/historial/v2/{id}` | Obtener con HATEOAS |
| POST | `/historial` | Crear |
| PUT | `/historial/{id}` | Actualizar |
| DELETE | `/historial/{id}` | Eliminar |

## Puerto
`9097`

## Base de datos
MySQL: `bd_historial`

## Swagger UI
```
http://localhost:9097/doc/swagger-ui.html
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

# O directamente (usa application.yml)
./mvnw spring-boot:run
```

## Ejecución con Docker
```bash
# Desde la raíz del proyecto
docker compose up historial-service --build
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
