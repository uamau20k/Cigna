package com.example.notificaciones_service;

import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.repository.NotificacionRepository;
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
        Random random = new Random();
        String[] mensajes = {
            "Su cita medica ha sido confirmada para el dia de manana.",
            "Su cita medica ha sido cancelada. Por favor reagende.",
            "Recuerde que tiene una cita medica programada para hoy.",
            "Su tratamiento ha sido actualizado por su medico.",
            "Se ha registrado una nueva reserva a su nombre.",
            "Su medico ha enviado un mensaje sobre su tratamiento.",
            "Tiene un pago pendiente por su ultima consulta medica.",
            "Su resultado de examen ya esta disponible en el sistema."
        };
        String[] tipos = {"EMAIL", "SMS", "PUSH"};

        for (int i = 0; i < 5; i++) {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdCliente((long) random.nextInt(5) + 1);
            notificacion.setTipo(tipos[random.nextInt(tipos.length)]);
            notificacion.setMensaje(mensajes[random.nextInt(mensajes.length)]);
            notificacion.setFechaEnvio(new java.util.Date());
            notificacion.setLeido(false);
            notificacionRepository.save(notificacion);
        }
    }
}
