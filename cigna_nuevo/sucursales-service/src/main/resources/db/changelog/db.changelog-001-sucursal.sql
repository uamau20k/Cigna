--liquibase formatted sql

--changeset sucursal:1
CREATE TABLE IF NOT EXISTS sucursal (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(150) NOT NULL,
	direccion VARCHAR(255) NOT NULL,
	telefono VARCHAR(50) NOT NULL,
	ciudad VARCHAR(100) NOT NULL,
	activa BOOLEAN
);

--changeset sucursal:2
INSERT INTO sucursal (nombre, direccion, telefono, ciudad, activa) VALUES
('Clinica Central', 'Av. Providencia 1234', '+56912345678', 'Santiago', true),
('Clinica Norte', 'Calle Recoleta 567', '+56987654321', 'Santiago', true),
('Clinica Sur', 'Av. Vicuna Mackenna 890', '+56911223344', 'Concepcion', true);
