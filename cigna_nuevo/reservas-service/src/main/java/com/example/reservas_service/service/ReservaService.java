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

    @Value("${api.usuario.exists}")
    private String usuarioPath;

    @Value("${api.servicio.nombre}")
    private String servicioPath;

    @Value("${api.tratamiento.exists}")
    private String tratamientoPath;

    @Value("${api.servicio.exists}")
    private String servicioExistsPath;

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
        logger.info("Iniciando guardar reserva idUsuario={}, descripcion={}", reserva.getIdUsuario(), reserva.getDescripcion());
        if (reserva.getEstado() != null && !esEstadoValido(reserva.getEstado())) {
            throw new BadRequestException("Estado no valido: " + reserva.getEstado() + ". Valores permitidos: " + ESTADOS_VALIDOS);
        }

        Boolean existeUsuario = validarUsuarioRemoto(reserva.getIdUsuario(), token);
        if (existeUsuario == null) throw new BadRequestException("No se pudo validar la existencia del usuario");
        if (Boolean.FALSE.equals(existeUsuario)) {
            logger.warn("Usuario no existe id={}", reserva.getIdUsuario());
            throw new ResourceNotFoundException("Usuario no existe");
        }
        Boolean existeTratamiento = validarTratamientoRemoto(reserva.getIdTratamiento(), token);
        if (existeTratamiento == null) throw new BadRequestException("No se pudo validar la existencia del tratamiento");
        if (Boolean.FALSE.equals(existeTratamiento)) {
            logger.warn("Tratamiento no existe id={}", reserva.getIdTratamiento());
            throw new ResourceNotFoundException("Tratamiento no existe");
        }
        Boolean existeServicio = validarServicioRemoto(reserva.getIdServicio(), token);
        if (existeServicio == null) throw new BadRequestException("No se pudo validar la existencia del servicio");
        if (Boolean.FALSE.equals(existeServicio)) {
            logger.warn("Servicio no existe id={}", reserva.getIdServicio());
            throw new ResourceNotFoundException("Servicio no existe");
        }

        if (reserva.getIdServicio() != null) {
        String nombreServicio = obtenerNombreServicio(reserva.getIdServicio(), token);
        reserva.setDescripcion(nombreServicio);
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

    public List<Reserva> listarPorUsuario(Long idUsuario) {
        logger.info("Listando reservas del usuario id={}", idUsuario);
        return reservaRepository.findByIdUsuario(idUsuario);
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

        Boolean existeUsuario = validarUsuarioRemoto(reserva.getIdUsuario(), token);
        if (existeUsuario == null) throw new BadRequestException("No se pudo validar la existencia del usuario");
        if (Boolean.FALSE.equals(existeUsuario)) throw new ResourceNotFoundException("Usuario no existe");

         Boolean existeServicio = validarServicioRemoto(reserva.getIdServicio(), token);
        if (existeServicio == null) throw new BadRequestException("No se pudo validar la existencia del servicio");
        if (Boolean.FALSE.equals(existeServicio)) throw new ResourceNotFoundException("Servicio no existe");


        existente.setIdUsuario(reserva.getIdUsuario());
        existente.setIdServicio(reserva.getIdServicio());
        existente.setIdTratamiento(reserva.getIdTratamiento());
        existente.setEstado(reserva.getEstado());
        if (reserva.getIdServicio() != null) {
        String nombreServicio = obtenerNombreServicio(reserva.getIdServicio(), token);
        existente.setDescripcion(nombreServicio);
        }
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

    private Boolean validarUsuarioRemoto(Long idUsuario, String token) {
        try {
            return webClient.get()
                    .uri(String.format(usuarioPath, idUsuario))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar usuario id={}: {}", idUsuario, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de usuarios");
        }
    }
    private String obtenerNombreServicio(Long idServicio, String token) {
        try {
            return webClient.get()
                    .uri(String.format(servicioPath, idServicio))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.warn("No se pudo obtener nombre del servicio id={}", idServicio);
            return "Reserva de servicio";
        }
    }

    private Boolean validarTratamientoRemoto(Long idTratamiento, String token) {
        try {
            return webClient.get()
                    .uri(String.format(tratamientoPath, idTratamiento))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar tratamiento id={}: {}", idTratamiento, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de tratamientos");
        }
    }
    private Boolean validarServicioRemoto(Long idServicio, String token) {
        try {
            return webClient.get()
                    .uri(String.format(servicioExistsPath, idServicio))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexion al validar servicio id={}: {}", idServicio, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de servicios");
        }
    }


}
