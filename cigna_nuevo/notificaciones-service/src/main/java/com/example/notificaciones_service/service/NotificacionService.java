package com.example.notificaciones_service.service;

import com.example.notificaciones_service.exception.ResourceNotFoundException;
import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> listar() {
        logger.info("Listando todos los notificacions");
        List<Notificacion> lista = notificacionRepository.findAll();
        logger.info("Total notificacions: {}", lista.size());
        return lista;
    }

    public Notificacion obtenerPorId(Long id) {
        logger.info("Buscando notificacion id={}", id);
        return notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Notificacion no encontrado id={}", id);
                    return new ResourceNotFoundException("Notificacion no existe con id: " + id);
                });
    }

    public Notificacion guardar(Notificacion notificacion) {
        logger.info("Guardando notificacion");
        Notificacion guardado = notificacionRepository.save(notificacion);
        logger.info("Notificacion guardado id={}", guardado.getId());
        return guardado;
    }

    public Notificacion actualizar(Long id, Notificacion notificacion) {
        logger.info("Actualizando notificacion id={}", id);
        if (!notificacionRepository.existsById(id))
            throw new ResourceNotFoundException("Notificacion no existe con id: " + id);
        notificacion.setId(id);
        Notificacion actualizado = notificacionRepository.save(notificacion);
        logger.info("Notificacion actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando notificacion id={}", id);
        if (!notificacionRepository.existsById(id))
            throw new ResourceNotFoundException("Notificacion no existe con id: " + id);
        notificacionRepository.deleteById(id);
        logger.info("Notificacion eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return notificacionRepository.existsById(id);
    }
}
