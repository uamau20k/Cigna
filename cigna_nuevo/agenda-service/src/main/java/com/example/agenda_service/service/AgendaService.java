package com.example.agenda_service.service;

import com.example.agenda_service.exception.BadRequestException;
import com.example.agenda_service.exception.ResourceNotFoundException;
import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.repository.AgendaRepository;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;

@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);
    private final AgendaRepository agendaRepository;
    private final WebClient webClient;

    @Value("${api.sucursal.exists}")
    private String sucursalPath;

     public AgendaService(AgendaRepository agendaRepository, WebClient webClient) {
        this.agendaRepository = agendaRepository;
        this.webClient = webClient;
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

    public Agenda guardar(Agenda agenda, String token) {
    logger.info("Guardando agenda");

    Boolean existeSucursal = validarSucursalRemota(agenda.getIdSucursal(), token);
    if (existeSucursal == null) throw new BadRequestException("No se pudo validar la existencia de la sucursal");
    if (Boolean.FALSE.equals(existeSucursal)) {
        logger.warn("Sucursal no existe id={}", agenda.getIdSucursal());
        throw new ResourceNotFoundException("Sucursal no existe id: " + agenda.getIdSucursal());
    }

    Agenda guardado = agendaRepository.save(agenda);
    logger.info("Agenda guardado id={}", guardado.getId());
    return guardado;
}

    public Agenda actualizar(Long id, Agenda agenda, String token) {
    logger.info("Actualizando agenda id={}", id);
    if (!agendaRepository.existsById(id))
        throw new ResourceNotFoundException("Agenda no existe con id: " + id);

    Boolean existeSucursal = validarSucursalRemota(agenda.getIdSucursal(), token);
    if (existeSucursal == null) throw new BadRequestException("No se pudo validar la existencia de la sucursal");
    if (Boolean.FALSE.equals(existeSucursal)) {
        logger.warn("Sucursal no existe id={}", agenda.getIdSucursal());
        throw new ResourceNotFoundException("Sucursal no existe id: " + agenda.getIdSucursal());
    }

    agenda.setId(id);
    Agenda actualizado = agendaRepository.save(agenda);
    logger.info("Agenda actualizado id={}", id);
    return actualizado;
}

    private Boolean validarSucursalRemota(Long idSucursal, String token) {
        try {
            return webClient.get()
                    .uri(String.format(sucursalPath, idSucursal))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar sucursal id={}: {}", idSucursal, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de sucursales");
        }
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
