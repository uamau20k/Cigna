package com.example.compras_service;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.compras_service.model.Compra;
import com.example.compras_service.repository.CompraRepository;

import java.util.Date;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {
    private final CompraRepository compraRepository;

    DataLoader(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        String[] estados = {"PENDIENTE", "PAGADO", "CANCELADO"};
        for (int i = 0; i < 5; i++) {
            Compra compra = new Compra();
            compra.setIdPaciente((long) random.nextInt(3) + 1);
            compra.setIdServicio((long) random.nextInt(3) + 1);
            compra.setFechaCompra(new Date());
            compra.setEstado(faker.options().option(estados));
            compra.setDescripcion(faker.lorem().sentence());
            compraRepository.save(compra);
        }
    }
}
