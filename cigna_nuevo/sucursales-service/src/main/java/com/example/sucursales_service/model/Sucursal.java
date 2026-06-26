package com.example.sucursales_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="El nombre es obligatorio") @Size(min=3,max=150)
    @Column(nullable = false)
    private String nombre;
    @NotBlank(message="La direccion es obligatoria")
    @Column(nullable = false)
    private String direccion;
    @NotBlank(message="El telefono es obligatorio")
    @Column(nullable = false)
    private String telefono;
    @NotBlank(message="La ciudad es obligatoria")
    @Column(nullable = false)
    private String ciudad;
    private Boolean activa;
}
