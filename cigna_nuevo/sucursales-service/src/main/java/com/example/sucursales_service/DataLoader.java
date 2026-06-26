package com.example.sucursales_service;

import com.example.sucursales_service.model.Sucursal;
import com.example.sucursales_service.repository.SucursalRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final SucursalRepository sucursalRepository;

    DataLoader(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i < 5; i++) {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre(faker.options().option("Clinica Central","Clinica Norte","Centro Medico Sur","Policlinico Oriente"));
            sucursal.setDireccion(faker.address().streetAddress());
            sucursal.setTelefono(faker.phoneNumber().phoneNumber());
            sucursal.setCiudad(faker.options().option("Santiago","Valparaiso","Concepcion","La Serena"));
            sucursal.setActiva(true);
            sucursalRepository.save(sucursal);
        }
    }
}
