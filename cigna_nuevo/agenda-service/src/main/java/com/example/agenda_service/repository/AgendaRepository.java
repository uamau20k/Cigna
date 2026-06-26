package com.example.agenda_service.repository;

import com.example.agenda_service.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
