package com.example.historial_service.controller;

import com.example.historial_service.dto.HistorialClinicoDTO;
import com.example.historial_service.model.HistorialClinico;
import com.example.historial_service.service.HistorialClinicoService;
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
@RequestMapping("/historial")
@Tag(name = "Historial Clinico", description = "Gestion de historial clinico")
public class HistorialController {

    private final HistorialClinicoService historialService;
    private static final Logger logger = LoggerFactory.getLogger(HistorialController.class);

    public HistorialController(HistorialClinicoService historialService) {
        this.historialService = historialService;
    }

    @PostMapping
    @Operation(summary = "Crear historial clinico")
    public ResponseEntity<HistorialClinicoDTO> crear(
            @Valid @RequestBody HistorialClinicoDTO dto,
            @RequestHeader("Authorization") String token) {
        logger.info("POST /historial - idUsuario={}", dto.getIdUsuario());
        HistorialClinico nuevo = historialService.guardar(dto.toModel(), token);
        return ResponseEntity.ok(HistorialClinicoDTO.fromModel(nuevo));
}

    @GetMapping
    @Operation(summary = "Listar todos los historiales")
    public ResponseEntity<List<HistorialClinicoDTO>> listar() {
        logger.info("GET /historial - Listando historiales");
        List<HistorialClinicoDTO> dtos = historialService.listar().stream()
                .map(HistorialClinicoDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial por ID")
    public ResponseEntity<HistorialClinicoDTO> obtener(@PathVariable Long id) {
        logger.info("GET /historial/{}", id);
        return ResponseEntity.ok(HistorialClinicoDTO.fromModel(historialService.obtenerPorId(id)));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar historial por usuario")
    public ResponseEntity<List<HistorialClinicoDTO>> listarPorUsuario(@PathVariable Long idUsuario) {
        List<HistorialClinicoDTO> dtos = historialService.listarPorUsuario(idUsuario).stream()
                .map(HistorialClinicoDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar historial clinico")
    public ResponseEntity<HistorialClinicoDTO> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody HistorialClinicoDTO dto,
        @RequestHeader("Authorization") String token) {
    logger.info("PUT /historial/{}", id);
    HistorialClinico actualizado = historialService.actualizar(id, dto.toModel(), token);
    return ResponseEntity.ok(HistorialClinicoDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar historial clinico")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        logger.info("DELETE /historial/{}", id);
        historialService.eliminar(id);
        return ResponseEntity.ok("Historial Eliminado Exitosamente");
    }
    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar si existe un historial")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        return ResponseEntity.ok(historialService.existePorId(id));
    }
}
