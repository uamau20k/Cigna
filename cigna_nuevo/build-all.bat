@echo off
echo Building all services...

cd auth-service
call .\mvnw package -DskipTests
cd ..

cd api-gateway
call .\mvnw package -DskipTests
cd ..

cd usuarios-service
call .\mvnw package -DskipTests
cd ..

cd sucursales-service
call .\mvnw package -DskipTests
cd ..

cd servicios-service
call .\mvnw package -DskipTests
cd ..

cd agenda-service
call .\mvnw package -DskipTests
cd ..

cd reservas-service
call .\mvnw package -DskipTests
cd ..

cd pagos-service
call .\mvnw package -DskipTests
cd ..

cd notificaciones-service
call .\mvnw package -DskipTests
cd ..

cd tratamientos-service
call .\mvnw package -DskipTests
cd ..

cd historial-service
call .\mvnw package -DskipTests
cd ..

cd compras-service
call .\mvnw package -DskipTests
cd ..

echo All services built successfully!
docker compose up --build
