package com.example.resena_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenas", uniqueConstraints = @UniqueConstraint(columnNames = {"idUsuario", "idServicio"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(nullable = false)
    private Long idUsuario;

    @NotNull(message = "El ID del servicio es obligatorio")
    @Column(nullable = false)
    private Long idServicio;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    @Column(length = 500)
    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaResena;

    @PrePersist
    public void prePersist() {
        this.fechaResena = LocalDateTime.now();
    }
}