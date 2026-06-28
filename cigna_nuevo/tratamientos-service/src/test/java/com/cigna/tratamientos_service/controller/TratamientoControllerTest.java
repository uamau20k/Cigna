package com.cigna.tratamientos_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cigna.tratamientos_service.controller.TratamientoController;
import com.cigna.tratamientos_service.dto.TratamientoDTO;
import com.cigna.tratamientos_service.model.Tratamiento;
import com.cigna.tratamientos_service.service.TratamientoService;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("TratamientoController Tests")
public class TratamientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TratamientoService tratamientoService;

    @InjectMocks
    private TratamientoController tratamientoController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Tratamiento tratamiento;
    private TratamientoDTO dto;

    @BeforeEach
    void setUp() {
        tratamiento = new Tratamiento(1L, "Fisioterapia", "Rehabilitacion fisica", 30, "Sin medicamentos", true);
        dto = new TratamientoDTO();
        dto.setId(1L);
        dto.setNombre("Fisioterapia");
        dto.setDescripcion("Rehabilitacion fisica");
        dto.setDuracionDias(30);
        dto.setMedicamentos("Sin medicamentos");
        dto.setActivo(true);
        mockMvc = MockMvcBuilders.standaloneSetup(tratamientoController).build();
    }

    @Test
    @DisplayName("GET /tratamientos - lista todos los tratamientos")
    void testListar() throws Exception {
        // GIVEN
        when(tratamientoService.listar()).thenReturn(List.of(tratamiento));

        // WHEN / THEN
        mockMvc.perform(get("/tratamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(tratamientoService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /tratamientos/{id} - retorna tratamiento existente")
    void testObtener() throws Exception {
        // GIVEN
        when(tratamientoService.obtenerPorId(1L)).thenReturn(tratamiento);

        // WHEN / THEN
        mockMvc.perform(get("/tratamientos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(tratamientoService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /tratamientos/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(tratamientoService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/tratamientos/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /tratamientos - crea tratamiento correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(tratamientoService.guardar(any())).thenReturn(tratamiento);

        // WHEN / THEN
        mockMvc.perform(post("/tratamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(tratamientoService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /tratamientos/{id} - actualiza tratamiento correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(tratamientoService.actualizar(eq(1L), any())).thenReturn(tratamiento);

        // WHEN / THEN
        mockMvc.perform(put("/tratamientos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(tratamientoService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /tratamientos/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(tratamientoService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/tratamientos/1"))
                .andExpect(status().isNoContent());
        verify(tratamientoService, times(1)).eliminar(1L);
    }
}
