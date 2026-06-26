package com.example.servicios_service.service;

import com.example.servicios_service.exception.ResourceNotFoundException;
import com.example.servicios_service.model.Servicio;
import com.example.servicios_service.repository.ServicioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioService {

    private static final Logger logger = LoggerFactory.getLogger(ServicioService.class);
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listar() {
        logger.info("Listando todos los servicios");
        List<Servicio> lista = servicioRepository.findAll();
        logger.info("Total servicios: {}", lista.size());
        return lista;
    }

    public Servicio obtenerPorId(Long id) {
        logger.info("Buscando servicio id={}", id);
        return servicioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Servicio no encontrado id={}", id);
                    return new ResourceNotFoundException("Servicio no existe con id: " + id);
                });
    }

    public Servicio guardar(Servicio servicio) {
        logger.info("Guardando servicio");
        Servicio guardado = servicioRepository.save(servicio);
        logger.info("Servicio guardado id={}", guardado.getId());
        return guardado;
    }

    public Servicio actualizar(Long id, Servicio servicio) {
        logger.info("Actualizando servicio id={}", id);
        if (!servicioRepository.existsById(id))
            throw new ResourceNotFoundException("Servicio no existe con id: " + id);
        servicio.setId(id);
        Servicio actualizado = servicioRepository.save(servicio);
        logger.info("Servicio actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando servicio id={}", id);
        if (!servicioRepository.existsById(id))
            throw new ResourceNotFoundException("Servicio no existe con id: " + id);
        servicioRepository.deleteById(id);
        logger.info("Servicio eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return servicioRepository.existsById(id);
    }
}
