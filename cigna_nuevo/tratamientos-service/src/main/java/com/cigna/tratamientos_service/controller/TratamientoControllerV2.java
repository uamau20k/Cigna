package com.cigna.tratamientos_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import com.cigna.tratamientos_service.assemblers.TratamientoModelAssembler;
import com.cigna.tratamientos_service.model.Tratamiento;
import com.cigna.tratamientos_service.service.TratamientoService;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/tratamientos/v2")
@Tag(name = "Tratamientos V2", description = "Gestion de tratamientos con HATEOAS")
public class TratamientoControllerV2 {

    private final TratamientoService tratamientoService;
    private final TratamientoModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(TratamientoControllerV2.class);

    public TratamientoControllerV2(TratamientoService tratamientoService, TratamientoModelAssembler assembler) {
        this.tratamientoService = tratamientoService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar tratamientos con HATEOAS")
    public CollectionModel<EntityModel<Tratamiento>> listarTratamientos() {
        logger.info("V2 GET /tratamientos");
        List<EntityModel<Tratamiento>> lista = tratamientoService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista, linkTo(methodOn(TratamientoControllerV2.class).listarTratamientos()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tratamiento por ID con HATEOAS")
    public EntityModel<Tratamiento> obtenerTratamiento(@PathVariable Long id) {
        logger.info("V2 GET /tratamientos/{}", id);
        return assembler.toModel(tratamientoService.obtenerPorId(id));
    }
}
