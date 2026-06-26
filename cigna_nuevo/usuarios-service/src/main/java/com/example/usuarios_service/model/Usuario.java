package com.example.usuarios_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank(message = "El telefono es obligatorio")
    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;
}
