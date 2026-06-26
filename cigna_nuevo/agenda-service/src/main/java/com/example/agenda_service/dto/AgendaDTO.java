package com.example.agenda_service.dto;

import com.example.agenda_service.model.Agenda;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de Agenda medica")
public class AgendaDTO {

    @Schema(description = "ID unico del bloque de agenda", example = "1")
    private Long id;

    @NotNull(message = "El ID del medico es obligatorio")
    @Schema(description = "ID del medico asignado", example = "1")
    private Long idMedico;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    @Schema(description = "ID de la sucursal", example = "1")
    private Long idSucursal;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Fecha del bloque horario", example = "2026-10-01")
    private Date fecha; 

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Schema(description = "Hora de inicio del bloque", example = "09:00")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    @Schema(description = "Hora de fin del bloque", example = "09:30")
    private String horaFin;

    @Schema(description = "Disponibilidad del bloque", example = "true")
    private Boolean disponible;

    public Agenda toModel() {
        return new Agenda(id, idMedico, idSucursal, fecha, horaInicio, horaFin, disponible);
    }

    public static AgendaDTO fromModel(Agenda a) {
        if (a == null) return null;
        return new AgendaDTO(a.getId(), a.getIdMedico(), a.getIdSucursal(),
                a.getFecha(), a.getHoraInicio(), a.getHoraFin(), a.getDisponible());
    }
}
