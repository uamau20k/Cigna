package com.example.notificaciones_service.service;

import com.example.notificaciones_service.exception.BadRequestException;
import com.example.notificaciones_service.exception.ResourceNotFoundException;
import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.List;

@Service
public class NotificacionService {
     private final WebClient webClient;
      @Value("${api.usuario.exists}")
        private String usuarioPath;

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, WebClient webClient) {
        this.notificacionRepository = notificacionRepository;
        this.webClient = webClient;
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

   public Notificacion guardar(Notificacion notificacion, String token) {
        logger.info("Guardando notificacion");
        Boolean existeUsuario = validarUsuarioRemoto(notificacion.getIdCliente(), token);
        if (existeUsuario == null) {
            throw new BadRequestException("No se pudo validar la existencia del usuario");
        }
        if (Boolean.FALSE.equals(existeUsuario)) {
            logger.warn("Usuario no existe id={}", notificacion.getIdCliente());
            throw new ResourceNotFoundException("Usuario no existe");
        }
        if (notificacion.getFechaEnvio() == null) {
            notificacion.setFechaEnvio(new java.util.Date());
        }
        if (notificacion.getLeido() == null) {
            notificacion.setLeido(false);
        }

        logger.info("Antes de guardar: fechaEnvio={}, leido={}",
            notificacion.getFechaEnvio(), notificacion.getLeido());

        Notificacion guardado = notificacionRepository.save(notificacion);

        logger.info("Notificacion guardado id={}, fechaEnvio={}",
            guardado.getId(), guardado.getFechaEnvio());

        return guardado;
    }

    public Notificacion actualizar(Long id, Notificacion notificacion, String token) {
        logger.info("Actualizando notificacion id={}", id);
        if (!notificacionRepository.existsById(id))
            throw new ResourceNotFoundException("Notificacion no existe con id: " + id);
        notificacion.setId(id);
        if (notificacion.getFechaEnvio() == null) {  // <-- agrega esto
            notificacion.setFechaEnvio(new java.util.Date());
        }
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
}
