package com.example.servicios_service.controller;

import com.example.servicios_service.assemblers.ServicioModelAssembler;
import com.example.servicios_service.model.Servicio;
import com.example.servicios_service.service.ServicioService;
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
@RequestMapping("/servicios/v2")
@Tag(name = "Servicios V2", description = "Gestion de servicios con HATEOAS")
public class ServicioControllerV2 {

    private final ServicioService servicioService;
    private final ServicioModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ServicioControllerV2.class);

    public ServicioControllerV2(ServicioService servicioService, ServicioModelAssembler assembler) {
        this.servicioService = servicioService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar servicios con HATEOAS")
    public CollectionModel<EntityModel<Servicio>> listarServicios() {
        logger.info("V2 GET /servicios");
        List<EntityModel<Servicio>> lista = servicioService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista, linkTo(methodOn(ServicioControllerV2.class).listarServicios()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID con HATEOAS")
    public EntityModel<Servicio> obtenerServicio(@PathVariable Long id) {
        logger.info("V2 GET /servicios/{}", id);
        return assembler.toModel(servicioService.obtenerPorId(id));
    }
}
