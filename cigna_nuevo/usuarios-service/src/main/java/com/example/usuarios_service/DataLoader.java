package com.example.usuarios_service;

import com.example.usuarios_service.model.Usuario;
import com.example.usuarios_service.repository.UsuarioRepository;
import net.datafaker.Faker;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    DataLoader(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

   @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(java.util.Locale.forLanguageTag("es"));
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            if (usuarioRepository.count() >= 10) break;
            Usuario u = new Usuario();
            u.setNombre(faker.name().firstName());
            u.setApellido(faker.name().lastName());
            u.setCorreo(faker.internet().emailAddress());
            String numero = String.format("+56 9 %04d %04d", random.nextInt(9000) + 1000, random.nextInt(9000) + 1000);
            u.setTelefono(numero);
            u.setActivo(true);
            usuarioRepository.save(u);
        }
    }
}
