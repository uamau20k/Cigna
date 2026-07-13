package com.example.resena_service.dto;

import java.time.LocalDateTime;

import com.example.resena_service.model.Resena;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Resena", description = "Modelo para la creación de una nueva reseña o valoración de un producto")
public class ResenaDTO {

    private Long id;

    @Schema(description = "Identificador único del usuario que emite la reseña", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @Schema(description = "Identificador único del servicio que se está valorando", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del servicio es obligatorio")
    private Long idServicio;

    @Schema(description = "Calificación otorgada al servicio (escala del 1 al 5)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @Schema(description = "Comentario opcional detallando la experiencia con el servicio", example = "Excelente servicio, muy atento y profesional.", maxLength = 500)
    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comentario;

    @Schema(description = "Fecha en que se registró la reseña", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaResena;

    public Resena toModel() {
        return new Resena(id, idUsuario, idServicio, calificacion, comentario, null);
    }

    public static ResenaDTO fromModel(Resena resena) {
        return new ResenaDTO(
                resena.getId(),
                resena.getIdUsuario(),
                resena.getIdServicio(),
                resena.getCalificacion(),
                resena.getComentario(),
                resena.getFechaResena()
        );
    }
}