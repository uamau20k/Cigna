package com.example.servicios_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.servicios_service.dto.ServicioDTO;
import com.example.servicios_service.service.ServicioService;
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
import com.example.servicios_service.model.Servicio;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicioController Tests")
public class ServicioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServicioService servicioService;

    @InjectMocks
    private ServicioController servicioController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Servicio servicio;
    private ServicioDTO dto;

    @BeforeEach
    void setUp() {
        servicio = new Servicio(1L, "Cardiologia", "Evaluacion cardiovascular", 85000.0, 45, true);
        dto = new ServicioDTO();
        dto.setId(1L);
        dto.setNombre("Cardiologia");
        dto.setDescripcion("Evaluacion cardiovascular");
        dto.setPrecio(85000.0);
        dto.setDuracionMinutos(45);
        dto.setActivo(true);
        mockMvc = MockMvcBuilders.standaloneSetup(servicioController).build();
    }

    @Test
    @DisplayName("GET /servicios - lista todos los servicios")
    void testListar() throws Exception {
        // GIVEN
        when(servicioService.listar()).thenReturn(List.of(servicio));

        // WHEN / THEN
        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(servicioService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /servicios/{id} - retorna servicio existente")
    void testObtener() throws Exception {
        // GIVEN
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);

        // WHEN / THEN
        mockMvc.perform(get("/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(servicioService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /servicios/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(servicioService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/servicios/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /servicios - crea servicio correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(servicioService.guardar(any())).thenReturn(servicio);

        // WHEN / THEN
        mockMvc.perform(post("/servicios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(servicioService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /servicios/{id} - actualiza servicio correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(servicioService.actualizar(eq(1L), any())).thenReturn(servicio);

        // WHEN / THEN
        mockMvc.perform(put("/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(servicioService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /servicios/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(servicioService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/servicios/1"))
                .andExpect(status().isNoContent());
        verify(servicioService, times(1)).eliminar(1L);
    }
}
