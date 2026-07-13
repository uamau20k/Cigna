--liquibase formatted sql

--changeset resena:1
CREATE TABLE IF NOT EXISTS resenas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_servicio BIGINT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario VARCHAR(500),
    fecha_resena DATETIME NOT NULL,
    CONSTRAINT uk_resena_usuario_servicio UNIQUE (id_usuario, id_servicio)
);

--changeset resena:2
INSERT INTO resenas (id_usuario, id_servicio, calificacion, comentario, fecha_resena) VALUES
(1, 1, 5, 'Excelente atención, muy profesional y puntual.', '2026-06-01 10:00:00'),
(2, 1, 4, 'Buen servicio, aunque esperé un poco más de lo indicado.', '2026-06-02 11:30:00'),
(1, 2, 5, 'El tratamiento fue muy cómodo y efectivo.', '2026-06-03 14:15:00'),
(3, 3, 3, 'Cumple su función básica, calidad acorde al precio pagado.', '2026-06-04 09:20:00'),
(2, 4, 5, 'Excelente equipo médico, resultados notorios de inmediato.', '2026-06-05 16:45:00'),
(1, 5, 4, 'Buena atención, el especialista explicó todo con claridad.', '2026-06-06 08:10:00'),
(4, 6, 5, 'El servicio vino muy completo y bien explicado.', '2026-06-07 12:00:00'),
(2, 7, 2, 'La sala estaba muy fría durante la consulta, pediré otra hora.', '2026-06-08 15:30:00'),
(3, 8, 5, 'Servicio de muy buena calidad, el trato fue excelente.', '2026-06-08 18:00:00'),
(1, 9, 4, 'El chequeo fue preciso y con buen material clínico.', '2026-06-08 20:00:00');