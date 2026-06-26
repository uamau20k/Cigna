package com.example.sucursales_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.sucursales_service.controller.SucursalControllerV2;
import com.example.sucursales_service.model.Sucursal;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<Sucursal, EntityModel<Sucursal>> {

    @Override
    public EntityModel<Sucursal> toModel(Sucursal sucursal) {
        return EntityModel.of(sucursal,
                linkTo(methodOn(SucursalControllerV2.class).obtenerSucursal(sucursal.getId())).withSelfRel(),
                linkTo(methodOn(SucursalControllerV2.class).listarSucursals()).withRel("sucursales"));
    }
}
