--liquibase formatted sql

--changeset tratamiento:1
CREATE TABLE IF NOT EXISTS tratamiento (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(150) NOT NULL,
	descripcion VARCHAR(500),
	duracion_dias INT NOT NULL,
	medicamentos VARCHAR(500),
	activo BOOLEAN
);

--changeset tratamiento:2
INSERT INTO tratamiento (nombre, descripcion, duracion_dias, medicamentos, activo) VALUES
('Antibioticoterapia', 'Tratamiento con antibioticos para infecciones bacterianas', 10, 'Amoxicilina 500mg', true),
('Fisioterapia', 'Sesiones de rehabilitacion fisica', 30, 'Sin medicamentos', true),
('Control Nutricional', 'Plan nutricional personalizado con seguimiento mensual', 90, 'Suplementos vitaminicos', true),
('Rehabilitacion Cardiaca', 'Programa de recuperacion cardiovascular', 60, 'Betabloqueadores', true);
