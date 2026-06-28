--liquibase formatted sql

--changeset compra:1
CREATE TABLE IF NOT EXISTS compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_servicio BIGINT NOT NULL,
    fecha_compra DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

--changeset compra:2
INSERT INTO compra (id_usuario, id_servicio, fecha_compra, estado, descripcion) VALUES
(1, 1, '2026-06-10 10:00:00', 'PAGADO', 'Consulta médica general'),
(2, 2, '2026-06-11 11:30:00', 'PENDIENTE', 'Examen de laboratorio'),
(3, 1, '2026-06-12 09:15:00', 'CANCELADO', 'Consulta especialista');
