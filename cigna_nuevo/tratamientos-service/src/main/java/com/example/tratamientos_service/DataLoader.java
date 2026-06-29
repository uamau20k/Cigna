package com.example.tratamientos_service;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.tratamientos_service.model.Tratamiento;
import com.example.tratamientos_service.repository.TratamientoRepository;

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
            tratamiento.setDescripcion(faker.options().option(
        "Tratamiento con antibióticos por 10 días",
                "Sesiones de rehabilitación física tres veces por semana",
                "Control nutricional con dieta personalizada",
                "Terapia de kinesioterapia para recuperación muscular",
                "Fisioterapia post operatoria de rodilla"
                ));
            
            tratamiento.setDuracionDias(faker.number().numberBetween(7, 180));
            
            tratamiento.setMedicamentos(faker.options().option(
            "Amoxicilina, Ibuprofeno, Paracetamol",
            "Omeprazol, Metformina, Atorvastatina",
            "Losartán, Enalapril, Aspirina",
            "Clonazepam, Sertralina, Melatonina",
            "Prednisona, Salbutamol, Bromhexina"
        ));
            
            tratamiento.setActivo(true);
            
            tratamientoRepository.save(tratamiento);
        }
    }
}