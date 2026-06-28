package com.cigna.tratamientos_service;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cigna.tratamientos_service.model.Tratamiento;
import com.cigna.tratamientos_service.repository.TratamientoRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final TratamientoRepository tratamientoRepository;

    DataLoader(TratamientoRepository tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

@Override
    public void run(String... args) throws Exception {
        // Configuramos Faker para usar español
        Faker faker = new Faker(java.util.Locale.forLanguageTag("es"));
        
        for (int i = 0; i < 5; i++) {
            Tratamiento tratamiento = new Tratamiento();
            
            // Tus opciones fijas ya están en español, se mantiene perfecto
            tratamiento.setNombre(faker.options().option("Antibioticoterapia","Fisioterapia","Rehabilitacion","Control Nutricional","Kinesioterapia"));
            
            // La descripción ahora se generará con estructura y conectores en español
            tratamiento.setDescripcion(faker.lorem().sentence(8));
            
            tratamiento.setDuracionDias(faker.number().numberBetween(7, 180));
            
            // Une las 3 palabras con comas de forma limpia (ej: "palabra1, palabra2, palabra3")
            tratamiento.setMedicamentos(String.join(", ", faker.lorem().words(3)));
            
            tratamiento.setActivo(true);
            
            tratamientoRepository.save(tratamiento);
        }
    }
}