package com.example.sucursales_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.sucursales_service.dto.SucursalDTO;
import com.example.sucursales_service.service.SucursalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.example.sucursales_service.model.Sucursal;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("SucursalController Tests")
public class SucursalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SucursalService sucursalService;

    @InjectMocks
    private SucursalController sucursalController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Sucursal sucursal;
    private SucursalDTO dto;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal(1L, "Clinica Central", "Av. Test 123", "+56912345678", "Santiago", true);
        dto = new SucursalDTO();
        dto.setId(1L);
        dto.setNombre("Clinica Central");
        dto.setDireccion("Av. Test 123");
        dto.setTelefono("+56912345678");
        dto.setCiudad("Santiago");
        dto.setActiva(true);
        mockMvc = MockMvcBuilders.standaloneSetup(sucursalController).build();
    }

    @Test
    @DisplayName("GET /sucursales - lista todos los sucursals")
    void testListar() throws Exception {
        // GIVEN
        when(sucursalService.listar()).thenReturn(List.of(sucursal));

        // WHEN / THEN
        mockMvc.perform(get("/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(sucursalService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /sucursales/{id} - retorna sucursal existente")
    void testObtener() throws Exception {
        // GIVEN
        when(sucursalService.obtenerPorId(1L)).thenReturn(sucursal);

        // WHEN / THEN
        mockMvc.perform(get("/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(sucursalService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /sucursales/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(sucursalService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/sucursales/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /sucursales - crea sucursal correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(sucursalService.guardar(any())).thenReturn(sucursal);

        // WHEN / THEN
        mockMvc.perform(post("/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(sucursalService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /sucursales/{id} - actualiza sucursal correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(sucursalService.actualizar(eq(1L), any())).thenReturn(sucursal);

        // WHEN / THEN
        mockMvc.perform(put("/sucursales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(sucursalService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /sucursales/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(sucursalService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/sucursales/1"))
                .andExpect(status().isNoContent());
        verify(sucursalService, times(1)).eliminar(1L);
    }
}
