package com.example.resena_service.controller;

import com.example.resena_service.dto.ResenaDTO;
import com.example.resena_service.model.Resena;
import com.example.resena_service.service.ResenaService;
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
@RequestMapping("/resenas")
@Tag(name = "Reseñas", description = "API para gestion de reseñas de servicios medicos")
public class ResenaController {

    private static final Logger logger = LoggerFactory.getLogger(ResenaController.class);
    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }


    @PostMapping
    @Operation(summary = "Crear reseña", description = "Registra una nueva reseña, validando usuario, servicio y compra.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "El usuario no ha comprado el servicio", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuario o servicio no encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Ya existe una reseña de este usuario para este servicio", content = @Content)
    })
    public ResponseEntity<ResenaDTO> crearResena(
            @Valid @RequestBody ResenaDTO resenaDto,
            @RequestHeader("Authorization") String token) {
        try {
            logger.info("POST /resenas - Creando reseña: idUsuario={}, idServicio={}",
                    resenaDto.getIdUsuario(), resenaDto.getIdServicio());
            Resena nuevaResena = resenaService.crearResena(resenaDto.toModel(), token);
            logger.info("Reseña creada exitosamente id={}", nuevaResena.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(ResenaDTO.fromModel(nuevaResena));
        } catch (Exception e) {
            logger.error("Error al crear reseña: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Listar todas las reseñas", description = "Retorna todas las reseñas registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Resena.class)))
    })
    public ResponseEntity<List<ResenaDTO>> listarTodas() {
        logger.info("GET /resenas - Listando todas las reseñas");
        List<ResenaDTO> dtos = resenaService.listarTodas().stream()
                .map(ResenaDTO::fromModel)
                .collect(Collectors.toList());
        logger.info("Total reseñas encontradas: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/servicio/{idServicio}")
    @Operation(summary = "Listar reseñas de un servicio", description = "Retorna todas las reseñas asociadas a un servicio específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<List<ResenaDTO>> obtenerPorServicio(
            @Parameter(description = "ID del servicio", example = "1")
            @PathVariable Long idServicio) {
        logger.info("GET /resenas/servicio/{} - Listando reseñas", idServicio);
        List<ResenaDTO> dtos = resenaService.obtenerResenasPorServicio(idServicio).stream()
                .map(ResenaDTO::fromModel)
                .collect(Collectors.toList());
        logger.info("Total reseñas encontradas: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar reseñas de un usuario", description = "Retorna todas las reseñas realizadas por un usuario específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<List<ResenaDTO>> obtenerPorUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long idUsuario) {
        logger.info("GET /resenas/usuario/{} - Listando reseñas", idUsuario);
        List<ResenaDTO> dtos = resenaService.obtenerResenasPorUsuario(idUsuario).stream()
                .map(ResenaDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/servicio/{idServicio}/promedio")
    @Operation(summary = "Promedio de calificación", description = "Calcula el promedio de calificación de un servicio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promedio calculado exitosamente")
    })
    public ResponseEntity<Double> obtenerPromedio(
            @Parameter(description = "ID del servicio", example = "1")
            @PathVariable Long idServicio) {
        logger.info("GET /resenas/servicio/{}/promedio", idServicio);
        Double promedio = resenaService.obtenerPromedioCalificacion(idServicio);
        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID", description = "Busca y retorna una reseña por su ID único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<ResenaDTO> obtenerResena(
            @Parameter(description = "ID de la reseña", example = "1")
            @PathVariable Long id) {
        logger.info("GET /resenas/{} - Obteniendo reseña", id);
        return ResponseEntity.ok(ResenaDTO.fromModel(resenaService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar existencia de reseña", description = "Verifica si existe una reseña con el ID indicado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de la verificacion")
    })
    public ResponseEntity<Boolean> existeResena(
            @Parameter(description = "ID de la reseña a verificar", example = "1")
            @PathVariable Long id) {
        logger.info("GET /resenas/{}/exists", id);
        return ResponseEntity.ok(resenaService.existePorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña", description = "Actualiza la calificación y el comentario de una reseña existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<ResenaDTO> actualizarResena(
            @Parameter(description = "ID de la reseña a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la reseña")
            @Valid @RequestBody ResenaDTO dto) {
        logger.info("PUT /resenas/{} - Actualizando reseña", id);
        Resena actualizada = resenaService.actualizar(id, dto.toModel());
        logger.info("Reseña actualizada id={}", id);
        return ResponseEntity.ok(ResenaDTO.fromModel(actualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    public ResponseEntity<String> eliminarResena(
            @Parameter(description = "ID de la reseña a eliminar", example = "1")
            @PathVariable Long id) {
        logger.info("DELETE /resenas/{} - Eliminando reseña", id);
        resenaService.eliminar(id);
        logger.info("Reseña eliminada id={}", id);
        return ResponseEntity.ok("Reseña eliminada exitosamente");
    }
}