package com.example.historial_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.historial_service.exception.ResourceNotFoundException;
import com.example.historial_service.model.HistorialClinico;
import com.example.historial_service.repository.HistorialClinicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes"}) // Para evitar advertencias de tipo sin usar
@ExtendWith(MockitoExtension.class)
class HistorialClinicoServiceTest {

    private HistorialClinicoService historialService;

    @Mock private HistorialClinicoRepository historialRepository;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        historialService = new HistorialClinicoService(historialRepository, webClient);
        Field field = HistorialClinicoService.class.getDeclaredField("clientePath");
        field.setAccessible(true);
        field.set(historialService, "http://api/clientes/%d/exists");
    }

    private void mockClienteExistente() {
        // GIVEN

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        // GIVEN

        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        // GIVEN

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        // GIVEN

        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
    }

    @Test
    void testGuardar() {
        mockClienteExistente();
        HistorialClinico h = new HistorialClinico(null, 1L, null, "Hipertension", "Tratamiento A");
        HistorialClinico guardado = new HistorialClinico(1L, 1L, new Date(), "Hipertension", "Tratamiento A");
        // GIVEN

        when(historialRepository.save(any())).thenReturn(guardado);

        HistorialClinico resultado = historialService.guardar(h);

        // WHEN


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(historialRepository).save(any(HistorialClinico.class));
    }

    @Test
    void testListar() {
        HistorialClinico h = new HistorialClinico(1L, 1L, new Date(), "Test", "Test trat");
        // GIVEN

        when(historialRepository.findAll()).thenReturn(List.of(h));

        List<HistorialClinico> lista = historialService.listar();

        assertEquals(1, lista.size());
        verify(historialRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        HistorialClinico h = new HistorialClinico(1L, 1L, new Date(), "Test", "Test trat");
        // GIVEN

        when(historialRepository.findById(1L)).thenReturn(Optional.of(h));

        HistorialClinico resultado = historialService.obtenerPorId(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerNoExiste() {
        // GIVEN

        when(historialRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> historialService.obtenerPorId(99L));
    }

    @Test
    void testEliminar() {
        // GIVEN

        when(historialRepository.existsById(1L)).thenReturn(true);
        historialService.eliminar(1L);
        verify(historialRepository).deleteById(1L);
    }

    @Test
    void testEliminarNoExiste() {
        // GIVEN

        when(historialRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> historialService.eliminar(99L));
    }

    @Test
    void testActualizar() {
        mockClienteExistente();
        HistorialClinico existente = new HistorialClinico(1L, 1L, new Date(), "Original", "Trat A");
        // GIVEN

        when(historialRepository.findById(1L)).thenReturn(Optional.of(existente));
        HistorialClinico cambios = new HistorialClinico(1L, 1L, new Date(), "Actualizado", "Trat B");
        // GIVEN

        when(historialRepository.save(any())).thenReturn(cambios);

        HistorialClinico resultado = historialService.actualizar(1L, cambios);
        assertNotNull(resultado);
        verify(historialRepository).save(any());
    }
}
