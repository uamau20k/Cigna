package com.example.resena_service.service;

import com.example.resena_service.exception.BadRequestException;
import com.example.resena_service.exception.ConflictException;
import com.example.resena_service.exception.ResourceNotFoundException;
import com.example.resena_service.model.Resena;
import com.example.resena_service.repository.ResenaRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;


@Service
public class ResenaService {

    private static final Logger logger = LoggerFactory.getLogger(ResenaService.class);

    private final ResenaRepository resenaRepository;
    private final WebClient webClient;

    @Value("${api.usuario.exists}")
    private String usuarioExistsPath;

    public ResenaService(ResenaRepository resenaRepository, WebClient webClient) {
        this.resenaRepository = resenaRepository;
        this.webClient = webClient;
    }

    private Boolean validarUsuarioExiste(Long idUsuario, String token) {
        try {
            return webClient.get()
                    .uri(String.format(usuarioExistsPath, idUsuario))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexión al validar usuario id={}: {}", idUsuario, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de usuarios");
        }
    }

    @Value("${api.servicio.exists}")
    private String servicioExistsPath;

    private Boolean validarServicioExiste(Long idServicio, String token) {
        try {
            return webClient.get()
                    .uri(String.format(servicioExistsPath, idServicio))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexión al validar servicio id={}: {}", idServicio, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de servicios");
        }
    }

    @Value("${api.compra.existe}")
    private String compraExistePath;

    private Boolean validarCompraExiste(Long idUsuario, Long idServicio, String token) {
        try {
            return webClient.get()
                    .uri(String.format(compraExistePath, idUsuario, idServicio))
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientRequestException e) {
            logger.error("Error de conexión al validar compra usuario={} servicio={}: {}",
                    idUsuario, idServicio, e.getMessage());
            throw new BadRequestException("No se pudo conectar con el servicio de compras");
        }
    }

    public Resena crearResena(Resena resena, String token) {
        logger.info("Iniciando validaciones para reseña: usuario={}, servicio={}",
                resena.getIdUsuario(), resena.getIdServicio());

        // 1. Validar que no exista una reseña duplicada (consulta local, sin red)
        if (resenaRepository.existsByIdServicioAndIdUsuario(resena.getIdServicio(), resena.getIdUsuario())) {
            logger.warn("Reseña rechazada: ya existe para usuario={} servicio={}",
                    resena.getIdUsuario(), resena.getIdServicio());
            throw new ConflictException("Ya existe una reseña de este usuario para este servicio");
        }

        // 2. Validar que el usuario existe
        Boolean usuarioExiste = validarUsuarioExiste(resena.getIdUsuario(), token);
        if (Boolean.FALSE.equals(usuarioExiste)) {
            logger.warn("Reseña rechazada: usuario {} no existe", resena.getIdUsuario());
            throw new ResourceNotFoundException("El usuario no existe");
        }

        // 3. Validar que el servicio existe
        Boolean servicioExiste = validarServicioExiste(resena.getIdServicio(), token);
        if (Boolean.FALSE.equals(servicioExiste)) {
            logger.warn("Reseña rechazada: servicio {} no existe", resena.getIdServicio());
            throw new ResourceNotFoundException("El servicio no existe");
        }

        // 4. Validar que el usuario compró y pagó ese servicio
        Boolean compraExiste = validarCompraExiste(resena.getIdUsuario(), resena.getIdServicio(), token);
        if (Boolean.FALSE.equals(compraExiste)) {
            logger.warn("Reseña rechazada: usuario {} no ha comprado el servicio {}",
                    resena.getIdUsuario(), resena.getIdServicio());
            throw new BadRequestException("No puedes reseñar un servicio que no has comprado");
        }

        // 5. guardamos
        logger.info("Validaciones superadas, guardando reseña...");
        Resena guardada = resenaRepository.save(resena);
        logger.info("Reseña guardada exitosamente id={}", guardada.getId());
        return guardada;
    }

        //Listar reseñas de un servicio
    public List<Resena> obtenerResenasPorServicio(Long idServicio) {
        logger.info("Consultando reseñas del servicio id={}", idServicio);
        List<Resena> resenas = resenaRepository.findByIdServicio(idServicio);
        logger.info("Total reseñas encontradas: {}", resenas.size());
        return resenas;
    }

     //Listar reseñas de un usuario
    public List<Resena> obtenerResenasPorUsuario(Long idUsuario) {
        logger.info("Consultando reseñas del usuario id={}", idUsuario);
        return resenaRepository.findByIdUsuario(idUsuario);
    }

    public Double obtenerPromedioCalificacion(Long idServicio) {
        logger.info("Calculando promedio de calificación para servicio id={}", idServicio);
        Double promedio = resenaRepository.findPromedioCalificacionByIdServicio(idServicio);
        return promedio != null ? promedio : 0.0;
    }

    public List<Resena> listarTodas() {
        logger.info("Listando todas las reseñas");
        List<Resena> resenas = resenaRepository.findAll();
        logger.info("Total reseñas encontradas: {}", resenas.size());
        return resenas;
    }

    public Resena obtenerPorId(Long id) {
        logger.info("Buscando reseña id={}", id);
        return resenaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Reseña no encontrada id={}", id);
                    return new ResourceNotFoundException("Reseña no existe con id: " + id);
                });
    }

    public boolean existePorId(Long id) {
        return resenaRepository.existsById(id);
    }

    public Resena actualizar(Long id, Resena resena) {
        logger.info("Actualizando reseña id={}", id);
        Resena existente = resenaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Reseña no encontrada id={}", id);
                    return new ResourceNotFoundException("Reseña no existe con id: " + id);
                });
        existente.setCalificacion(resena.getCalificacion());
        existente.setComentario(resena.getComentario());
        Resena actualizada = resenaRepository.save(existente);
        logger.info("Reseña actualizada id={}", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando reseña id={}", id);
        if (!resenaRepository.existsById(id)) {
            logger.warn("Reseña no encontrada id={}", id);
            throw new ResourceNotFoundException("Reseña no existe con id: " + id);
        }
        resenaRepository.deleteById(id);
        logger.info("Reseña eliminada id={}", id);
    }
}