package com.example.notificaciones_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.notificaciones_service.exception.BadRequestException;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion ejemplo;
    private final String token = "Bearer fake-token";

    @BeforeEach
    void setUp() {
        ejemplo = new Notificacion();
        ejemplo.setId(1L);
        ejemplo.setIdCliente(1L);
        ejemplo.setTipo("EMAIL");
        ejemplo.setMensaje("Su reserva ha sido confirmada");
        ejemplo.setFechaEnvio(new java.util.Date());
        ejemplo.setLeido(false);

        // El @Value no se resuelve fuera de un contexto Spring, hay que setearlo a mano
        ReflectionTestUtils.setField(notificacionService, "usuarioPath", "/usuarios/%d/exists");
    }

    // Helper para mockear la cadena fluida de WebClient
    @SuppressWarnings("unchecked")
    private void mockUsuarioExiste(Boolean existe) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.justOrEmpty(existe));
    }

    @Test
    @DisplayName("listar - retorna lista de notificacions")
    void testListar() {
        when(notificacionRepository.findAll()).thenReturn(List.of(ejemplo));

        List<Notificacion> resultado = notificacionService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        when(notificacionRepository.findAll()).thenReturn(List.of());

        List<Notificacion> resultado = notificacionService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna notificacion cuando existe")
    void testObtenerPorId() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        Notificacion resultado = notificacionService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(notificacionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda notificacion cuando el usuario existe")
    void testGuardar() {
        mockUsuarioExiste(true);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(ejemplo);

        Notificacion resultado = notificacionService.guardar(ejemplo, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("guardar - lanza excepcion cuando el usuario no existe")
    void testGuardar_usuarioNoExiste() {
        mockUsuarioExiste(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.guardar(ejemplo, token)
        );
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar - actualiza notificacion existente")
    void testActualizar() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        // se quita mockUsuarioExiste(true) porque actualizar() no llama al WebClient
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(ejemplo);

        Notificacion resultado = notificacionService.actualizar(1L, ejemplo, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando notificacion no existe")
    void testActualizarNoExiste() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.actualizar(99L, ejemplo, token)
        );
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina notificacion existente")
    void testEliminar() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        notificacionService.eliminar(1L);

        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando notificacion no existe")
    void testEliminarNoExiste() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> notificacionService.eliminar(99L)
        );
        verify(notificacionRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        assertTrue(notificacionService.existePorId(1L));
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertFalse(notificacionService.existePorId(99L));
    }
}