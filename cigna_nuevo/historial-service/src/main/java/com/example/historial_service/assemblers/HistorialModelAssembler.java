package com.example.historial_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.historial_service.controller.HistorialControllerV2;
import com.example.historial_service.model.HistorialClinico;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class HistorialModelAssembler implements RepresentationModelAssembler<HistorialClinico, EntityModel<HistorialClinico>> {

    @Override
    public EntityModel<HistorialClinico> toModel(HistorialClinico historial) {
        return EntityModel.of(historial,
                linkTo(methodOn(HistorialControllerV2.class).obtenerHistorial(historial.getId())).withSelfRel(),
                linkTo(methodOn(HistorialControllerV2.class).listarHistoriales()).withRel("historiales"));
    }
}
