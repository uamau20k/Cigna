package com.example.usuarios_service.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.usuarios_service.dto.UsuarioDTO;
import com.example.usuarios_service.service.UsuarioService;
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
import com.example.usuarios_service.model.Usuario;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioController Tests")
public class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Usuario usuario;
    private UsuarioDTO dto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Juan", "Pérez", "juan@cigna.cl", "+56912345678", true);
        dto = new UsuarioDTO();
        dto.setId(1L);
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setCorreo("juan@cigna.cl");
        dto.setTelefono("+56912345678");
        dto.setActivo(true);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    @DisplayName("GET /usuarios - lista todos los usuarios")
    void testListar() throws Exception {
        // GIVEN
        when(usuarioService.listar()).thenReturn(List.of(usuario));

        // WHEN / THEN
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(usuarioService, times(1)).listar();
    }

    @Test
    @DisplayName("GET /usuarios/{id} - retorna usuario existente")
    void testObtener() throws Exception {
        // GIVEN
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        // WHEN / THEN
        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(usuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /usuarios/{id}/exists - verifica existencia")
    void testExiste() throws Exception {
        // GIVEN
        when(usuarioService.existePorId(1L)).thenReturn(true);

        // WHEN / THEN
        mockMvc.perform(get("/usuarios/1/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /usuarios - crea usuario correctamente")
    void testCrear() throws Exception {
        // GIVEN
        when(usuarioService.guardar(any())).thenReturn(usuario);

        // WHEN / THEN
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
        verify(usuarioService, times(1)).guardar(any());
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - actualiza usuario correctamente")
    void testActualizar() throws Exception {
        // GIVEN
        when(usuarioService.actualizar(eq(1L), any())).thenReturn(usuario);

        // WHEN / THEN
        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(usuarioService, times(1)).actualizar(eq(1L), any());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - elimina correctamente")
    void testEliminar() throws Exception {
        // GIVEN
        doNothing().when(usuarioService).eliminar(1L);

        // WHEN / THEN
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado exitosamente"));
        verify(usuarioService, times(1)).eliminar(1L);
    }
}
