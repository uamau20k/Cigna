package com.cigna.tratamientos_service.dto;

import com.cigna.tratamientos_service.model.Tratamiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de Tratamiento medico")
public class TratamientoDTO {

    @Schema(description = "ID unico del tratamiento", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150)
    @Schema(description = "Nombre del tratamiento", example = "Fisioterapia")
    private String nombre;

    @Size(max = 500)
    @Schema(description = "Descripcion del tratamiento", example = "Sesiones de rehabilitacion fisica")
    private String descripcion;

    @NotNull(message = "La duracion es obligatoria")
    @Min(1)
    @Schema(description = "Duracion en dias del tratamiento", example = "30")
    private Integer duracionDias;

    @Size(max = 500)
    @Schema(description = "Medicamentos asociados al tratamiento", example = "Sin medicamentos")
    private String medicamentos;

    @Schema(description = "Estado activo del tratamiento", example = "true")
    private Boolean activo;

    public Tratamiento toModel() {
        return new Tratamiento(id, nombre, descripcion, duracionDias, medicamentos, activo);
    }

    public static TratamientoDTO fromModel(Tratamiento t) {
        if (t == null) return null;
        return new TratamientoDTO(t.getId(), t.getNombre(), t.getDescripcion(),
                t.getDuracionDias(), t.getMedicamentos(), t.getActivo());
    }
}
