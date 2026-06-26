package com.example.historial_service.controller;

import com.example.historial_service.assemblers.HistorialModelAssembler;
import com.example.historial_service.model.HistorialClinico;
import com.example.historial_service.service.HistorialClinicoService;
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
@RequestMapping("historial/v2")
@Tag(name = "Historial Clinico V2", description = "Gestion de historial clinico con HATEOAS")
public class HistorialControllerV2 {

    private final HistorialClinicoService historialService;
    private final HistorialModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(HistorialControllerV2.class);

    public HistorialControllerV2(HistorialClinicoService historialService, HistorialModelAssembler assembler) {
        this.historialService = historialService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar historiales con HATEOAS")
    public CollectionModel<EntityModel<HistorialClinico>> listarHistoriales() {
        logger.info("V2 GET /historial - Listando historiales");
        List<EntityModel<HistorialClinico>> historiales = historialService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(historiales, linkTo(methodOn(HistorialControllerV2.class).listarHistoriales()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial por ID con HATEOAS")
    public EntityModel<HistorialClinico> obtenerHistorial(@PathVariable Long id) {
        logger.info("V2 GET /historial/{}", id);
        return assembler.toModel(historialService.obtenerPorId(id));
    }
}
