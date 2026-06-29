package com.example.compras_service.controller;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.compras_service.model.Compra;
import com.example.compras_service.dto.CompraDTO;
import com.example.compras_service.service.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class CompraControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompraService compraService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Compra compra;
    private CompraDTO compraDto;

    @BeforeEach
    void setUp() {
        compra = new Compra(1L, 2L, 3L, new Date(), "PENDIENTE", "Consulta médica");
        compraDto = new CompraDTO(null, 2L, 3L, null, "PENDIENTE", "Consulta médica");
        CompraController controller = new CompraController(compraService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarCompras() throws Exception {
        when(compraService.listar()).thenReturn(List.of(compra));

        mockMvc.perform(get("/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].idUsuario").value(2L))
                .andExpect(jsonPath("$[0].idServicio").value(3L))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    public void testObtenerCompra() throws Exception {
        when(compraService.obtenerPorId(1L)).thenReturn(compra);

        mockMvc.perform(get("/compras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(2L))
                .andExpect(jsonPath("$.idServicio").value(3L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    public void testCrearCompra() throws Exception {
        when(compraService.guardar(any(Compra.class), anyString())).thenReturn(compra);

        mockMvc.perform(post("/compras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer test-token")
                        .content(objectMapper.writeValueAsString(compraDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(2L))
                .andExpect(jsonPath("$.idServicio").value(3L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    public void testActualizarCompra() throws Exception {
        when(compraService.actualizar(eq(1L), any(Compra.class), anyString())).thenReturn(compra);

        mockMvc.perform(put("/compras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer test-token")
                        .content(objectMapper.writeValueAsString(compraDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(2L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    public void testEliminarCompra() throws Exception {
        doNothing().when(compraService).eliminar(1L);

        mockMvc.perform(delete("/compras/1")
                        .header("Authorization","Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Compra Eliminada Exitosamente"));

        verify(compraService, times(1)).eliminar(1L);
    }

    @Test
    @DisplayName("GET /compras/{id}/exists - verifica existencia")
    public void testExisteCompra() throws Exception {
        when(compraService.existePorId(1L)).thenReturn(true);

        mockMvc.perform(get("/compras/1/exists")
                        .header("Authorization","Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
