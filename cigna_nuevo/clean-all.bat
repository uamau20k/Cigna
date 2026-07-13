@echo off
echo Cleaning all services...

cd api-gateway
call .\mvnw clean
cd ..

cd auth-service
call .\mvnw clean
cd ..

cd usuarios-service
call .\mvnw clean
cd ..

cd sucursales-service
call .\mvnw clean
cd ..

cd servicios-service
call .\mvnw clean
cd ..

cd agenda-service
call .\mvnw clean
cd ..

cd reservas-service
call .\mvnw clean
cd ..

cd pagos-service
call .\mvnw clean
cd ..

cd notificaciones-service
call .\mvnw clean
cd ..

cd tratamientos-service
call .\mvnw clean
cd ..

cd historial-service
call .\mvnw clean
cd ..

cd compras-service
call .\mvnw clean
cd ..

cd resena-service
call .\mvnw clean
cd ..

echo Done!
