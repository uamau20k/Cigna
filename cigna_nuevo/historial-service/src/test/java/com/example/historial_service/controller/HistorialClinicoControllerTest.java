package com.example.historial_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.historial_service.dto.HistorialClinicoDTO;
import com.example.historial_service.service.HistorialClinicoService;
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
import com.example.historial_service.model.HistorialClinico;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistorialClinicoController Tests")
public class HistorialClinicoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HistorialClinicoService historialService;

    @InjectMocks
    private HistorialController historialController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private HistorialClinico historial;
    private HistorialClinicoDTO dto;

    @BeforeEach
    void setUp() {
        historial = new HistorialClinico(1L, 1L, new java.util.Date(), "Hipertension", "Medicacion");
        dto = new HistorialClinicoDTO();
        dto.setId(1L);
        dto.setIdCliente(1L);
        dto.setDiagnostico("Hipertension");
        dto.setTratamiento("Medicacion");
        mockMvc = MockMvcBuilders.standaloneSetup(historialController).build();
    }

    @Test
    @DisplayName("GET /historial - lista todos los historials")
    void testListar() throws Exception {
        // GIVEN
        when(historialService.listar()).thenReturn(List.of(historial));

        // WHEN / THEN
        mockMvc.perform(get("/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(historialService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /historial/{id} - retorna historial existente")
    void testObtener() throws Exception {
        // GIVEN
        when(historialService.obtenerPorId(1L)).thenReturn(historial);

        // WHEN / THEN
        mockMvc.perform(get("/historial/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(historialService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /historial/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(historialService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/historial/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /historial - crea historial correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(historialService.guardar(any())).thenReturn(historial);

        // WHEN / THEN
        mockMvc.perform(post("/historial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(historialService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /historial/{id} - actualiza historial correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(historialService.actualizar(eq(1L), any())).thenReturn(historial);

        // WHEN / THEN
        mockMvc.perform(put("/historial/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(historialService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /historial/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(historialService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/historial/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Historial Eliminado Exitosamente"));
        verify(historialService, times(1)).eliminar(1L);
    }
}
