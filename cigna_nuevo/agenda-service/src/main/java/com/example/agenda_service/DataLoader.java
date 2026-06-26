package com.example.agenda_service;

import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.repository.AgendaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final AgendaRepository agendaRepository;

    DataLoader(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Agenda agenda = new Agenda();
            agenda.setIdMedico((long) random.nextInt(5) + 1);
            agenda.setIdSucursal((long) random.nextInt(3) + 1);
            agenda.setFecha(new java.util.Date());
            agenda.setHoraInicio(faker.options().option("08:00","09:00","10:00","11:00","14:00","15:00"));
            agenda.setHoraFin(faker.options().option("08:30","09:30","10:30","11:30","14:30","15:30"));
            agenda.setDisponible(true);
            agendaRepository.save(agenda);
        }
    }
}
