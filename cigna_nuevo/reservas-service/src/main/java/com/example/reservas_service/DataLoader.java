package com.example.reservas_service;

import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.repository.ReservaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final ReservaRepository reservaRepository;

    DataLoader(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Reserva reserva = new Reserva();
            reserva.setIdUsuario((long) random.nextInt(2) + 1);
            reserva.setIdServicio((long) random.nextInt(2) + 1);
            reserva.setIdTratamiento((long) random.nextInt(2) + 1);
            reserva.setFechaReserva(new Date());
            reserva.setDescripcion(faker.commerce().productName());
            reserva.setEstado(faker.options().option("PENDIENTE", "CONFIRMADA", "CANCELADA"));
            reservaRepository.save(reserva);
        }
    }
}
