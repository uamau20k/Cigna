--liquibase formatted sql

--changeset agenda:1
CREATE TABLE IF NOT EXISTS agenda (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	id_medico BIGINT NOT NULL,
	id_sucursal BIGINT NOT NULL,
	fecha DATE NOT NULL,
	hora_inicio VARCHAR(5) NOT NULL,
	hora_fin VARCHAR(5) NOT NULL,
	disponible BOOLEAN NOT NULL DEFAULT TRUE
);

--changeset agenda:2
INSERT INTO agenda (id_medico, id_sucursal, fecha, hora_inicio, hora_fin, disponible) VALUES
(1, 1, '2026-07-01', '08:00', '08:30', true),
(1, 1, '2026-07-01', '09:00', '09:30', true),
(2, 2, '2026-07-02', '10:00', '10:30', false);
