package com.example.reservas_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaReserva;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false, length = 255)
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false, length = 50)
    private String estado;
}
