package com.example.servicios_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.servicios_service.controller.ServicioControllerV2;
import com.example.servicios_service.model.Servicio;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ServicioModelAssembler implements RepresentationModelAssembler<Servicio, EntityModel<Servicio>> {

    @Override
    public EntityModel<Servicio> toModel(Servicio servicio) {
        return EntityModel.of(servicio,
                linkTo(methodOn(ServicioControllerV2.class).obtenerServicio(servicio.getId())).withSelfRel(),
                linkTo(methodOn(ServicioControllerV2.class).listarServicios()).withRel("servicios"));
    }
}
