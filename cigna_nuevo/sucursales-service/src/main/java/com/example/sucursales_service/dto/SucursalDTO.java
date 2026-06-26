package com.example.sucursales_service.dto;

import com.example.sucursales_service.model.Sucursal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de Sucursal medica")
public class SucursalDTO {

    @Schema(description = "ID unico de la sucursal", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150)
    @Schema(description = "Nombre de la sucursal", example = "Clinica Central")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    @Schema(description = "Direccion de la sucursal", example = "Av. Providencia 1234")
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    @Schema(description = "Telefono de contacto", example = "+56912345678")
    private String telefono;

    @NotBlank(message = "La ciudad es obligatoria")
    @Schema(description = "Ciudad donde se ubica la sucursal", example = "Santiago")
    private String ciudad;

    @Schema(description = "Estado activo de la sucursal", example = "true")
    private Boolean activa;

    public Sucursal toModel() {
        return new Sucursal(id, nombre, direccion, telefono, ciudad, activa);
    }

    public static SucursalDTO fromModel(Sucursal s) {
        if (s == null) return null;
        return new SucursalDTO(s.getId(), s.getNombre(), s.getDireccion(),
                s.getTelefono(), s.getCiudad(), s.getActiva());
    }
}
