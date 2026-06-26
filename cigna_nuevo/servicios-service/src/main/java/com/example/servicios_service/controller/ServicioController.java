package com.example.servicios_service.controller;

import com.example.servicios_service.dto.ServicioDTO;
import com.example.servicios_service.model.Servicio;
import com.example.servicios_service.service.ServicioService;
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
@RequestMapping("/servicios")
@Tag(name = "Servicios Médicos", description = "API para gestion de servicios del sistema medico")
public class ServicioController {

    private static final Logger logger = LoggerFactory.getLogger(ServicioController.class);
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los servicios", description = "Retorna el catálogo completo de servicios médicos disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Servicio.class)))
    })
    public ResponseEntity<List<ServicioDTO>> listar() {
        logger.info("GET /servicios - Solicitud para listar todos los servicios");
        List<ServicioDTO> dtos = servicioService.listar().stream()
                .map(ServicioDTO::fromModel).collect(Collectors.toList());
        logger.debug("Total servicios retornados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID", description = "Busca y retorna un servicio médico por su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio encontrado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Servicio.class))),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<ServicioDTO> obtener(
            @Parameter(description = "ID unico del servicio", example = "1")
            @PathVariable Long id) {
        logger.info("GET /servicios/{} - Solicitud para obtener servicio por ID", id);
        return ResponseEntity.ok(ServicioDTO.fromModel(servicioService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de servicio", description = "Verifica si existe un servicio médico con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existe(
            @Parameter(description = "ID del servicio a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /servicios/{}/exists", id);
        return ResponseEntity.ok(servicioService.existePorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear servicio", description = "Registra un nuevo servicio médico en el catálogo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Servicio.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content)
    })
    public ResponseEntity<ServicioDTO> crear(
            @Parameter(description = "Datos del servicio a crear")
            @Valid @RequestBody ServicioDTO dto) {
        logger.info("POST /servicios - Solicitud para crear servicio");
        Servicio nuevo = servicioService.guardar(dto.toModel());
        logger.debug("Servicio creado exitosamente con ID: {}", nuevo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ServicioDTO.fromModel(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio", description = "Actualiza los datos de un servicio médico existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Servicio.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<ServicioDTO> actualizar(
            @Parameter(description = "ID del servicio a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del servicio")
            @Valid @RequestBody ServicioDTO dto) {
        logger.info("PUT /servicios/{} - Solicitud para actualizar servicio", id);
        Servicio actualizado = servicioService.actualizar(id, dto.toModel());
        logger.debug("Servicio ID {} actualizado correctamente", id);
        return ResponseEntity.ok(ServicioDTO.fromModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio", description = "Elimina un servicio médico del catálogo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del servicio a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /servicios/{} - Solicitud para eliminar servicio", id);
        servicioService.eliminar(id);
        logger.debug("Servicio ID {} eliminado exitosamente", id);
        return ResponseEntity.noContent().build();
    }
}
