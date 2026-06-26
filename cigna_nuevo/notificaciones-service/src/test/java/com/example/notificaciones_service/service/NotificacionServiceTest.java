package com.example.notificaciones_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.notificaciones_service.exception.ResourceNotFoundException;
import com.example.notificaciones_service.model.Notificacion;
import com.example.notificaciones_service.repository.NotificacionRepository;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Notificacion();
        ejemplo.setId(1L);
        ejemplo.setIdCliente(1L);
        ejemplo.setTipo("EMAIL");
        ejemplo.setMensaje("Su reserva ha sido confirmada");
        ejemplo.setFechaEnvio(new java.util.Date());
        ejemplo.setLeido(false);
    }

    @Test
    @DisplayName("listar - retorna lista de notificacions")
    void testListar() {
        // GIVEN
        when(notificacionRepository.findAll()).thenReturn(List.of(ejemplo));

        // WHEN
        List<Notificacion> resultado = notificacionService.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        // GIVEN
        when(notificacionRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Notificacion> resultado = notificacionService.listar();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna notificacion cuando existe")
    void testObtenerPorId() {
        // GIVEN
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        // WHEN
        Notificacion resultado = notificacionService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(notificacionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda notificacion correctamente")
    void testGuardar() {
        // GIVEN
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(ejemplo);

        // WHEN
        Notificacion resultado = notificacionService.guardar(ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("actualizar - actualiza notificacion existente")
    void testActualizar() {
        // GIVEN
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(ejemplo);

        // WHEN
        Notificacion resultado = notificacionService.actualizar(1L, ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando notificacion no existe")
    void testActualizarNoExiste() {
        // GIVEN
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.actualizar(99L, ejemplo)
        );
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina notificacion existente")
    void testEliminar() {
        // GIVEN
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        // WHEN
        notificacionService.eliminar(1L);

        // THEN
        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando notificacion no existe")
    void testEliminarNoExiste() {
        // GIVEN
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.eliminar(99L)
        );
        verify(notificacionRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        // GIVEN
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = notificacionService.existePorId(1L);

        // THEN
        assertTrue(resultado);
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        // GIVEN
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean resultado = notificacionService.existePorId(99L);

        // THEN
        assertFalse(resultado);
    }
}
