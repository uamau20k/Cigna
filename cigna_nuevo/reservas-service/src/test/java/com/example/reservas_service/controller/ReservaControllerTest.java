package com.example.reservas_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.reservas_service.dto.ReservaDTO;
import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReservaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservaService reservaService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Reserva reserva;
    private ReservaDTO reservaDto;

    @BeforeEach
    void setUp() {
        reserva = new Reserva(1L, 1L, new Date(), "Reserva laptop", "PENDIENTE");
        reservaDto = new ReservaDTO(null, 1L, null, "Reserva laptop", "PENDIENTE");
        ReservaController controller = new ReservaController(reservaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListarReservas() throws Exception {
        when(reservaService.listar()).thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idUsuario").value(1L))
                .andExpect(jsonPath("$[0].descripcion").value("Reserva laptop"))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void testObtenerReserva() throws Exception {
        when(reservaService.obtenerPorId(1L)).thenReturn(reserva);

        mockMvc.perform(get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Reserva laptop"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testCrearReserva() throws Exception {
        when(reservaService.guardar(any(Reserva.class), anyString())).thenReturn(reserva);

        mockMvc.perform(post("/reservas")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Reserva laptop"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testActualizarReserva() throws Exception {
        when(reservaService.actualizar(eq(1L), any(Reserva.class), anyString())).thenReturn(reserva);

        mockMvc.perform(put("/reservas/1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Reserva laptop"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void testEliminarReserva() throws Exception {
        doNothing().when(reservaService).eliminar(1L);

        mockMvc.perform(delete("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva Eliminada Exitosamente"));
        verify(reservaService, times(1)).eliminar(1L);
    }

    @Test
    void testExisteReserva() throws Exception {
        when(reservaService.existePorId(1L)).thenReturn(true);

        mockMvc.perform(get("/reservas/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
