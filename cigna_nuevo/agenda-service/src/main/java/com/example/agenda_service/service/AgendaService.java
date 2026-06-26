package com.example.agenda_service.service;

import com.example.agenda_service.exception.ResourceNotFoundException;
import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.repository.AgendaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public List<Agenda> listar() {
        logger.info("Listando todos los agendas");
        List<Agenda> lista = agendaRepository.findAll();
        logger.info("Total agendas: {}", lista.size());
        return lista;
    }

    public Agenda obtenerPorId(Long id) {
        logger.info("Buscando agenda id={}", id);
        return agendaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Agenda no encontrado id={}", id);
                    return new ResourceNotFoundException("Agenda no existe con id: " + id);
                });
    }

    public Agenda guardar(Agenda agenda) {
        logger.info("Guardando agenda");
        Agenda guardado = agendaRepository.save(agenda);
        logger.info("Agenda guardado id={}", guardado.getId());
        return guardado;
    }

    public Agenda actualizar(Long id, Agenda agenda) {
        logger.info("Actualizando agenda id={}", id);
        if (!agendaRepository.existsById(id))
            throw new ResourceNotFoundException("Agenda no existe con id: " + id);
        agenda.setId(id);
        Agenda actualizado = agendaRepository.save(agenda);
        logger.info("Agenda actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando agenda id={}", id);
        if (!agendaRepository.existsById(id))
            throw new ResourceNotFoundException("Agenda no existe con id: " + id);
        agendaRepository.deleteById(id);
        logger.info("Agenda eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return agendaRepository.existsById(id);
    }
}
