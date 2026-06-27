--liquibase formatted sql

--changeset historial:1
CREATE TABLE IF NOT EXISTS historial_clinico (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	id_usuario BIGINT NOT NULL,
	fecha DATE NOT NULL,
	diagnostico VARCHAR(500) NOT NULL,
	tratamiento VARCHAR(500) NOT NULL
);

--changeset historial:2
INSERT INTO historial_clinico (id_usuario, fecha, diagnostico, tratamiento) VALUES
(1, '2026-05-01', 'Hipertension arterial leve', 'Control mensual y medicacion'),
(1, '2026-05-15', 'Control rutinario', 'Sin novedad'),
(2, '2026-06-01', 'Lumbalgia cronica', 'Fisioterapia 2 veces por semana');
