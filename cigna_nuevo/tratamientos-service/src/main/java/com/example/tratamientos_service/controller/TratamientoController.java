package com.example.tratamientos_service.controller;

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

import com.example.tratamientos_service.dto.TratamientoDTO;
import com.example.tratamientos_service.model.Tratamiento;
import com.example.tratamientos_service.service.TratamientoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tratamientos")
@Tag(name = "Tratamientos Médicos", description = "API para gestion de tratamientos del sistema medico")
public class TratamientoController {

    private static final Logger logger = LoggerFactory.getLogger(TratamientoController.class);
    private final TratamientoService tratamientoService;

    public TratamientoController(TratamientoService tratamientoService) {
        this.tratamientoService = tratamientoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los tratamientos", description = "Retorna el catálogo completo de tratamientos médicos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tratamiento.class)))
    })
    public ResponseEntity<List<TratamientoDTO>> listar() {
        logger.info("GET /tratamientos - Solicitud para listar todos los tratamientos");
        List<TratamientoDTO> dtos = tratamientoService.listar().stream()
                .map(TratamientoDTO::fromModel).collect(Collectors.toList());
        logger.debug("Total tratamientos retornados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tratamiento por ID", description = "Busca y retorna un tratamiento médico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tratamiento encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tratamiento.class))),
        @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
    })
    public ResponseEntity<TratamientoDTO> obtener(
            @Parameter(description = "ID unico del tratamiento", example = "1")
            @PathVariable Long id) {
        logger.info("GET /tratamientos/{} - Solicitud para obtener tratamiento por ID", id);
        return ResponseEntity.ok(TratamientoDTO.fromModel(tratamientoService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de tratamiento", description = "Verifica si existe un tratamiento con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "ID del tratamiento a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /tratamientos/{}/exists", id);
        return ResponseEntity.ok(tratamientoService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear tratamiento", description = "Registra un nuevo tratamiento médico en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tratamiento creado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tratamiento.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content)
    })
    public ResponseEntity<TratamientoDTO> crear(
            @Parameter(description = "Datos del tratamiento a crear")
            @Valid @RequestBody TratamientoDTO dto) {
        logger.info("POST /tratamientos - Solicitud para crear tratamiento");
        Tratamiento nuevo = tratamientoService.guardar(dto.toModel());
        logger.debug("Tratamiento creado exitosamente con ID: {}", nuevo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TratamientoDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tratamiento", description = "Actualiza los datos de un tratamiento existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tratamiento actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tratamiento.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
    })
    public ResponseEntity<TratamientoDTO> actualizar(
            @Parameter(description = "ID del tratamiento a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del tratamiento")
            @Valid @RequestBody TratamientoDTO dto) {
        logger.info("PUT /tratamientos/{} - Solicitud para actualizar tratamiento", id);
        Tratamiento actualizado = tratamientoService.actualizar(id, dto.toModel());
        logger.debug("Tratamiento ID {} actualizado correctamente", id);
        return ResponseEntity.ok(TratamientoDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tratamiento", description = "Elimina un tratamiento médico del catálogo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tratamiento eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tratamiento no encontrado", content = @Content)
})
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del tratamiento a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /tratamientos/{} - Solicitud para eliminar tratamiento", id);
        tratamientoService.eliminar(id);
        logger.debug("Tratamiento ID {} eliminado exitosamente", id); 
        return ResponseEntity.ok("Tratamiento eliminado exitosamente"); // se implemento un mensaje de confirmacion para la eliminacion
    }
}
