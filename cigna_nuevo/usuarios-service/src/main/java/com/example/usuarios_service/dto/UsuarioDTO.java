package com.example.usuarios_service.dto;

import com.example.usuarios_service.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de Usuario del sistema medico")
public class UsuarioDTO {

    @Schema(description = "ID unico del usuario", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del usuario", example = "Perez")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser valido")
    @Schema(description = "Correo electronico del usuario", example = "juan@cigna.cl")
    private String correo;

    @NotBlank(message = "El telefono es obligatorio")
    @Schema(description = "Numero de telefono", example = "+56912345678")
    private String telefono;

    @Schema(description = "Estado activo del usuario", example = "true")
    private Boolean activo;

    public Usuario toModel() {
        return new Usuario(id, nombre, apellido, correo, telefono, activo);
    }

    public static UsuarioDTO fromModel(Usuario u) {
        if (u == null) return null;
        return new UsuarioDTO(u.getId(), u.getNombre(), u.getApellido(),
                u.getCorreo(), u.getTelefono(), u.getActivo());
    }
}
