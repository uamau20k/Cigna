package com.example.agenda_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="El ID del medico es obligatorio")
    @Column(nullable = false)
    private Long idMedico;
    @NotNull(message="El ID de la sucursal es obligatorio")
    @Column(nullable = false)
    private Long idSucursal;
    @NotNull(message="La fecha es obligatoria")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;
    @NotBlank(message="La hora de inicio es obligatoria")
    @Column(nullable = false)
    private String horaInicio;
    @NotBlank(message="La hora de fin es obligatoria")
    @Column(nullable = false)
    private String horaFin;
    private Boolean disponible;
}
