--liquibase formatted sql

--changeset reserva:1
CREATE TABLE IF NOT EXISTS reserva (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	id_usuario BIGINT NOT NULL,
	id_servicio BIGINT NOT NULL,
	id_tratamiento BIGINT NOT NULL,
	fecha_reserva DATETIME NOT NULL,
	descripcion VARCHAR(255) NULL,
	estado VARCHAR(50) NOT NULL
);

--changeset reserva:2
INSERT INTO reserva (id_usuario, id_servicio, id_tratamiento, fecha_reserva, descripcion, estado) VALUES
(1, 1, 1, '2026-06-10 10:00:00', 'Consulta General', 'PENDIENTE'),
(1, 2, 2, '2026-06-11 14:30:00', 'Control Nutricional', 'CONFIRMADA'),
(2, 3, 3, '2026-06-12 09:00:00', 'Fisioterapia', 'PENDIENTE');
