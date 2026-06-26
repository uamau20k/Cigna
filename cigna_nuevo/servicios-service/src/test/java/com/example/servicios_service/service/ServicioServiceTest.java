package com.example.servicios_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.servicios_service.exception.ResourceNotFoundException;
import com.example.servicios_service.model.Servicio;
import com.example.servicios_service.repository.ServicioRepository;
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
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    private Servicio ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Servicio();
        ejemplo.setId(1L);
        ejemplo.setNombre("Cardiologia");
        ejemplo.setDescripcion("Evaluacion cardiovascular");
        ejemplo.setPrecio(85000.0);
        ejemplo.setDuracionMinutos(45);
        ejemplo.setActivo(true);
    }

    @Test
    @DisplayName("listar - retorna lista de servicios")
    void testListar() {
        // GIVEN
        when(servicioRepository.findAll()).thenReturn(List.of(ejemplo));

        // WHEN
        List<Servicio> resultado = servicioService.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(servicioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        // GIVEN
        when(servicioRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Servicio> resultado = servicioService.listar();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna servicio cuando existe")
    void testObtenerPorId() {
        // GIVEN
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        // WHEN
        Servicio resultado = servicioService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(servicioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> servicioService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(servicioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda servicio correctamente")
    void testGuardar() {
        // GIVEN
        when(servicioRepository.save(any(Servicio.class))).thenReturn(ejemplo);

        // WHEN
        Servicio resultado = servicioService.guardar(ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }

    @Test
    @DisplayName("actualizar - actualiza servicio existente")
    void testActualizar() {
        // GIVEN
        when(servicioRepository.existsById(1L)).thenReturn(true);
        when(servicioRepository.save(any(Servicio.class))).thenReturn(ejemplo);

        // WHEN
        Servicio resultado = servicioService.actualizar(1L, ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando servicio no existe")
    void testActualizarNoExiste() {
        // GIVEN
        when(servicioRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> servicioService.actualizar(99L, ejemplo)
        );
        verify(servicioRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina servicio existente")
    void testEliminar() {
        // GIVEN
        when(servicioRepository.existsById(1L)).thenReturn(true);

        // WHEN
        servicioService.eliminar(1L);

        // THEN
        verify(servicioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando servicio no existe")
    void testEliminarNoExiste() {
        // GIVEN
        when(servicioRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> servicioService.eliminar(99L)
        );
        verify(servicioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        // GIVEN
        when(servicioRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = servicioService.existePorId(1L);

        // THEN
        assertTrue(resultado);
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        // GIVEN
        when(servicioRepository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean resultado = servicioService.existePorId(99L);

        // THEN
        assertFalse(resultado);
    }
}
