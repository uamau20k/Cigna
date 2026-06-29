package com.example.reservas_service.service;

import com.example.reservas_service.exception.BadRequestException;
import com.example.reservas_service.exception.ResourceNotFoundException;
import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);
    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "CONFIRMADA", "CANCELADA");

    private final ReservaRepository reservaRepository;
    private final WebClient webClient;

    @Value("${api.cliente.exists}")
    private String clientePath;

    public ReservaService(ReservaRepository reservaRepository, WebClient webClient) {
        this.reservaRepository = reservaRepository;
        this.webClient = webClient;
    }

    // ─── Regla de negocio 1: validar que el estado sea uno de los permitidos ───
    public boolean esEstadoValido(String estado) {
        return estado != null && ESTADOS_VALIDOS.contains(estado);
    }

    // ─── Regla de negocio 2: solo se puede cancelar si la reserva está PENDIENTE ─
    public boolean puedeCancelarse(Reserva reserva) {
        return "PENDIENTE".equals(reserva.getEstado());
    }

    // ─── Regla de negocio 3: calcular días hasta la fecha de reserva ─────────────
    public long diasHastaReserva(Date fechaReserva) {
        if (fechaReserva == null) return 0;
        long diff = fechaReserva.getTime() - new Date().getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public Reserva guardar(Reserva reserva, String token) {
        logger.info("Iniciando guardar reserva idCliente={}, descripcion={}", reserva.getIdCliente(), reserva.getDescripcion());

        if (reserva.getEstado() != null && !esEstadoValido(reserva.getEstado())) {
            throw new BadRequestException("Estado no valido: " + reserva.getEstado() + ". Valores permitidos: " + ESTADOS_VALIDOS);
        }

        Boolean existeCliente = validarClienteRemoto(reserva.getIdCliente(), token);
        if (existeCliente == null) throw new BadRequestException("No se pudo validar la existencia del cliente");
        if (Boolean.FALSE.equals(existeCliente)) {
            logger.warn("Cliente no existe id={}", reserva.getIdCliente());
            throw new ResourceNotFoundException("Cliente no existe");
        }

        if (reserva.getFechaReserva() == null) reserva.setFechaReserva(new Date());
        if (reserva.getEstado() == null || reserva.getEstado().isBlank()) reserva.setEstado("PENDIENTE");

        Reserva guardada = reservaRepository.save(reserva);
        logger.info("Reserva guardada exitosamente id={}, diasHasta={}", guardada.getId(), diasHastaReserva(guardada.getFechaReserva()));
        return guardada;
    }

    public List<Reserva> listar() {
        logger.info("Listando todas las reservas");
        List<Reserva> lista = reservaRepository.findAll();
        logger.info("Total reservas: {}", lista.size());
        return lista;
    }

    public List<Reserva> listarPorCliente(Long idCliente) {
        logger.info("Listando reservas del cliente id={}", idCliente);
        return reservaRepository.findByIdCliente(idCliente);
    }

    public Reserva obtenerPorId(Long id) {
        logger.info("Buscando reserva id={}", id);
        return reservaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Reserva no encontrada id={}", id);
                    return new ResourceNotFoundException("Reserva no existe");
                });
    }

    public Reserva actualizar(Long id, Reserva reserva, String token) {
        logger.info("Actualizando reserva id={}", id);
        Reserva existente = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no existe"));

        if (reserva.getEstado() != null && !esEstadoValido(reserva.getEstado())) {
            throw new BadRequestException("Estado no valido: " + reserva.getEstado());
        }

        Boolean existeCliente = validarClienteRemoto(reserva.getIdCliente(), token);
        if (existeCliente == null) throw new BadRequestException("No se pudo validar la existencia del cliente");
        if (Boolean.FALSE.equals(existeCliente)) throw new ResourceNotFoundException("Cliente no existe");

        existente.setIdCliente(reserva.getIdCliente());
        existente.setDescripcion(reserva.getDescripcion());
        existente.setEstado(reserva.getEstado());
        if (reserva.getFechaReserva() != null) existente.setFechaReserva(reserva.getFechaReserva());

        Reserva actualizada = reservaRepository.save(existente);
        logger.info("Reserva actualizada id={}", actualizada.getId());
        return actualizada;
    }

    public void cancelar(Long id) {
        logger.info("Cancelando reserva id={}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no existe"));
        if (!puedeCancelarse(reserva)) {
            throw new BadRequestException("Solo se pueden cancelar reservas en estado PENDIENTE. Estado actual: " + reserva.getEstado());
        }
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
        logger.info("Reserva cancelada id={}", id);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando reserva id={}", id);
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no existe");
        }
        reservaRepository.deleteById(id);
        logger.info("Reserva eliminada id={}", id);
    }

    public boolean existePorId(Long id) {
        return reservaRepository.existsById(id);
    }

    private Boolean validarClienteRemoto(Long idCliente, String token) {
        try {
            return webClient.get()
                    .uri(String.format(clientePath, idCliente))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar cliente id={}: {}", idCliente, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de clientes");
        }
    }
}
