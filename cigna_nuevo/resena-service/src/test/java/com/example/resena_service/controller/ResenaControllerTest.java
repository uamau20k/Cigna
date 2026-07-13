package com.example.resena_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.resena_service.dto.ResenaDTO;
import com.example.resena_service.exception.BadRequestException;
import com.example.resena_service.exception.ConflictException;
import com.example.resena_service.exception.GlobalExceptionHandler;
import com.example.resena_service.exception.ResourceNotFoundException;
import com.example.resena_service.model.Resena;
import com.example.resena_service.service.ResenaService;
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

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResenaController Tests")
class ResenaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResenaService resenaService;

    @InjectMocks
    private ResenaController resenaController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String token = "Bearer fake-token";
    private Resena resena;
    private ResenaDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        resena = new Resena();
        resena.setId(1L);
        resena.setIdUsuario(1L);
        resena.setIdServicio(2L);
        resena.setCalificacion(5);
        resena.setComentario("Excelente atención");
        resena.setFechaResena(LocalDateTime.now());

        dto = new ResenaDTO();
        dto.setIdUsuario(1L);
        dto.setIdServicio(2L);
        dto.setCalificacion(5);
        dto.setComentario("Excelente atención");

        mockMvc = MockMvcBuilders.standaloneSetup(resenaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /resenas - lista todas las reseñas")
    void testListarTodas() throws Exception {
        when(resenaService.listarTodas()).thenReturn(List.of(resena));

        mockMvc.perform(get("/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(resenaService, times(1)).listarTodas();
    }
    
    @Test
    @DisplayName("POST /resenas - crea reseña correctamente")
    void testCrearResena() throws Exception {
        // GIVEN
        when(resenaService.crearResena(any(Resena.class), eq(token))).thenReturn(resena);

        // WHEN / THEN
        mockMvc.perform(post("/resenas")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.calificacion").value(5));

        verify(resenaService, times(1)).crearResena(any(Resena.class), eq(token));
    }
    
    @Test
    @DisplayName("POST /resenas - reseña duplicada, devuelve 409")
    void testCrearResena_duplicada() throws Exception {
        // GIVEN
        when(resenaService.crearResena(any(Resena.class), eq(token)))
            .thenThrow(new ConflictException("Ya existe una reseña de este usuario para este servicio"));

        // WHEN / THEN
        mockMvc.perform(post("/resenas")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ya existe una reseña de este usuario para este servicio"));

        verify(resenaService, times(1)).crearResena(any(Resena.class), eq(token));
    }

    @Test
    @DisplayName("POST /resenas - usuario no existe, devuelve 404")
    void testCrearResena_usuarioNoExiste() throws Exception {
        // GIVEN
        when(resenaService.crearResena(any(Resena.class), eq(token)))
            .thenThrow(new ResourceNotFoundException("El usuario no existe"));

        // WHEN / THEN
        mockMvc.perform(post("/resenas")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El usuario no existe"));

        verify(resenaService, times(1)).crearResena(any(Resena.class), eq(token));
    }

    @Test
    @DisplayName("POST /resenas - usuario no compró el servicio, devuelve 400")
    void testCrearResena_noComprado() throws Exception {
        // GIVEN
        when(resenaService.crearResena(any(Resena.class), eq(token)))
            .thenThrow(new BadRequestException("No puedes reseñar un servicio que no has comprado"));

        // WHEN / THEN
        mockMvc.perform(post("/resenas")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No puedes reseñar un servicio que no has comprado"));

        verify(resenaService, times(1)).crearResena(any(Resena.class), eq(token));
    }

    @Test
    @DisplayName("GET /resenas/servicio/{id} - lista reseñas del servicio")
    void testObtenerPorServicio() throws Exception {
        // GIVEN
        when(resenaService.obtenerResenasPorServicio(2L)).thenReturn(List.of(resena));

        // WHEN / THEN
        mockMvc.perform(get("/resenas/servicio/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idServicio").value(2L));

        verify(resenaService, times(1)).obtenerResenasPorServicio(2L);
    }

    @Test
    @DisplayName("GET /resenas/usuario/{id} - lista reseñas del usuario")
    void testObtenerPorUsuario() throws Exception {
        // GIVEN
        when(resenaService.obtenerResenasPorUsuario(1L)).thenReturn(List.of(resena));

        // WHEN / THEN
        mockMvc.perform(get("/resenas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idUsuario").value(1L));

        verify(resenaService, times(1)).obtenerResenasPorUsuario(1L);
    }

    @Test
    @DisplayName("GET /resenas/servicio/{id}/promedio - retorna el promedio")
    void testObtenerPromedio() throws Exception {
        // GIVEN
        when(resenaService.obtenerPromedioCalificacion(2L)).thenReturn(4.5);

        // WHEN / THEN
        mockMvc.perform(get("/resenas/servicio/2/promedio"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));

        verify(resenaService, times(1)).obtenerPromedioCalificacion(2L);
    }
}