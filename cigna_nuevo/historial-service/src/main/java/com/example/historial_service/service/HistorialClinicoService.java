package com.example.historial_service.service;

import com.example.historial_service.exception.BadRequestException;
import com.example.historial_service.exception.ResourceNotFoundException;
import com.example.historial_service.model.HistorialClinico;
import com.example.historial_service.repository.HistorialClinicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.Date;
import java.util.List;

@Service
public class HistorialClinicoService {

    private final HistorialClinicoRepository historialRepository;
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(HistorialClinicoService.class);

    @Value("${api.cliente.exists}")
    private String clientePath;

    public HistorialClinicoService(HistorialClinicoRepository historialRepository, WebClient webClient) {
        this.historialRepository = historialRepository;
        this.webClient = webClient;
    }

    public HistorialClinico guardar(HistorialClinico historial) {
        logger.info("Guardando historial idCliente={}", historial.getIdCliente());
        Boolean existeCliente = validarClienteRemoto(historial.getIdCliente());
        if (existeCliente == null) throw new BadRequestException("No se pudo validar el cliente");
        if (Boolean.FALSE.equals(existeCliente)) throw new ResourceNotFoundException("Cliente no existe");
        if (historial.getFecha() == null) historial.setFecha(new Date());
        HistorialClinico guardado = historialRepository.save(historial);
        logger.info("Historial guardado id={}", guardado.getId());
        return guardado;
    }

    public List<HistorialClinico> listar() {
        logger.info("Listando todos los historiales");
        List<HistorialClinico> lista = historialRepository.findAll();
        logger.info("Total historiales: {}", lista.size());
        return lista;
    }

    public List<HistorialClinico> listarPorCliente(Long idCliente) {
        logger.info("Listando historial del cliente id={}", idCliente);
        return historialRepository.findByIdCliente(idCliente);
    }

    public HistorialClinico obtenerPorId(Long id) {
        logger.info("Buscando historial id={}", id);
        return historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial clinico no existe"));
    }

    public HistorialClinico actualizar(Long id, HistorialClinico historial) {
        logger.info("Actualizando historial id={}", id);
        HistorialClinico existente = historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial clinico no existe"));
        Boolean existeCliente = validarClienteRemoto(historial.getIdCliente());
        if (existeCliente == null) throw new BadRequestException("No se pudo validar el cliente");
        if (Boolean.FALSE.equals(existeCliente)) throw new ResourceNotFoundException("Cliente no existe");
        existente.setIdCliente(historial.getIdCliente());
        existente.setDiagnostico(historial.getDiagnostico());
        existente.setTratamiento(historial.getTratamiento());
        if (historial.getFecha() != null) existente.setFecha(historial.getFecha());
        HistorialClinico actualizado = historialRepository.save(existente);
        logger.info("Historial actualizado id={}", actualizado.getId());
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando historial id={}", id);
        if (!historialRepository.existsById(id)) throw new ResourceNotFoundException("Historial clinico no existe");
        historialRepository.deleteById(id);
        logger.info("Historial eliminado id={}", id);
    }

    private Boolean validarClienteRemoto(Long idCliente) {
        try {
            return webClient.get()
                    .uri(String.format(clientePath, idCliente))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar cliente id={}: {}", idCliente, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de clientes");
        }
    }
        public Boolean existePorId(Long id) {
        return historialRepository.existsById(id);
        }
}
