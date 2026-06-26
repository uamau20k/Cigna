package com.example.sucursales_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.sucursales_service.exception.ResourceNotFoundException;
import com.example.sucursales_service.model.Sucursal;
import com.example.sucursales_service.repository.SucursalRepository;
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
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal ejemplo;

    @BeforeEach
    void setUp() {
        ejemplo = new Sucursal();
        ejemplo.setId(1L);
        ejemplo.setNombre("Clinica Central");
        ejemplo.setDireccion("Av. Providencia 1234");
        ejemplo.setTelefono("+56912345678");
        ejemplo.setCiudad("Santiago");
        ejemplo.setActiva(true);
    }

    @Test
    @DisplayName("listar - retorna lista de sucursals")
    void testListar() {
        // GIVEN
        when(sucursalRepository.findAll()).thenReturn(List.of(ejemplo));

        // WHEN
        List<Sucursal> resultado = sucursalService.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(sucursalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - retorna lista vacia cuando no hay registros")
    void testListarVacio() {
        // GIVEN
        when(sucursalRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<Sucursal> resultado = sucursalService.listar();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("obtenerPorId - retorna sucursal cuando existe")
    void testObtenerPorId() {
        // GIVEN
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(ejemplo));

        // WHEN
        Sucursal resultado = sucursalService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(sucursalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> sucursalService.obtenerPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        verify(sucursalRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar - guarda sucursal correctamente")
    void testGuardar() {
        // GIVEN
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(ejemplo);

        // WHEN
        Sucursal resultado = sucursalService.guardar(ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(sucursalRepository, times(1)).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("actualizar - actualiza sucursal existente")
    void testActualizar() {
        // GIVEN
        when(sucursalRepository.existsById(1L)).thenReturn(true);
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(ejemplo);

        // WHEN
        Sucursal resultado = sucursalService.actualizar(1L, ejemplo);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(sucursalRepository, times(1)).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("actualizar - lanza excepcion cuando sucursal no existe")
    void testActualizarNoExiste() {
        // GIVEN
        when(sucursalRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> sucursalService.actualizar(99L, ejemplo)
        );
        verify(sucursalRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - elimina sucursal existente")
    void testEliminar() {
        // GIVEN
        when(sucursalRepository.existsById(1L)).thenReturn(true);

        // WHEN
        sucursalService.eliminar(1L);

        // THEN
        verify(sucursalRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - lanza excepcion cuando sucursal no existe")
    void testEliminarNoExiste() {
        // GIVEN
        when(sucursalRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        assertThrows(
            ResourceNotFoundException.class,
            () -> sucursalService.eliminar(99L)
        );
        verify(sucursalRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("existePorId - retorna true cuando existe")
    void testExistePorIdTrue() {
        // GIVEN
        when(sucursalRepository.existsById(1L)).thenReturn(true);

        // WHEN
        boolean resultado = sucursalService.existePorId(1L);

        // THEN
        assertTrue(resultado);
    }

    @Test
    @DisplayName("existePorId - retorna false cuando no existe")
    void testExistePorIdFalse() {
        // GIVEN
        when(sucursalRepository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean resultado = sucursalService.existePorId(99L);

        // THEN
        assertFalse(resultado);
    }
}
