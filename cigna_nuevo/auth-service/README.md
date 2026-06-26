# auth-service

## Descripción
Microservicio de autenticación JWT. Gestiona el registro y login de usuarios, generando tokens JWT utilizados por todos los demás microservicios.

## Responsabilidad
Emite tokens JWT firmados con HMAC-SHA. Los demás servicios validan ese token via `oauth2-resource-server`.

## Puerto
`9094`

## Base de datos
MySQL: `bd_users`

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/login` | Login — retorna token JWT |
| POST | `/auth/register` | Registro de nuevo usuario |

### Ejemplo login
```json
POST /auth/login
{
  "email": "usuario@ejemplo.cl",
  "password": "123456"
}
```
### Respuesta
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Swagger UI
```
http://localhost:9094/doc/swagger-ui.html
```

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT compartida con todos los servicios |

## Ejecución local
```bash
cd auth-service
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up auth-service --build
```
