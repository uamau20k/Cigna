package com.cigna.tratamientos_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.cigna.tratamientos_service.controller.TratamientoControllerV2;
import com.cigna.tratamientos_service.model.Tratamiento;

@Component
public class TratamientoModelAssembler implements RepresentationModelAssembler<Tratamiento, EntityModel<Tratamiento>> {

    @Override
    public EntityModel<Tratamiento> toModel(Tratamiento tratamiento) {
        return EntityModel.of(tratamiento,
                linkTo(methodOn(TratamientoControllerV2.class).obtenerTratamiento(tratamiento.getId())).withSelfRel(),
                linkTo(methodOn(TratamientoControllerV2.class).listarTratamientos()).withRel("tratamientos"));
    }
}
