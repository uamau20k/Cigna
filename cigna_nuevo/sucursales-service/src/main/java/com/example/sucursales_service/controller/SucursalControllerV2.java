package com.example.sucursales_service.controller;

import com.example.sucursales_service.assemblers.SucursalModelAssembler;
import com.example.sucursales_service.model.Sucursal;
import com.example.sucursales_service.service.SucursalService;
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
@RequestMapping("/sucursales/v2")
@Tag(name = "Sucursals V2", description = "Gestion de sucursals con HATEOAS")
public class SucursalControllerV2 {

    private final SucursalService sucursalService;
    private final SucursalModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(SucursalControllerV2.class);

    public SucursalControllerV2(SucursalService sucursalService, SucursalModelAssembler assembler) {
        this.sucursalService = sucursalService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar sucursals con HATEOAS")
    public CollectionModel<EntityModel<Sucursal>> listarSucursals() {
        logger.info("V2 GET /sucursales");
        List<EntityModel<Sucursal>> lista = sucursalService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista, linkTo(methodOn(SucursalControllerV2.class).listarSucursals()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID con HATEOAS")
    public EntityModel<Sucursal> obtenerSucursal(@PathVariable Long id) {
        logger.info("V2 GET /sucursales/{}", id);
        return assembler.toModel(sucursalService.obtenerPorId(id));
    }
}
