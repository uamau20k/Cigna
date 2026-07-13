package com.example.resena_service.controller;

import com.example.resena_service.assemblers.ResenaModelAssembler;
import com.example.resena_service.model.Resena;
import com.example.resena_service.service.ResenaService;
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
@RequestMapping("/resenas/v2")
@Tag(name = "Reseñas V2", description = "Gestion de reseñas con HATEOAS")
public class ResenaControllerV2 {

    private final ResenaService resenaService;
    private final ResenaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ResenaControllerV2.class);

    public ResenaControllerV2(ResenaService resenaService, ResenaModelAssembler assembler) {
        this.resenaService = resenaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar reseñas con HATEOAS")
    public CollectionModel<EntityModel<Resena>> listarResenas() {
        logger.info("V2 GET /resenas - Listando reseñas");
        List<EntityModel<Resena>> resenas = resenaService.listarTodas().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(resenas, linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID con HATEOAS")
    public EntityModel<Resena> obtenerResena(@PathVariable Long id) {
        logger.info("V2 GET /resenas/{} - Obteniendo reseña", id);
        return assembler.toModel(resenaService.obtenerPorId(id));
    }
}
