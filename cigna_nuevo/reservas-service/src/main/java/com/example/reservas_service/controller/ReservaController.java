package com.example.reservas_service.controller;

import com.example.reservas_service.dto.ReservaDTO;
import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.service.ReservaService;
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
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Gestion de reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(summary = "Crear reserva")
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO dto,
                                                    @RequestHeader("Authorization") String token) {
        logger.info("POST /reservas - idCliente={}", dto.getIdCliente());
        Reserva nueva = reservaService.guardar(dto.toModel(), token);
        logger.info("Reserva creada id={}", nueva.getId());
        return ResponseEntity.ok(ReservaDTO.fromModel(nueva));
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas")
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        logger.info("GET /reservas - Listando reservas");
        List<ReservaDTO> dtos = reservaService.listar().stream()
                .map(ReservaDTO::fromModel).collect(Collectors.toList());
        logger.info("Total reservas listadas: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID")
    public ResponseEntity<ReservaDTO> obtenerReserva(@PathVariable Long id) {
        logger.info("GET /reservas/{} - Obteniendo reserva", id);
        return ResponseEntity.ok(ReservaDTO.fromModel(reservaService.obtenerPorId(id)));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Listar reservas por cliente")
    public ResponseEntity<List<ReservaDTO>> listarPorCliente(@PathVariable Long idCliente) {
        logger.info("GET /reservas/cliente/{}", idCliente);
        List<ReservaDTO> dtos = reservaService.listarPorCliente(idCliente).stream()
                .map(ReservaDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Verificar si existe una reserva")
    public ResponseEntity<Boolean> existeReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.existePorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva")
    public ResponseEntity<ReservaDTO> actualizarReserva(@PathVariable Long id,
                                                        @Valid @RequestBody ReservaDTO dto,
                                                        @RequestHeader("Authorization") String token) {
        logger.info("PUT /reservas/{} - Actualizando reserva", id);
        Reserva actualizada = reservaService.actualizar(id, dto.toModel(), token);
        logger.info("Reserva actualizada id={}", id);
        return ResponseEntity.ok(ReservaDTO.fromModel(actualizada));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una reserva (solo si esta PENDIENTE)")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        logger.info("PATCH /reservas/{}/cancelar", id);
        reservaService.cancelar(id);
        return ResponseEntity.ok("Reserva cancelada exitosamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva")
    public ResponseEntity<String> eliminarReserva(@PathVariable Long id) {
        logger.info("DELETE /reservas/{} - Eliminando reserva", id);
        reservaService.eliminar(id);
        logger.info("Reserva eliminada id={}", id);
        return ResponseEntity.ok("Reserva Eliminada Exitosamente");
    }
}
