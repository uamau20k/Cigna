# 🏥 Cigna Project

<p align="center">

# Sistema de Gestión Clínica basado en Microservicios

Arquitectura distribuida desarrollada con **Java 21**, **Spring Boot** y **Spring Cloud**, diseñada para administrar procesos clínicos mediante una arquitectura de microservicios desacoplados.

</p>

---

# 🚀 Tecnologías utilizadas

<p align="center">

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-Microservices-success?style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liquibase-2962FF?style=for-the-badge)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

</p>

---

# 📖 Descripción

**Cigna Project** es una plataforma desarrollada bajo una arquitectura de microservicios para gestionar procesos clínicos de forma escalable y desacoplada.

Cada dominio de negocio funciona como un servicio independiente, permitiendo una mejor mantenibilidad, escalabilidad y comunicación entre componentes.

El proyecto incorpora autenticación mediante JWT, API Gateway, Eureka Discovery Server, documentación con Swagger, migraciones con Liquibase y despliegue mediante Docker.

---

# 🏗 Arquitectura

```mermaid
graph TD

Cliente --> API_Gateway

API_Gateway --> Auth_Service
API_Gateway --> Usuario_Service
API_Gateway --> Servicio_Service
API_Gateway --> Tratamiento_Service
API_Gateway --> Agenda_Service
API_Gateway --> Reserva_Service
API_Gateway --> Compra_Service
API_Gateway --> Pago_Service
API_Gateway --> Historial_Service
API_Gateway --> Sucursal_Service
API_Gateway --> Notificacion_Service

Reserva_Service --> Usuario_Service
Reserva_Service --> Agenda_Service

Agenda_Service --> Servicio_Service

Compra_Service --> Pago_Service
Compra_Service --> Servicio_Service

Historial_Service --> Usuario_Service
Historial_Service --> Tratamiento_Service

Notificacion_Service --> Usuario_Service
```

---

# 📦 Microservicios

| Microservicio | Descripción |
|--------------|-------------|
| 🔍 Discovery Server | Registro y descubrimiento de servicios mediante Eureka. |
| 🌐 API Gateway | Punto único de acceso para todos los microservicios. |
| 🔐 Auth Service | Autenticación y autorización mediante JWT. |
| 👤 Usuario Service | Gestión de usuarios del sistema. |
| 🩺 Servicio Service | Administración de servicios clínicos. |
| 💊 Tratamiento Service | Gestión de tratamientos médicos. |
| 📅 Agenda Service | Administración de agendas médicas. |
| 📋 Reserva Service | Gestión de reservas de atención. |
| 🛒 Compra Service | Administración de compras realizadas por usuarios. |
| 💳 Pago Service | Procesamiento de pagos. |
| 📖 Historial Service | Gestión del historial clínico. |
| 🏥 Sucursal Service | Administración de sucursales. |
| 🔔 Notificación Service | Envío de notificaciones del sistema. |

---

# ⭐ Características

- Arquitectura basada en microservicios.
- API Gateway como punto único de acceso.
- Service Discovery mediante Eureka.
- Autenticación con JWT y Spring Security.
- Comunicación REST entre microservicios.
- Documentación con Swagger/OpenAPI.
- Persistencia independiente por servicio.
- Migraciones automáticas mediante Liquibase.
- Contenedorización con Docker.
- Maven como herramienta de construcción.
- Preparado para despliegue en Railway o Render.

---

# 🗂 Estructura del proyecto

```text
cigna/

├── README.md
│
├── discovery-server/
├── api-gateway/
│
├── auth-service/
├── usuario-service/
├── servicio-service/
├── tratamiento-service/
├── agenda-service/
├── reserva-service/
├── compra-service/
├── pago-service/
├── historial-service/
├── sucursal-service/
├── notificacion-service/
│
└── docs/
```

---

# 📷 Capturas

Próximamente se incorporarán capturas de:

- 📊 Eureka Dashboard
- 📚 Swagger UI
- 🐳 Docker Compose
- 📮 Colección Postman
- 🗄️ Bases de datos

---

# 🚀 Inicio rápido

Cada microservicio cuenta con su propia documentación técnica (`README.md`), donde encontrarás:

- 📋 Descripción del servicio
- 🔄 Comunicación con otros microservicios
- 🌐 Endpoints REST
- 📚 Swagger UI
- ⚙️ Variables de entorno
- 🐳 Ejecución con Docker
- ☁️ Despliegue en Railway/Render
- ✅ Tests y cobertura

---

# 👥 Equipo

Proyecto desarrollado por el equipo **Cigna Project** como una solución académica orientada a la implementación de una arquitectura de microservicios utilizando el ecosistema Spring.

---

# 📄 Licencia

Este proyecto fue desarrollado con fines académicos.
