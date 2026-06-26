package com.example.servicios_service.dto;

import com.example.servicios_service.model.Servicio;
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
@Schema(description = "DTO de Servicio medico")
public class ServicioDTO {

    @Schema(description = "ID unico del servicio", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150)
    @Schema(description = "Nombre del servicio medico", example = "Cardiologia")
    private String nombre;

    @Size(max = 500)
    @Schema(description = "Descripcion del servicio", example = "Evaluacion cardiovascular completa")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Min(0)
    @Schema(description = "Precio del servicio en pesos", example = "85000.0")
    private Double precio;

    @NotNull(message = "La duracion es obligatoria")
    @Min(1)
    @Schema(description = "Duracion en minutos", example = "45")
    private Integer duracionMinutos;

    @Schema(description = "Estado activo del servicio", example = "true")
    private Boolean activo;

    public Servicio toModel() {
        return new Servicio(id, nombre, descripcion, precio, duracionMinutos, activo);
    }

    public static ServicioDTO fromModel(Servicio s) {
        if (s == null) return null;
        return new ServicioDTO(s.getId(), s.getNombre(), s.getDescripcion(),
                s.getPrecio(), s.getDuracionMinutos(), s.getActivo());
    }
}
