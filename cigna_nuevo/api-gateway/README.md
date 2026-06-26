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
| `/clientes/**` | cliente-service | 9091 |
| `/compras/**` | compra-service | 9092 |
| `/productos/**` | producto-service | 9093 |
| `/pagos/**` | pago-service | 9095 |
| `/reservas/**` | reserva-service | 9096 |
| `/historial/**` | historial-service | 9097 |
| `/envios/**` | envio-service | 9098 |

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
