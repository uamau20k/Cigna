package com.example.notificaciones_service.controller;

import com.example.notificaciones_service.assemblers.NotificacionModelAssembler;
import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.service.NotificacionService;
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
@RequestMapping("/notificaciones/v2")
@Tag(name = "Notificacions V2", description = "Gestion de notificacions con HATEOAS")
public class NotificacionControllerV2 {

    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(NotificacionControllerV2.class);

    public NotificacionControllerV2(NotificacionService notificacionService, NotificacionModelAssembler assembler) {
        this.notificacionService = notificacionService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar notificacions con HATEOAS")
    public CollectionModel<EntityModel<Notificacion>> listarNotificacions() {
        logger.info("V2 GET /notificaciones");
        List<EntityModel<Notificacion>> lista = notificacionService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista, linkTo(methodOn(NotificacionControllerV2.class).listarNotificacions()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificacion por ID con HATEOAS")
    public EntityModel<Notificacion> obtenerNotificacion(@PathVariable Long id) {
        logger.info("V2 GET /notificaciones/{}", id);
        return assembler.toModel(notificacionService.obtenerPorId(id));
    }
}
