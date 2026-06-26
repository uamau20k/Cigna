package com.example.notificaciones_service.controller;

import com.example.notificaciones_service.dto.NotificacionDTO;
import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificaciones")
@Tag(name = "Notificaciones", description = "API para gestion de notificacions del sistema medico")
public class NotificacionController {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionController.class);
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los notificacions", description = "Retorna todas las notificaciones registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class)))
    })
    public ResponseEntity<List<NotificacionDTO>> listar() {
        logger.info("GET /notificaciones - Solicitud para listar todos los notificacions");
        List<NotificacionDTO> dtos = notificacionService.listar().stream()
                .map(NotificacionDTO::fromModel).collect(Collectors.toList());
        logger.debug("Total notificacions retornados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificacion por ID", description = "Busca y retorna una notificación por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificacion encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "404", description = "Notificacion no encontrado", content = @Content)
    })
    public ResponseEntity<NotificacionDTO> obtener(
            @Parameter(description = "ID unico del notificacion", example = "1")
            @PathVariable Long id) {
        logger.info("GET /notificaciones/{} - Solicitud para obtener notificacion por ID", id);
        return ResponseEntity.ok(NotificacionDTO.fromModel(notificacionService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de notificacion", description = "Verifica si existe una notificación con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "ID del notificacion a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /notificaciones/{}/exists", id);
        return ResponseEntity.ok(notificacionService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear notificacion", description = "Registra una nueva notificación para un paciente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificacion creado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content)
    })
    public ResponseEntity<NotificacionDTO> crear(
            @Parameter(description = "Datos del notificacion a crear")
            @Valid @RequestBody NotificacionDTO dto) {
        logger.info("POST /notificaciones - Solicitud para crear notificacion");
        Notificacion nuevo = notificacionService.guardar(dto.toModel());
        logger.debug("Notificacion creado exitosamente con ID: {}", nuevo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(NotificacionDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar notificacion", description = "Actualiza los datos de una notificación existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificacion actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Notificacion no encontrado", content = @Content)
    })
    public ResponseEntity<NotificacionDTO> actualizar(
            @Parameter(description = "ID del notificacion a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del notificacion")
            @Valid @RequestBody NotificacionDTO dto) {
        logger.info("PUT /notificaciones/{} - Solicitud para actualizar notificacion", id);
        Notificacion actualizado = notificacionService.actualizar(id, dto.toModel());
        logger.debug("Notificacion ID {} actualizado correctamente", id);
        return ResponseEntity.ok(NotificacionDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificacion", description = "Elimina una notificación por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificacion eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificacion no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del notificacion a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /notificaciones/{} - Solicitud para eliminar notificacion", id);
        notificacionService.eliminar(id);
        logger.debug("Notificacion ID {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }
}
