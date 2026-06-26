# CignaProject - Microservicios

## Requisitos previos
- Java 21
- XAMPP con MySQL corriendo en localhost:3306
- Postman para probar endpoints

## PRIMERA VEZ que levantas el proyecto

Las bases de datos se crean automáticamente. Liquibase crea las tablas e inserta los datos.
Levanta los servicios en este orden esperando que cada uno diga "Started ... in X seconds":

```
Terminal 1: cd discovery-server/discovery-server   → ./mvnw clean spring-boot:run
Terminal 2: cd api-gateway/api-gateway             → ./mvnw clean spring-boot:run
Terminal 3: cd servicio-service/servicio-service   → ./mvnw clean spring-boot:run
Terminal 4: cd tratamiento-service                 → ./mvnw clean spring-boot:run
Terminal 5: cd usuario-service/usuario-service     → ./mvnw clean spring-boot:run
Terminal 6: cd agenda-service/agenda-service       → ./mvnw clean spring-boot:run
Terminal 7: cd reserva-service/reserva-service     → ./mvnw clean spring-boot:run
```

## SEGUNDA VEZ en adelante
Las tablas ya existen. Debes cambiar setShouldRun(true) a setShouldRun(false)
en el LiquibaseConfig.java de cada servicio antes de volver a levantar:

Archivos a modificar:
- usuario-service/.../config/LiquibaseConfig.java
- servicio-service/.../config/LiquibaseConfig.java
- agenda-service/.../config/LiquibaseConfig.java
- reserva-service/.../config/LiquibaseConfig.java
- tratamiento-service/.../config/LiquibaseConfig.java

Cambiar: liquibase.setShouldRun(true)  →  liquibase.setShouldRun(false)

## Verificar que todo esté UP
http://localhost:8761  → deben aparecer 5 servicios

## Endpoints (todos via Gateway puerto 8080)

### usuario-service (puerto 8081)
GET    http://localhost:8080/api/usuarios
GET    http://localhost:8080/api/usuarios/{id}
GET    http://localhost:8080/api/usuarios/run/{run}
POST   http://localhost:8080/api/usuarios
PUT    http://localhost:8080/api/usuarios/{id}
DELETE http://localhost:8080/api/usuarios/{id}

### servicio-service (puerto 8082)
GET    http://localhost:8080/api/servicios
GET    http://localhost:8080/api/servicios/{id}
POST   http://localhost:8080/api/servicios
PUT    http://localhost:8080/api/servicios/{id}
DELETE http://localhost:8080/api/servicios/{id}

### agenda-service (puerto 8084) - comunica con servicio-service
GET    http://localhost:8080/api/agendas
GET    http://localhost:8080/api/agendas/{id}
GET    http://localhost:8080/api/agendas/disponibles
GET    http://localhost:8080/api/agendas/usuario/{idUsuario}
GET    http://localhost:8080/api/agendas/servicio/{idServicio}
GET    http://localhost:8080/api/agendas/servicio/{idServicio}/total
GET    http://localhost:8080/api/agendas/rango?desde=2026-06-01&hasta=2026-06-30
GET    http://localhost:8080/api/agendas/detalles/agenda/{idAgenda}
POST   http://localhost:8080/api/agendas
PUT    http://localhost:8080/api/agendas/{id}
PATCH  http://localhost:8080/api/agendas/{id}/estado?estado=RESERVADO
DELETE http://localhost:8080/api/agendas/{id}

### reserva-service (puerto 8085) - comunica con usuario-service y agenda-service
GET    http://localhost:8080/api/reservas
GET    http://localhost:8080/api/reservas/{id}
GET    http://localhost:8080/api/reservas/usuario/{idUsuario}
GET    http://localhost:8080/api/reservas/usuario/{idUsuario}/total
GET    http://localhost:8080/api/reservas/total/estado?estado=PENDIENTE
GET    http://localhost:8080/api/reservas/rango?desde=2026-05-01T00:00:00&hasta=2026-05-31T23:59:59
POST   http://localhost:8080/api/reservas
PATCH  http://localhost:8080/api/reservas/{id}/estado?estado=CONFIRMADA
DELETE http://localhost:8080/api/reservas/{id}

### tratamiento-service (puerto 8086)
GET    http://localhost:8080/api/tratamientos
GET    http://localhost:8080/api/tratamientos/{id}
POST   http://localhost:8080/api/tratamientos
PUT    http://localhost:8080/api/tratamientos/{id}
DELETE http://localhost:8080/api/tratamientos/{id}

## Bases de datos (se crean automáticamente)
- cigna_usuarios    → tabla: usuarios
- cigna_servicios   → tabla: servicio
- cigna_agendas     → tablas: agenda, detalle_agenda (FK)
- cigna_reservas    → tabla: reserva
- cigna_tratamientos → tabla: tratamiento
