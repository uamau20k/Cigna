package com.example.agenda_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.agenda_service.dto.AgendaDTO;
import com.example.agenda_service.service.AgendaService;
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
import com.example.agenda_service.model.Agenda;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgendaController Tests")
public class AgendaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AgendaService agendaService;

    @InjectMocks
    private AgendaController agendaController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Agenda agenda;
    private AgendaDTO dto;

    @BeforeEach
    void setUp() {
        agenda = new Agenda(1L, 1L, 1L, new java.util.Date(), "09:00", "09:30", true);
        dto = new AgendaDTO();
        dto.setId(1L);
        dto.setIdMedico(1L);
        dto.setIdSucursal(1L);
        dto.setFecha(new java.util.Date());
        dto.setHoraInicio("09:00");
        dto.setHoraFin("09:30");
        dto.setDisponible(true);
        mockMvc = MockMvcBuilders.standaloneSetup(agendaController).build();
    }

    @Test
    @DisplayName("GET /agendas - lista todos los agendas")
    void testListar() throws Exception {
        // GIVEN
        when(agendaService.listar()).thenReturn(List.of(agenda));

        // WHEN / THEN
        mockMvc.perform(get("/agendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(agendaService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /agendas/{id} - retorna agenda existente")
    void testObtener() throws Exception {
        // GIVEN
        when(agendaService.obtenerPorId(1L)).thenReturn(agenda);

        // WHEN / THEN
        mockMvc.perform(get("/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(agendaService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /agendas/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(agendaService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/agendas/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /agendas - crea agenda correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(agendaService.guardar(any(), anyString())).thenReturn(agenda);

        // WHEN / THEN
        mockMvc.perform(post("/agendas")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(agendaService, times(1)).guardar(any(), anyString());
    }

    @Test
    @DisplayName("PUT /agendas/{id} - actualiza agenda correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(agendaService.actualizar(eq(1L), any(), anyString())).thenReturn(agenda);

        // WHEN / THEN
        mockMvc.perform(put("/agendas/1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(agendaService, times(1)).actualizar(eq(1L), any(), anyString());
    }

    @Test
    @DisplayName("DELETE /agendas/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(agendaService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/agendas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Agenda eliminado exitosamente"));
        verify(agendaService, times(1)).eliminar(1L);
    }
}
