package com.example.sucursales_service.controller;

import com.example.sucursales_service.dto.SucursalDTO;
import com.example.sucursales_service.model.Sucursal;
import com.example.sucursales_service.service.SucursalService;
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
@RequestMapping("/sucursales")
@Tag(name = "Sucursales", description = "API para gestion de sucursals del sistema medico")
public class SucursalController {

    private static final Logger logger = LoggerFactory.getLogger(SucursalController.class);
    private final SucursalService sucursalService;

    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los sucursals", description = "Retorna lista de todas las sucursales médicas registradas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Sucursal.class)))
    })
    public ResponseEntity<List<SucursalDTO>> listar() {
        logger.info("GET /sucursales - Solicitud para listar todos los sucursals");
        List<SucursalDTO> dtos = sucursalService.listar().stream()
                .map(SucursalDTO::fromModel).collect(Collectors.toList());
        logger.debug("Total sucursals retornados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Busca y retorna una sucursal por su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Sucursal.class))),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrado", content = @Content)
    })
    public ResponseEntity<SucursalDTO> obtener(
            @Parameter(description = "ID unico del sucursal", example = "1")
            @PathVariable Long id) {
        logger.info("GET /sucursales/{} - Solicitud para obtener sucursal por ID", id);
        return ResponseEntity.ok(SucursalDTO.fromModel(sucursalService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de sucursal", description = "Verifica si existe una sucursal con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "ID del sucursal a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /sucursales/{}/exists", id);
        return ResponseEntity.ok(sucursalService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Registra una nueva sucursal médica en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Sucursal.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content)
    })
    public ResponseEntity<SucursalDTO> crear(
            @Parameter(description = "Datos del sucursal a crear")
            @Valid @RequestBody SucursalDTO dto) {
        logger.info("POST /sucursales - Solicitud para crear sucursal");
        Sucursal nuevo = sucursalService.guardar(dto.toModel());
        logger.debug("Sucursal creado exitosamente con ID: {}", nuevo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(SucursalDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza los datos de una sucursal existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Sucursal.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrado", content = @Content)
    })
    public ResponseEntity<SucursalDTO> actualizar(
            @Parameter(description = "ID del sucursal a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del sucursal")
            @Valid @RequestBody SucursalDTO dto) {
        logger.info("PUT /sucursales/{} - Solicitud para actualizar sucursal", id);
        Sucursal actualizado = sucursalService.actualizar(id, dto.toModel());
        logger.debug("Sucursal ID {} actualizado correctamente", id);
        return ResponseEntity.ok(SucursalDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sucursal eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del sucursal a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /sucursales/{} - Solicitud para eliminar sucursal", id);
        sucursalService.eliminar(id);
        logger.debug("Sucursal ID {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }
}
