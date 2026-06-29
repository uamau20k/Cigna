<div align="center">
🏥 CIGNA PROJECT
Sistema de Gestión Clínica basado en Arquitectura de Microservicios
<p>












</p>

"Una plataforma distribuida para la gestión integral de procesos clínicos, desarrollada con tecnologías modernas del ecosistema Spring."

</div>
✨ Características
<table> <tr> <td width="50%">
🔐 Seguridad
JWT Authentication
Spring Security
Roles y permisos
Endpoints protegidos
</td> <td width="50%">
☁️ Microservicios
API Gateway
Eureka Discovery
Spring Cloud
Comunicación REST
</td> </tr> <tr> <td>
🗄 Persistencia
MySQL
Liquibase
Bases independientes
JPA / Hibernate
</td> <td>
🚀 DevOps
Docker
Maven
Swagger
Railway / Render
</td> </tr> </table>
🏗 Arquitectura
                   👨‍⚕️ Cliente
                        │
                 🌐 API Gateway
                        │
        ─────────────────────────────────
        │              │               │
     🔐 Auth      👤 Usuarios     🩺 Servicios
        │              │               │
     📅 Agenda     📋 Reservas     💊 Tratamientos
        │              │               │
      💳 Pagos      🛒 Compras     📖 Historial
                        │
                  🔔 Notificaciones
                        │
                  🏥 Sucursales
🧩 Ecosistema de Microservicios
🚀 Servicio	📌 Responsabilidad
🔐 Auth	Autenticación mediante JWT
👤 Usuarios	Gestión de usuarios
🩺 Servicios	Servicios clínicos
💊 Tratamientos	Tratamientos médicos
📅 Agendas	Agenda médica
📋 Reservas	Reservas de atención
🛒 Compras	Compras del sistema
💳 Pagos	Gestión de pagos
📖 Historial	Historial clínico
🏥 Sucursales	Administración de sucursales
🔔 Notificaciones	Envío de notificaciones
⚙️ Stack Tecnológico
<div align="center">
Backend	Cloud	Seguridad	Persistencia	DevOps
☕ Java 21	☁️ Spring Cloud	🔐 JWT	🐬 MySQL	🐳 Docker
🌱 Spring Boot	🌐 Eureka	Spring Security	Liquibase	Maven
</div>
📂 Organización
cigna/
│
├── discovery-server/
├── api-gateway/
│
├── auth-service/
├── usuario-service/
├── servicio-service/
├── agenda-service/
├── reserva-service/
├── compra-service/
├── pago-service/
├── historial-service/
├── tratamiento-service/
├── sucursal-service/
└── notificacion-service/
📸 Capturas
<table> <tr> <td align="center">
Eureka

📷

</td> <td align="center">
Swagger

📷

</td> </tr> <tr> <td align="center">
Docker

📷

</td> <td align="center">
Postman

📷

</td> </tr> </table>
📚 Documentación

Cada microservicio cuenta con su propio README, donde encontrarás:

✔ Descripción
✔ Responsabilidad
✔ Comunicación entre servicios
✔ Endpoints
✔ Swagger
✔ Variables de entorno
✔ Docker
✔ Despliegue
✔ Tests
<div align="center">
⭐ Arquitectura Moderna ⭐

Spring Boot • Spring Cloud • Docker • JWT • MySQL • Liquibase

💙 Gracias por visitar Cigna Project

</div>
