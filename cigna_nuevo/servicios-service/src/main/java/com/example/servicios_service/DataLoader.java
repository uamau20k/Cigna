package com.example.servicios_service;

import com.example.servicios_service.model.Servicio;
import com.example.servicios_service.repository.ServicioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ServicioRepository servicioRepository;

    DataLoader(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        for (int i = 0; i < 5; i++) {
            Servicio servicio = new Servicio();
            servicio.setNombre(faker.options().option("Consulta General","Cardiologia","Traumatologia","Pediatria","Neurologia"));
            servicio.setDescripcion(faker.lorem().sentence(8));
            servicio.setPrecio((double)(faker.number().numberBetween(15000, 120000)));
            servicio.setDuracionMinutos(faker.number().numberBetween(20, 60));
            servicio.setActivo(true);
            servicioRepository.save(servicio);
        }
    }
}
