package com.example.agenda_service.controller;

import com.example.agenda_service.assemblers.AgendaModelAssembler;
import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.service.AgendaService;
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
@RequestMapping("/agendas/v2")
@Tag(name = "Agendas V2", description = "Gestion de agendas con HATEOAS")
public class AgendaControllerV2 {

    private final AgendaService agendaService;
    private final AgendaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(AgendaControllerV2.class);

    public AgendaControllerV2(AgendaService agendaService, AgendaModelAssembler assembler) {
        this.agendaService = agendaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar agendas con HATEOAS")
    public CollectionModel<EntityModel<Agenda>> listarAgendas() {
        logger.info("V2 GET /agendas");
        List<EntityModel<Agenda>> lista = agendaService.listar().stream()
                .map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(lista, linkTo(methodOn(AgendaControllerV2.class).listarAgendas()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener agenda por ID con HATEOAS")
    public EntityModel<Agenda> obtenerAgenda(@PathVariable Long id) {
        logger.info("V2 GET /agendas/{}", id);
        return assembler.toModel(agendaService.obtenerPorId(id));
    }
}
