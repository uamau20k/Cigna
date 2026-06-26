--liquibase formatted sql

--changeset pago:1
CREATE TABLE IF NOT EXISTS pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_compra BIGINT NOT NULL,
    valor_neto INT NOT NULL,
    iva INT NOT NULL,
    descuento INT NOT NULL,
    total_pagar INT NOT NULL,
    medio_pago VARCHAR(50) NOT NULL,
    fecha DATETIME NOT NULL
);

--changeset pago:2
INSERT INTO pago (id_compra, valor_neto, iva, descuento, total_pagar, medio_pago, fecha) VALUES
(1, 100000, 19000, 0, 119000, 'TARJETA', '2026-06-10 10:00:00'),
(2, 50000, 9500, 10, 58500, 'TRANSFERENCIA', '2026-06-11 11:30:00'),
(3, 75000, 14250, 5, 89250, 'EFECTIVO', '2026-06-12 09:15:00');