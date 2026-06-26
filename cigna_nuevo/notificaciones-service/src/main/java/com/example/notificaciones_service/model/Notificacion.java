package com.example.notificaciones_service.model;

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
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="El ID del cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;
    @NotBlank(message="El tipo es obligatorio") @Pattern(regexp="EMAIL|SMS|PUSH")
    @Column(nullable = false)
    private String tipo;
    @NotBlank(message="El mensaje es obligatorio") @Size(max=500)
    @Column(nullable = false)
    private String mensaje;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    private Boolean leido;
}
