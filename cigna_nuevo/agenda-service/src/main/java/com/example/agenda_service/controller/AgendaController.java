package com.example.agenda_service.controller;

import com.example.agenda_service.dto.AgendaDTO;
import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.service.AgendaService;
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
@RequestMapping("/agendas")
@Tag(name = "Agenda Médica", description = "API para gestion de agendas del sistema medico")
public class AgendaController {

    private static final Logger logger = LoggerFactory.getLogger(AgendaController.class);
    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los agendas", description = "Retorna todos los bloques de agenda disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Agenda.class)))
    })
    public ResponseEntity<List<AgendaDTO>> listar() {
        logger.info("GET /agendas - Solicitud para listar todos los agendas");
        List<AgendaDTO> dtos = agendaService.listar().stream()
                .map(AgendaDTO::fromModel).collect(Collectors.toList());
        logger.debug("Total agendas retornados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener agenda por ID", description = "Busca y retorna un bloque de agenda por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Agenda.class))),
        @ApiResponse(responseCode = "404", description = "Agenda no encontrado", content = @Content)
    })
    public ResponseEntity<AgendaDTO> obtener(
            @Parameter(description = "ID unico del agenda", example = "1")
            @PathVariable Long id) {
        logger.info("GET /agendas/{} - Solicitud para obtener agenda por ID", id);
        return ResponseEntity.ok(AgendaDTO.fromModel(agendaService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de agenda", description = "Verifica si existe un bloque de agenda con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "ID del agenda a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /agendas/{}/exists", id);
        return ResponseEntity.ok(agendaService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear agenda", description = "Registra un nuevo bloque horario en la agenda médica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agenda creado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Agenda.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content)
    })
    public ResponseEntity<AgendaDTO> crear(
            @Parameter(description = "Datos del agenda a crear")
            @Valid @RequestBody AgendaDTO dto) {
        logger.info("POST /agendas - Solicitud para crear agenda");
        Agenda nuevo = agendaService.guardar(dto.toModel());
        logger.debug("Agenda creado exitosamente con ID: {}", nuevo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendaDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar agenda", description = "Actualiza un bloque de agenda existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Agenda.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Agenda no encontrado", content = @Content)
    })
    public ResponseEntity<AgendaDTO> actualizar(
            @Parameter(description = "ID del agenda a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del agenda")
            @Valid @RequestBody AgendaDTO dto) {
        logger.info("PUT /agendas/{} - Solicitud para actualizar agenda", id);
        Agenda actualizado = agendaService.actualizar(id, dto.toModel());
        logger.debug("Agenda ID {} actualizado correctamente", id);
        return ResponseEntity.ok(AgendaDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar agenda", description = "Elimina un bloque de agenda por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Agenda eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Agenda no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del agenda a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /agendas/{} - Solicitud para eliminar agenda", id);
        agendaService.eliminar(id);
        logger.debug("Agenda ID {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }
}
