package com.example.reservas_service.controller;

import com.example.reservas_service.assemblers.ReservaModelAssembler;
import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("reservas/v2")
@Tag(name = "Reservas V2", description = "Gestion de reservas con HATEOAS")
public class ReservaControllerV2 {

    private final ReservaService reservaService;
    private final ReservaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ReservaControllerV2.class);

    public ReservaControllerV2(ReservaService reservaService, ReservaModelAssembler assembler) {
        this.reservaService = reservaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar reservas con HATEOAS")
    public CollectionModel<EntityModel<Reserva>> listarReservas() {
        logger.info("V2 GET /reservas - Listando reservas");
        List<EntityModel<Reserva>> reservas = reservaService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(reservas, linkTo(methodOn(ReservaControllerV2.class).listarReservas()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID con HATEOAS")
    public EntityModel<Reserva> obtenerReserva(@PathVariable Long id) {
        logger.info("V2 GET /reservas/{} - Obteniendo reserva", id);
        return assembler.toModel(reservaService.obtenerPorId(id));
    }
}
