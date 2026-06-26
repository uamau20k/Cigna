package com.example.servicios_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="El nombre es obligatorio") @Size(min=3,max=150)
    @Column(nullable = false)
    private String nombre;
    @Size(max=500)
    private String descripcion;
    @NotNull(message="El precio es obligatorio") @Min(0)
    @Column(nullable = false)
    private Double precio;
    @NotNull(message="La duracion es obligatoria") @Min(1)
    @Column(nullable = false)
    private Integer duracionMinutos;
    private Boolean activo;
}
