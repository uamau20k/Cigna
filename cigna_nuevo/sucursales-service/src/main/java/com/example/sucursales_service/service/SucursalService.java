package com.example.sucursales_service.service;

import com.example.sucursales_service.exception.ResourceNotFoundException;
import com.example.sucursales_service.model.Sucursal;
import com.example.sucursales_service.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SucursalService {

    private static final Logger logger = LoggerFactory.getLogger(SucursalService.class);
    private final SucursalRepository sucursalRepository;

    public SucursalService(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public List<Sucursal> listar() {
        logger.info("Listando todos los sucursals");
        List<Sucursal> lista = sucursalRepository.findAll();
        logger.info("Total sucursals: {}", lista.size());
        return lista;
    }

    public Sucursal obtenerPorId(Long id) {
        logger.info("Buscando sucursal id={}", id);
        return sucursalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Sucursal no encontrado id={}", id);
                    return new ResourceNotFoundException("Sucursal no existe con id: " + id);
                });
    }

    public Sucursal guardar(Sucursal sucursal) {
        logger.info("Guardando sucursal");
        Sucursal guardado = sucursalRepository.save(sucursal);
        logger.info("Sucursal guardado id={}", guardado.getId());
        return guardado;
    }

    public Sucursal actualizar(Long id, Sucursal sucursal) {
        logger.info("Actualizando sucursal id={}", id);
        if (!sucursalRepository.existsById(id))
            throw new ResourceNotFoundException("Sucursal no existe con id: " + id);
        sucursal.setId(id);
        Sucursal actualizado = sucursalRepository.save(sucursal);
        logger.info("Sucursal actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando sucursal id={}", id);
        if (!sucursalRepository.existsById(id))
            throw new ResourceNotFoundException("Sucursal no existe con id: " + id);
        sucursalRepository.deleteById(id);
        logger.info("Sucursal eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return sucursalRepository.existsById(id);
    }
}
