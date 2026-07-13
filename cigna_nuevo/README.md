# 📚 Cigna Project - Guía de Desarrollo

Esta carpeta contiene el código fuente de los microservicios que conforman **Cigna Project**, una plataforma clínica desarrollada con **Spring Boot** bajo una arquitectura de microservicios.

> La documentación específica de cada servicio se encuentra en el `README.md` correspondiente dentro de cada microservicio.

---

# ✅ Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

* Java 21
* Maven
* Docker Desktop (opcional, recomendado)
* MySQL 8.x
* Git
* Postman o Insomnia

---

# 🚀 Primer inicio

1. Clona el repositorio.

```bash
git clone https://github.com/uamau20k/Cigna.git
cd Cigna
```

2. Configura las bases de datos MySQL necesarias.

3. Configura las variables de entorno de cada microservicio según corresponda.

4. Ejecuta los microservicios respetando las dependencias entre ellos.

Se recomienda iniciar en el siguiente orden:

1. Auth Service
2. API Gateway
3. Usuario Service
4. Servicio Service
5. Tratamiento Service
6. Agenda Service
7. Reserva Service
8. Compra Service
9. Pago Service
10. Historial Service
11. Sucursal Service
12. Notificación Service
13. Reseña Service

---

# 🐳 Ejecución con Docker

Si utilizas Docker Compose:

```bash
docker compose up --build
```

o levantar un servicio específico:

```bash
docker compose up auth-service --build
```

se creo un archivo .bat para poder ejecutar todos los servicios, 
dentro de cigna-nuevo:
```bash
.\build-all.bat
```

---

# 📂 Microservicios

| Servicio             | Documentación                    |
| -------------------- | -------------------------------- |
| Auth Service         | `auth-service/README.md`         |
| Usuario Service      | `usuario-service/README.md`      |
| Servicio Service     | `servicio-service/README.md`     |
| Tratamiento Service  | `tratamiento-service/README.md`  |
| Agenda Service       | `agenda-service/README.md`       |
| Reserva Service      | `reserva-service/README.md`      |
| Compra Service       | `compra-service/README.md`       |
| Pago Service         | `pago-service/README.md`         |
| Historial Service    | `historial-service/README.md`    |
| Sucursal Service     | `sucursal-service/README.md`     |
| Notificación Service | `notificacion-service/README.md` |
| Reseña Service       | `resena-service/README.md`       |

---

# 🔐 Autenticación

La plataforma utiliza autenticación mediante **JWT**.

Para acceder a los endpoints protegidos:

1. Inicia sesión mediante **Auth Service**.
2. Obtén un token JWT.
3. Incluye el token en el encabezado de las peticiones:

```http
Authorization: Bearer <token>
```

---

# 📚 Documentación API

Cada microservicio expone su propia documentación Swagger.

La URL tiene el siguiente formato:

```
http://localhost:{PUERTO}/doc/swagger-ui.html
```

Consulta el README de cada servicio para conocer el puerto correspondiente.

---

# 🗄 Bases de datos

Cada microservicio administra su propia base de datos MySQL, siguiendo el principio de independencia de datos característico de una arquitectura de microservicios.

Las migraciones son gestionadas mediante **Liquibase**.

---

# 🧪 Pruebas

Cada servicio puede ejecutar sus pruebas de manera independiente.

```bash
./mvnw test
```

Los reportes de cobertura se generan con **JaCoCo**.

---

# ☁️ Despliegue

Los microservicios están preparados para ejecutarse tanto en entorno local como en plataformas de despliegue como Railway o Render mediante perfiles de Spring.

---

# 📖 Documentación adicional

Para conocer los endpoints, variables de entorno, comunicación entre servicios y configuración específica, consulta el `README.md` de cada microservicio.
