--liquibase formatted sql

--changeset notificacion:1
CREATE TABLE IF NOT EXISTS notificacion (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	id_usuario BIGINT NOT NULL,
	tipo VARCHAR(10) NOT NULL,
	mensaje VARCHAR(500) NOT NULL,
	fecha_envio DATETIME,
	leido BOOLEAN
);

--changeset notificacion:2
INSERT INTO notificacion (id_usuario, tipo, mensaje, fecha_envio, leido) VALUES
(1, 'EMAIL', 'Su reserva ha sido confirmada para manana a las 09:00', '2026-06-10 08:00:00', false),
(2, 'SMS', 'Recuerde su cita medica en Clinica Central', '2026-06-11 07:00:00', true),
(1, 'PUSH', 'Su resultado de examen esta disponible', '2026-06-12 10:00:00', false);
