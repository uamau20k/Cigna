package com.example.usuarios_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.usuarios_service.exception.BadRequestException;
import com.example.usuarios_service.exception.ResourceNotFoundException;
import com.example.usuarios_service.model.Usuario;
import com.example.usuarios_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private UsuarioService usuarioService;

    private Usuario ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Usuario(1L, "Juan", "Pérez", "juan@cigna.cl", "+56912345678", true);
    }

    @Test
    @DisplayName("listar - retorna lista de usuarios")
    void testListar() {
        // GIVEN

        when(usuarioRepository.findAll()).thenReturn(List.of(ejemplo));
        // WHEN

        List<Usuario> resultado = usuarioService.listar();
        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    @DisplayName("obtenerPorId - retorna usuario cuando existe")
    void testObtenerPorId() {
        // GIVEN

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(ejemplo));
        Usuario resultado = usuarioService.obtenerPorId(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("guardar - guarda usuario correctamente")
    void testGuardar() {
        // GIVEN

        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        // GIVEN

        when(usuarioRepository.save(any())).thenReturn(ejemplo);
        Usuario resultado = usuarioService.guardar(ejemplo);
        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("guardar - lanza excepcion cuando correo duplicado")
    void testGuardarCorreoDuplicado() {
        // GIVEN

        when(usuarioRepository.existsByCorreo("juan@cigna.cl")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> usuarioService.guardar(ejemplo));
    }

    @Test
    @DisplayName("actualizar - actualiza usuario existente")
    void testActualizar() {
        // GIVEN

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(ejemplo));
        // GIVEN

        when(usuarioRepository.save(any())).thenReturn(ejemplo);
        Usuario resultado = usuarioService.actualizar(1L, ejemplo);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("eliminar - elimina usuario existente")
    void testEliminar() {
        // GIVEN

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        usuarioService.eliminar(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando no existe")
    void testEliminarNoExiste() {
        // GIVEN

        when(usuarioRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.eliminar(99L));
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorId() {
        // GIVEN

        when(usuarioRepository.existsById(1L)).thenReturn(true);
        assertTrue(usuarioService.existePorId(1L));
    }
}
