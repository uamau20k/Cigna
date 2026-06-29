package com.example.reservas_service.dto;

import com.example.reservas_service.model.Reserva;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long idServicio;

    @NotNull(message = "El ID del tratamiento es obligatorio")
    private Long idTratamiento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date fechaReserva;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
        regexp = "PENDIENTE|CONFIRMADA|CANCELADA",
        message = "El estado debe ser PENDIENTE, CONFIRMADA o CANCELADA"
    )
    private String estado;

    public Reserva toModel() {
        return new Reserva(id, idUsuario, idServicio, idTratamiento, fechaReserva, descripcion, estado);
    }

    public static ReservaDTO fromModel(Reserva r) {
        return new ReservaDTO(r.getId(), r.getIdUsuario(), r.getIdServicio(), r.getIdTratamiento(), r.getFechaReserva(),
                r.getDescripcion(), r.getEstado());
    }
}
