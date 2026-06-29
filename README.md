🏥 Cigna Project

<div align="center">

Sistema de Gestión Clínica basado en Microservicios

Arquitectura distribuida desarrollada con Spring Boot, Spring Cloud y Java 21, enfocada en la administración integral de procesos clínicos mediante servicios desacoplados.










</div>

📖 Descripción

Cigna Project es una plataforma desarrollada bajo una arquitectura de microservicios que simula el funcionamiento de un sistema clínico moderno.

Cada dominio de negocio se implementa como un servicio independiente, lo que permite un desarrollo desacoplado, una mayor escalabilidad y una mejor mantenibilidad del sistema.

La plataforma incorpora autenticación mediante JWT, descubrimiento de servicios con Eureka, un API Gateway como punto de entrada único, documentación con Swagger/OpenAPI y persistencia independiente para cada microservicio.

🏗 Arquitectura
graph TD

Cliente --> API_Gateway

API_Gateway --> Auth
API_Gateway --> Usuarios
API_Gateway --> Servicios
API_Gateway --> Tratamientos
API_Gateway --> Agendas
API_Gateway --> Reservas
API_Gateway --> Compras
API_Gateway --> Pagos
API_Gateway --> Historial
API_Gateway --> Sucursales
API_Gateway --> Notificaciones

Reservas --> Usuarios
Reservas --> Agendas
Compras --> Pagos
Compras --> Servicios
Historial --> Usuarios
Historial --> Tratamientos
Notificaciones --> Usuarios
📦 Microservicios
Servicio	Responsabilidad
🔐 Auth Service	Autenticación y autorización mediante JWT.
👤 Usuario Service	Gestión de usuarios del sistema.
🩺 Servicio Service	Administración de servicios clínicos.
💊 Tratamiento Service	Gestión de tratamientos médicos.
📅 Agenda Service	Administración de agendas y disponibilidad.
📋 Reserva Service	Gestión de reservas de atención.
🛒 Compra Service	Registro de compras realizadas por los usuarios.
💳 Pago Service	Procesamiento de pagos asociados a compras.
📖 Historial Service	Administración del historial clínico.
🏥 Sucursal Service	Gestión de sucursales y sedes.
🔔 Notificación Service	Envío de notificaciones a los usuarios.
🚀 Características
Arquitectura basada en microservicios.
API Gateway como punto único de acceso.
Eureka Discovery Server.
Seguridad mediante JWT.
Swagger/OpenAPI en todos los servicios.
Comunicación REST entre microservicios.
Migraciones automáticas con Liquibase.
Bases de datos independientes por servicio.
Contenedorización mediante Docker.
Preparado para despliegue en Railway o Render.
📁 Organización del repositorio
cigna/
│
├── discovery-server/
├── api-gateway/
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
├── notificacion-service/
│
└── docs/
📚 Documentación

Cada microservicio posee su propia documentación técnica, donde encontrarás:

Descripción del servicio
Responsabilidades
Comunicación con otros servicios
Endpoints REST
Swagger UI
Variables de entorno
Ejecución local
Docker
Despliegue
Tests y cobertura

Esto permite mantener una documentación modular y sencilla de mantener.

🔒 Seguridad

El sistema utiliza autenticación basada en JSON Web Token (JWT).

El flujo de autenticación es:

El usuario inicia sesión mediante Auth Service.
Se genera un token JWT.
El cliente envía el token en cada petición.
Los microservicios validan el token antes de procesar la solicitud.
📸 Próximamente

Se incorporarán imágenes de:

Dashboard de Eureka
Swagger UI
Docker Compose
Arquitectura del sistema
Colección Postman
👨‍💻 Equipo

Proyecto desarrollado como parte de una solución académica para la implementación de una arquitectura de microservicios utilizando el ecosistema Spring.

📄 Licencia

Proyecto desarrollado con fines académicos.
