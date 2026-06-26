--liquibase formatted sql

--changeset usuario:1
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

--changeset usuario:2
INSERT INTO usuario (nombre, apellido, correo, telefono, activo) VALUES
('Juan', 'Pérez', 'juan.perez@cigna.cl', '+56912345678', true),
('María', 'González', 'maria.gonzalez@cigna.cl', '+56987654321', true),
('Carlos', 'López', 'carlos.lopez@cigna.cl', '+56911223344', true),
('Ana', 'Martínez', 'ana.martinez@cigna.cl', '+56944556677', true),
('Luis', 'Fernández', 'luis.fernandez@cigna.cl', '+56966778899', true);
