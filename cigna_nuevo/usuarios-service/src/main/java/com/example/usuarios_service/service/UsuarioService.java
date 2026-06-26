package com.example.usuarios_service.service;

import com.example.usuarios_service.exception.BadRequestException;
import com.example.usuarios_service.exception.ResourceNotFoundException;
import com.example.usuarios_service.model.Usuario;
import com.example.usuarios_service.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        logger.info("Listando todos los usuarios");
        List<Usuario> lista = usuarioRepository.findAll();
        logger.info("Total usuarios: {}", lista.size());
        return lista;
    }

    public Usuario obtenerPorId(Long id) {
        logger.info("Buscando usuario id={}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado id={}", id);
                    return new ResourceNotFoundException("Usuario no existe con id: " + id);
                });
    }

    public Usuario guardar(Usuario usuario) {
        logger.info("Guardando usuario correo={}", usuario.getCorreo());
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new BadRequestException("Ya existe un usuario con el correo: " + usuario.getCorreo());
        }
        if (usuario.getActivo() == null) usuario.setActivo(true);
        Usuario guardado = usuarioRepository.save(usuario);
        logger.info("Usuario guardado id={}", guardado.getId());
        return guardado;
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        logger.info("Actualizando usuario id={}", id);
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no existe con id: " + id));
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setCorreo(usuario.getCorreo());
        existente.setTelefono(usuario.getTelefono());
        existente.setActivo(usuario.getActivo());
        Usuario actualizado = usuarioRepository.save(existente);
        logger.info("Usuario actualizado id={}", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando usuario id={}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no existe con id: " + id);
        }
        usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado id={}", id);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }
}
