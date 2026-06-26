package com.example.usuarios_service.controller;

import com.example.usuarios_service.dto.UsuarioDTO;
import com.example.usuarios_service.model.Usuario;
import com.example.usuarios_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        logger.info("GET /usuarios");
        List<UsuarioDTO> dtos = usuarioService.listar().stream()
                .map(UsuarioDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioDTO> obtener(@PathVariable Long id) {
        logger.info("GET /usuarios/{}", id);
        return ResponseEntity.ok(UsuarioDTO.fromModel(usuarioService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar si existe un usuario")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO dto) {
        logger.info("POST /usuarios correo={}", dto.getCorreo());
        Usuario nuevo = usuarioService.guardar(dto.toModel());
        logger.info("Usuario creado id={}", nuevo.getId());
        return ResponseEntity.ok(UsuarioDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody UsuarioDTO dto) {
        logger.info("PUT /usuarios/{}", id);
        return ResponseEntity.ok(UsuarioDTO.fromModel(usuarioService.actualizar(id, dto.toModel())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        logger.info("DELETE /usuarios/{}", id);
        usuarioService.eliminar(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }
}
