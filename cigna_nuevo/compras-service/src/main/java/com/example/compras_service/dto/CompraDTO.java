package com.example.compras_service.dto;

import com.example.compras_service.model.Compra;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Long idServicio;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date fechaCompra;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    private String descripcion;

    public Compra toModel() {
        return new Compra(id, idPaciente, idServicio, fechaCompra, estado, descripcion);
    }

    public static CompraDTO fromModel(Compra compra) {
        return new CompraDTO(
                compra.getId(),
                compra.getIdPaciente(),
                compra.getIdServicio(),
                compra.getFechaCompra(),
                compra.getEstado(),
                compra.getDescripcion()
        );
    }
}
