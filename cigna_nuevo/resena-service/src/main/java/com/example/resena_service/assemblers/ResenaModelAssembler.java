package com.example.resena_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.resena_service.controller.ResenaControllerV2;
import com.example.resena_service.model.Resena;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<Resena, EntityModel<Resena>> {

    @Override
    public EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(resena,
                linkTo(methodOn(ResenaControllerV2.class).obtenerResena(resena.getId())).withSelfRel(),
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("resenas"));
    }
}
