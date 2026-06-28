package com.cigna.tratamientos_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cigna.tratamientos_service.exception.ResourceNotFoundException;
import com.cigna.tratamientos_service.model.Tratamiento;
import com.cigna.tratamientos_service.repository.TratamientoRepository;

import java.util.List;

@Service
public class TratamientoService {

    private static final Logger logger = LoggerFactory.getLogger(TratamientoService.class);
    private final TratamientoRepository tratamientoRepository;

    public TratamientoService(TratamientoRepository tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

    public List<Tratamiento> listar() {
        logger.info("Listando todos los tratamientos");
        List<Tratamiento> lista = tratamientoRepository.findAll();
        logger.info("Total tratamientos: {}", lista.size());
        return lista;
    }

    public Tratamiento obtenerPorId(Long id) {
        logger.info("Buscando tratamiento id={}", id);
        return tratamientoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Tratamiento no encontrado id={}", id);
                    return new ResourceNotFoundException("Tratamiento no existe con id: " + id);
                });
    }

    public Tratamiento guardar(Tratamiento tratamiento) {
        logger.info("Guardando tratamiento");
        Tratamiento guardado = tratamientoRepository.save(tratamiento);
        logger.info("Tratamiento guardado id={}", guardado.getId());
        return guardado;
    }

    public Tratamiento actualizar(Long id, Tratamiento tratamiento) {
        logger.info("Actualizando tratamiento id={}", id);
        if (!tratamientoRepository.existsById(id))
            throw new ResourceNotFoundException("Tratamiento no existe con id: " + id);
        tratamiento.setId(id);
        Tratamiento actualizado = tratamientoRepository.save(tratamiento);
        logger.info("Tratamiento actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando tratamiento id={}", id);
        if (!tratamientoRepository.existsById(id))
            throw new ResourceNotFoundException("Tratamiento no existe con id: " + id);
        tratamientoRepository.deleteById(id);
        logger.info("Tratamiento eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return tratamientoRepository.existsById(id);
    }
}
