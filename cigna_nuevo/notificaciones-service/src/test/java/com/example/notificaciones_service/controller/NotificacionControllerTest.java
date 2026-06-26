package com.example.notificaciones_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.notificaciones_service.dto.NotificacionDTO;
import com.example.notificaciones_service.service.NotificacionService;
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
import com.example.notificaciones_service.model.Notificacion;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionController Tests")
public class NotificacionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionController notificacionController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Notificacion notificacion;
    private NotificacionDTO dto;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion(1L, 1L, "EMAIL", "Su reserva ha sido confirmada", new java.util.Date(), false);
        dto = new NotificacionDTO();
        dto.setId(1L);
        dto.setIdCliente(1L);
        dto.setTipo("EMAIL");
        dto.setMensaje("Su reserva ha sido confirmada");
        dto.setLeido(false);
        mockMvc = MockMvcBuilders.standaloneSetup(notificacionController).build();
    }

    @Test
    @DisplayName("GET /notificaciones - lista todos los notificacions")
    void testListar() throws Exception {
        // GIVEN
        when(notificacionService.listar()).thenReturn(List.of(notificacion));

        // WHEN / THEN
        mockMvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(notificacionService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /notificaciones/{id} - retorna notificacion existente")
    void testObtener() throws Exception {
        // GIVEN
        when(notificacionService.obtenerPorId(1L)).thenReturn(notificacion);

        // WHEN / THEN
        mockMvc.perform(get("/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(notificacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /notificaciones/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(notificacionService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/notificaciones/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /notificaciones - crea notificacion correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(notificacionService.guardar(any())).thenReturn(notificacion);

        // WHEN / THEN
        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(notificacionService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /notificaciones/{id} - actualiza notificacion correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(notificacionService.actualizar(eq(1L), any())).thenReturn(notificacion);

        // WHEN / THEN
        mockMvc.perform(put("/notificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(notificacionService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /notificaciones/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(notificacionService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/notificaciones/1"))
                .andExpect(status().isNoContent());
        verify(notificacionService, times(1)).eliminar(1L);
    }
}
