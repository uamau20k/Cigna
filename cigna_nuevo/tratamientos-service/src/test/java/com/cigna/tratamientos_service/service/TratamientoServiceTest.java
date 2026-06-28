package com.cigna.tratamientos_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tratamientos_service.exception.ResourceNotFoundException;
import com.example.tratamientos_service.model.Tratamiento;
import com.example.tratamientos_service.repository.TratamientoRepository;
import com.example.tratamientos_service.service.TratamientoService;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TratamientoServiceTest {

    @Mock
    private TratamientoRepository tratamientoRepository;

    @InjectMocks
    private TratamientoService tratamientoService;

    private Tratamiento ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Tratamiento();
        ejemplo.setId(1L);
        ejemplo.setNombre("Fisioterapia");
        ejemplo.setDescripcion("Sesiones de rehabilitacion fisica");
        ejemplo.setDuracionDias(30);
        ejemplo.setMedicamentos("Sin medicamentos");
        ejemplo.setActivo(true);
    }

    @Test
    @DisplayName("listar - retorna lista de tratamientos")
    void testListar() {
        // GIVEN
        when(tratamientoRepository.findAll()).thenReturn(List.of(ejemplo));

        // WHEN
        List<Tratamiento> resultado = tratamientoService.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tratamientoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        // GIVEN
        when(tratamientoRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Tratamiento> resultado = tratamientoService.listar();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna tratamiento cuando existe")
    void testObtenerPorId() {
        // GIVEN
        when(tratamientoRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        // WHEN
        Tratamiento resultado = tratamientoService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(tratamientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN
        when(tratamientoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> tratamientoService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(tratamientoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda tratamiento correctamente")
    void testGuardar() {
        // GIVEN
        when(tratamientoRepository.save(any(Tratamiento.class))).thenReturn(ejemplo);

        // WHEN
        Tratamiento resultado = tratamientoService.guardar(ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(tratamientoRepository, times(1)).save(any(Tratamiento.class));
    }

    @Test
    @DisplayName("actualizar - actualiza tratamiento existente")
    void testActualizar() {
        // GIVEN
        when(tratamientoRepository.existsById(1L)).thenReturn(true);
        when(tratamientoRepository.save(any(Tratamiento.class))).thenReturn(ejemplo);

        // WHEN
        Tratamiento resultado = tratamientoService.actualizar(1L, ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(tratamientoRepository, times(1)).save(any(Tratamiento.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando tratamiento no existe")
    void testActualizarNoExiste() {
        // GIVEN
        when(tratamientoRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> tratamientoService.actualizar(99L, ejemplo)
        );
        verify(tratamientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina tratamiento existente")
    void testEliminar() {
        // GIVEN
        when(tratamientoRepository.existsById(1L)).thenReturn(true);

        // WHEN
        tratamientoService.eliminar(1L);

        // THEN
        verify(tratamientoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando tratamiento no existe")
    void testEliminarNoExiste() {
        // GIVEN
        when(tratamientoRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> tratamientoService.eliminar(99L)
        );
        verify(tratamientoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        // GIVEN
        when(tratamientoRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = tratamientoService.existePorId(1L);

        // THEN
        assertTrue(resultado);
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        // GIVEN
        when(tratamientoRepository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean resultado = tratamientoService.existePorId(99L);

        // THEN
        assertFalse(resultado);
    }
}
