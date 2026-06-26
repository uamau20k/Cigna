package com.example.notificaciones_service;

import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.repository.NotificacionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final NotificacionRepository notificacionRepository;

    DataLoader(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdCliente((long) random.nextInt(5) + 1);
            notificacion.setTipo(faker.options().option("EMAIL","SMS","PUSH"));
            notificacion.setMensaje(faker.lorem().sentence(10));
            notificacion.setFechaEnvio(new java.util.Date());
            notificacion.setLeido(false);
            notificacionRepository.save(notificacion);
        }
    }
}
