package com.example.historial_service.dto;

import com.example.historial_service.model.HistorialClinico;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoDTO {

    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date fecha;

    @NotBlank(message = "El diagnostico es obligatorio")
    @Size(min = 5, max = 500, message = "El diagnostico debe tener entre 5 y 500 caracteres")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(min = 5, max = 500, message = "El tratamiento debe tener entre 5 y 500 caracteres")
    private String tratamiento;

    public HistorialClinico toModel() {
        return new HistorialClinico(id, idUsuario, fecha, diagnostico, tratamiento);
    }

    public static HistorialClinicoDTO fromModel(HistorialClinico h) {
        return new HistorialClinicoDTO(h.getId(), h.getIdUsuario(), h.getFecha(),
                h.getDiagnostico(), h.getTratamiento());
    }
}
