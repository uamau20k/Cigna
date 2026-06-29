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
        String[][] servicios = {
            {"Consulta General", "Consulta medica general con diagnostico y revision de sintomas."},
            {"Cardiologia", "Evaluacion y tratamiento de enfermedades del corazon y sistema cardiovascular."},
            {"Traumatologia", "Atencion de lesiones oseas, musculares y articulares."},
            {"Pediatria", "Consulta medica especializada para ninos y adolescentes."},
            {"Neurologia", "Diagnostico y tratamiento de enfermedades del sistema nervioso."}
        };

        for (int i = 0; i < servicios.length; i++) {
            Servicio servicio = new Servicio();
            servicio.setNombre(servicios[i][0]);
            servicio.setDescripcion(servicios[i][1]);
            servicio.setPrecio((double)(faker.number().numberBetween(15000, 120000)));
            servicio.setDuracionMinutos(faker.number().numberBetween(20, 60));
            servicio.setActivo(true);
            servicioRepository.save(servicio);
        }
    }
}
