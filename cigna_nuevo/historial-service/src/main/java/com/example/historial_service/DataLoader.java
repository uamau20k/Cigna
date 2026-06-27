package com.example.historial_service;

import com.example.historial_service.model.HistorialClinico;
import com.example.historial_service.repository.HistorialClinicoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final HistorialClinicoRepository historialRepository;

    DataLoader(HistorialClinicoRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            HistorialClinico h = new HistorialClinico();
            h.setIdUsuario((long) random.nextInt(2) + 1);
            h.setFecha(new Date());
            h.setDiagnostico(faker.options().option(
            "Hipertension arterial", "Diabetes tipo 2", "Lumbalgia cronica",
            "Arritmia cardiaca", "Gastritis aguda", "Ansiedad generalizada"
            ));
            h.setTratamiento(faker.lorem().sentence(6));
            historialRepository.save(h);
        }
    }
}
