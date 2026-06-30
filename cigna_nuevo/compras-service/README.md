# compras-service

## Descripción
Gestión de compras de servicios médicos para el sistema de comercio.

## Responsabilidad
Valida estados (PENDIENTE/PAGADO/CANCELADO) y verifica la existencia del usuario y del servicio antes de registrar o actualizar una compra.

## Comunicación entre servicios
→ usuarios-service (`/usuarios/{id}/exists`)  
→ servicios-service (`/servicios/{id}/exists`)

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/compras` | Listar todos |
| GET | `/compras/{id}` | Obtener por ID |
| GET | `/compras/{id}/exists` | Verificar existencia |
| POST | `/compras` | Crear |
| PUT | `/compras/{id}` | Actualizar |
| DELETE | `/compras/{id}` | Eliminar |

## Puerto
`9098`

## Base de datos
MySQL: `bd_compras`

## Swagger UI
```
http://localhost:9098/doc/swagger-ui.html
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
docker compose up compras-service --build
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
