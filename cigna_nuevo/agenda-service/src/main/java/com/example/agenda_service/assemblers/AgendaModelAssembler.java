package com.example.agenda_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.agenda_service.controller.AgendaControllerV2;
import com.example.agenda_service.model.Agenda;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AgendaModelAssembler implements RepresentationModelAssembler<Agenda, EntityModel<Agenda>> {

    @Override
    public EntityModel<Agenda> toModel(Agenda agenda) {
        return EntityModel.of(agenda,
                linkTo(methodOn(AgendaControllerV2.class).obtenerAgenda(agenda.getId())).withSelfRel(),
                linkTo(methodOn(AgendaControllerV2.class).listarAgendas()).withRel("agendas"));
    }
}
