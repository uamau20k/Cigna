package com.example.resena_service;

import com.example.resena_service.model.Resena;
import com.example.resena_service.repository.ResenaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String[] COMENTARIOS = {
            "Excelente atención, muy profesional y puntual.",
            "Buen servicio, aunque esperé un poco más de lo indicado.",
            "El tratamiento fue muy cómodo y efectivo.",
            "Cumple su función básica, calidad acorde al precio pagado.",
            "Excelente equipo médico, resultados notorios de inmediato.",
            "Buena atención, el especialista explicó todo con claridad.",
            "El servicio vino muy completo y bien explicado.",
            "La sala estaba muy fría durante la consulta, pediré otra hora.",
            "Servicio de muy buena calidad, el trato fue excelente.",
            "El chequeo fue preciso y con buen material clínico."
    };

    private final ResenaRepository resenaRepository;

    DataLoader(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        // idServicio 10+ para no chocar con las combinaciones idUsuario/idServicio ya sembradas por Liquibase (servicios 1-9)
        for (int i = 0; i < 5; i++) {
            Resena resena = new Resena();
            resena.setIdUsuario((long) faker.number().numberBetween(1, 5));
            resena.setIdServicio(10L + i);
            resena.setCalificacion(faker.number().numberBetween(1, 6));
            resena.setComentario(COMENTARIOS[faker.number().numberBetween(0, COMENTARIOS.length)]);
            resenaRepository.save(resena);
        }
    }
}
