package com.example.agenda_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.agenda_service.exception.ResourceNotFoundException;
import com.example.agenda_service.model.Agenda;
import com.example.agenda_service.repository.AgendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes"})
@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    private AgendaService agendaService;

    @Mock private AgendaRepository agendaRepository;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private Agenda ejemplo;

    @BeforeEach
    void setUp() throws Exception {
        agendaService = new AgendaService(agendaRepository, webClient);
        Field field = AgendaService.class.getDeclaredField("sucursalPath");
        field.setAccessible(true);
        field.set(agendaService, "http://api/sucursales/%d/exists");

        ejemplo = new Agenda();
        ejemplo.setId(1L);
        ejemplo.setIdMedico(1L);
        ejemplo.setIdSucursal(1L);
        ejemplo.setFecha(new java.util.Date());
        ejemplo.setHoraInicio("09:00");
        ejemplo.setHoraFin("09:30");
        ejemplo.setDisponible(true);
    }

    private void mockSucursalExistente() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
    }

    @Test
    @DisplayName("listar - retorna lista de agendas")
    void testListar() {
        when(agendaRepository.findAll()).thenReturn(List.of(ejemplo));

        List<Agenda> resultado = agendaService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        when(agendaRepository.findAll()).thenReturn(List.of());

        List<Agenda> resultado = agendaService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna agenda cuando existe")
    void testObtenerPorId() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        Agenda resultado = agendaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(agendaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda agenda correctamente con sucursal valida")
    void testGuardar() {
        mockSucursalExistente();
        when(agendaRepository.save(any(Agenda.class))).thenReturn(ejemplo);

        Agenda resultado = agendaService.guardar(ejemplo, "Bearer token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("guardar - lanza excepcion cuando sucursal no existe")
    void testGuardarSucursalNoExiste() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.FALSE));

        assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.guardar(ejemplo, "Bearer token")
        );
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar - actualiza agenda existente con sucursal valida")
    void testActualizar() {
        mockSucursalExistente();
        when(agendaRepository.existsById(1L)).thenReturn(true);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(ejemplo);

        Agenda resultado = agendaService.actualizar(1L, ejemplo, "Bearer token");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando agenda no existe")
    void testActualizarNoExiste() {
        when(agendaRepository.existsById(99L)).thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.actualizar(99L, ejemplo, "Bearer token")
        );
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina agenda existente")
    void testEliminar() {
        when(agendaRepository.existsById(1L)).thenReturn(true);

        agendaService.eliminar(1L);

        verify(agendaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando agenda no existe")
    void testEliminarNoExiste() {
        when(agendaRepository.existsById(99L)).thenReturn(false);

        assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.eliminar(99L)
        );
        verify(agendaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        when(agendaRepository.existsById(1L)).thenReturn(true);
        assertTrue(agendaService.existePorId(1L));
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        when(agendaRepository.existsById(99L)).thenReturn(false);
        assertFalse(agendaService.existePorId(99L));
    }
}