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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Agenda ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Agenda();
        ejemplo.setId(1L);
        ejemplo.setIdMedico(1L);
        ejemplo.setIdSucursal(1L);
        ejemplo.setFecha(new java.util.Date());
        ejemplo.setHoraInicio("09:00");
        ejemplo.setHoraFin("09:30");
        ejemplo.setDisponible(true);
    }

    @Test
    @DisplayName("listar - retorna lista de agendas")
    void testListar() {
        // GIVEN
        when(agendaRepository.findAll()).thenReturn(List.of(ejemplo));

        // WHEN
        List<Agenda> resultado = agendaService.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        // GIVEN
        when(agendaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Agenda> resultado = agendaService.listar();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna agenda cuando existe")
    void testObtenerPorId() {
        // GIVEN
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        // WHEN
        Agenda resultado = agendaService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(agendaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda agenda correctamente")
    void testGuardar() {
        // GIVEN
        when(agendaRepository.save(any(Agenda.class))).thenReturn(ejemplo);

        // WHEN
        Agenda resultado = agendaService.guardar(ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("actualizar - actualiza agenda existente")
    void testActualizar() {
        // GIVEN
        when(agendaRepository.existsById(1L)).thenReturn(true);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(ejemplo);

        // WHEN
        Agenda resultado = agendaService.actualizar(1L, ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando agenda no existe")
    void testActualizarNoExiste() {
        // GIVEN
        when(agendaRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.actualizar(99L, ejemplo)
        );
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina agenda existente")
    void testEliminar() {
        // GIVEN
        when(agendaRepository.existsById(1L)).thenReturn(true);

        // WHEN
        agendaService.eliminar(1L);

        // THEN
        verify(agendaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando agenda no existe")
    void testEliminarNoExiste() {
        // GIVEN
        when(agendaRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> agendaService.eliminar(99L)
        );
        verify(agendaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        // GIVEN
        when(agendaRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = agendaService.existePorId(1L);

        // THEN
        assertTrue(resultado);
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        // GIVEN
        when(agendaRepository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean resultado = agendaService.existePorId(99L);

        // THEN
        assertFalse(resultado);
    }
}
