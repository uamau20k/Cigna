# api-gateway

## Descripción
Puerta de entrada centralizada del sistema. Enruta todas las solicitudes hacia los microservicios correspondientes y valida el token JWT antes de dejar pasar cualquier request (excepto `/auth/**`).

## Responsabilidad
- Enrutamiento centralizado mediante Spring Cloud Gateway
- Validación de JWT en todas las rutas protegidas
- Punto único de entrada para el frontend

## Puerto
`9090`

## Rutas configuradas

| Ruta | Servicio destino | Puerto |
|------|-----------------|--------|
| `/auth/**` | auth-service | 9094 |
| `/usuarios/**` | usuarios-service | 9091 |
| `/sucursales/**` | sucursales-service | 9099 |
| `/servicios/**` | servicios-service | 9100 |
| `/agendas/**` | agenda-service | 9101 |
| `/reservas/**` | reservas-service | 9096 |
| `/pagos/**` | pagos-service | 9095 |
| `/notificaciones/**` | notificaciones-service | 9102 |
| `/tratamientos/**` | tratamientos-service | 9103 |
| `/historial/**` | historial-service | 9097 |
| `/compras/**` | compras-service | 9098 |
| `/resenas/**` | resena-service | 9104 |

## Seguridad
- `/auth/**` → público (no requiere token)
- Todas las demás rutas → requieren header `Authorization: Bearer {token}`

## Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `SECRET` | Clave secreta JWT para validar tokens |

## Ejecución local
```bash
cd api-gateway
.\mvnw spring-boot:run
```

## Ejecución con Docker
```bash
docker compose up api-gateway --build
```
