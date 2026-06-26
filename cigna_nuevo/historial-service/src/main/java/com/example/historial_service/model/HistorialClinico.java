package com.example.historial_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "historial_clinico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @NotBlank(message = "El diagnostico es obligatorio")
    @Column(nullable = false, length = 500)
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Column(nullable = false, length = 500)
    private String tratamiento;
}
