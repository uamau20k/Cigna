package com.example.notificaciones_service.dto;

import com.example.notificaciones_service.model.Notificacion;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de Notificacion a paciente")
public class NotificacionDTO {

    @Schema(description = "ID unico de la notificacion", example = "1")
    private Long id;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del paciente destinatario", example = "1")
    private Long idCliente;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "EMAIL|SMS|PUSH")
    @Schema(description = "Canal de notificacion", example = "EMAIL", allowableValues = {"EMAIL", "SMS", "PUSH"})
    private String tipo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500)
    @Schema(description = "Contenido del mensaje", example = "Su reserva ha sido confirmada")
    private String mensaje;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Fecha y hora de envio de la notificacion")
    private Date fechaEnvio;

    @Schema(description = "Indica si la notificacion fue leida", example = "false")
    private Boolean leido;

    public Notificacion toModel() {
        return new Notificacion(id, idCliente, tipo, mensaje, fechaEnvio, leido);
    }

    public static NotificacionDTO fromModel(Notificacion n) {
        if (n == null) return null;
        return new NotificacionDTO(n.getId(), n.getIdCliente(), n.getTipo(),
                n.getMensaje(), n.getFechaEnvio(), n.getLeido());
    }
}
