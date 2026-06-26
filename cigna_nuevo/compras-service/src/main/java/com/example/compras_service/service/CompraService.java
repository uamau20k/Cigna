package com.example.compras_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.compras_service.exception.BadRequestException;
import com.example.compras_service.exception.ResourceNotFoundException;
import com.example.compras_service.model.Compra;
import com.example.compras_service.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CompraService {
    private final CompraRepository compraRepository;
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);

    @Value("${api.paciente.exists}")
    private String pacientePath;

    @Value("${api.servicio.exists}")
    private String servicioPath;

    public CompraService(CompraRepository compraRepository, WebClient webClient) {
        this.compraRepository = compraRepository;
        this.webClient = webClient;
    }

    public Compra guardar(Compra compra) {
        logger.info("Iniciando guardar compra con idPaciente={}, idServicio={}, estado={}",
                compra.getIdPaciente(), compra.getIdServicio(), compra.getEstado());
        try {
            if (compra.getEstado() == null || compra.getEstado().isBlank())
                throw new IllegalArgumentException("estado requerido");
            if (compra.getFechaCompra() == null) compra.setFechaCompra(new Date());

            logger.info("Validando existencia de paciente idPaciente={}", compra.getIdPaciente());
            Boolean existePaciente = webClient.get()
                    .uri(String.format(pacientePath, compra.getIdPaciente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.info("Respuesta de api-gateway: existePaciente={}", existePaciente);
            if (existePaciente == null) {
                logger.error("No se pudo validar la existencia del paciente");
                throw new BadRequestException("No se pudo validar la existencia del paciente");
            }
            if (Boolean.FALSE.equals(existePaciente)) {
                logger.warn("Paciente no existe con id={}", compra.getIdPaciente());
                throw new ResourceNotFoundException("Paciente no existe");
            }

            logger.info("Validando existencia de servicio idServicio={}", compra.getIdServicio());
            Boolean existeServicio = webClient.get()
                    .uri(String.format(servicioPath, compra.getIdServicio()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.info("Respuesta de api-gateway: existeServicio={}", existeServicio);
            if (existeServicio == null) {
                logger.error("No se pudo validar la existencia del servicio");
                throw new BadRequestException("No se pudo validar la existencia del servicio");
            }
            if (Boolean.FALSE.equals(existeServicio)) {
                logger.warn("Servicio no existe con id={}", compra.getIdServicio());
                throw new ResourceNotFoundException("Servicio no existe");
            }

            Compra compraGuardada = compraRepository.save(compra);
            logger.info("Compra guardada exitosamente con id={}", compraGuardada.getId());
            return compraGuardada;
        } catch (Exception e) {
            logger.error("Error al guardar compra: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Compra> listar() {
        logger.info("Listando todas las compras");
        List<Compra> compras = compraRepository.findAll();
        logger.info("Total compras encontradas: {}", compras.size());
        return compras;
    }

    public Compra obtenerPorId(Long id) {
        logger.info("Buscando compra por id={}", id);
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Compra no encontrada id={}", id);
                    return new ResourceNotFoundException("Compra no existe");
                });
        logger.info("Compra encontrada id={}", id);
        return compra;
    }

    public Compra actualizar(Long id, Compra compra) {
        logger.info("Iniciando actualizar compra id={}", id);
        try {
            Compra existente = compraRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Compra no existe"));

            if (compra.getEstado() == null || compra.getEstado().isBlank())
                throw new IllegalArgumentException("estado requerido");
            if (compra.getFechaCompra() == null) compra.setFechaCompra(new Date());

            logger.info("Validando existencia de paciente idPaciente={}", compra.getIdPaciente());
            Boolean existePaciente = webClient.get()
                    .uri(String.format(pacientePath, compra.getIdPaciente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (existePaciente == null)
                throw new BadRequestException("No se pudo validar la existencia del paciente");
            if (Boolean.FALSE.equals(existePaciente))
                throw new ResourceNotFoundException("Paciente no existe");

            logger.info("Validando existencia de servicio idServicio={}", compra.getIdServicio());
            Boolean existeServicio = webClient.get()
                    .uri(String.format(servicioPath, compra.getIdServicio()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (existeServicio == null)
                throw new BadRequestException("No se pudo validar la existencia del servicio");
            if (Boolean.FALSE.equals(existeServicio))
                throw new ResourceNotFoundException("Servicio no existe");

            existente.setIdPaciente(compra.getIdPaciente());
            existente.setIdServicio(compra.getIdServicio());
            existente.setFechaCompra(compra.getFechaCompra());
            existente.setEstado(compra.getEstado());
            existente.setDescripcion(compra.getDescripcion());

            Compra actualizado = compraRepository.save(existente);
            logger.info("Compra actualizada exitosamente id={}", actualizado.getId());
            return actualizado;
        } catch (Exception e) {
            logger.error("Error al actualizar compra id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        logger.info("Iniciando eliminación de compra id={}", id);
        try {
            if (!compraRepository.existsById(id)) {
                logger.warn("Compra no existe para eliminar id={}", id);
                throw new ResourceNotFoundException("Compra no existe");
            }
            compraRepository.deleteById(id);
            logger.info("Compra eliminada exitosamente id={}", id);
        } catch (Exception e) {
            logger.error("Error al eliminar compra id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public boolean existePorId(Long id) {
        return compraRepository.existsById(id);
    }
}
