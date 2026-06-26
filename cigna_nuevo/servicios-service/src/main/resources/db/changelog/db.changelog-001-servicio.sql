--liquibase formatted sql

--changeset servicio:1
CREATE TABLE IF NOT EXISTS servicio (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(150) NOT NULL,
	descripcion VARCHAR(500),
	precio DOUBLE NOT NULL,
	duracion_minutos INT NOT NULL,
	activo BOOLEAN
);

--changeset servicio:2
INSERT INTO servicio (nombre, descripcion, precio, duracion_minutos, activo) VALUES
('Consulta General', 'Consulta medica general con diagnostico', 25000, 30, true),
('Cardiologia', 'Evaluacion cardiovascular completa', 85000, 45, true),
('Pediatria', 'Atencion medica para menores de 15 anos', 35000, 30, true),
('Traumatologia', 'Atencion de lesiones musculoesqueleticas', 70000, 40, true);
