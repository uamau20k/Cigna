package com.example.resena_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.example.resena_service.exception.BadRequestException;
import com.example.resena_service.exception.ConflictException;
import com.example.resena_service.exception.ResourceNotFoundException;
import com.example.resena_service.model.Resena;
import com.example.resena_service.repository.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private ResenaService resenaService;

    private final String token = "Bearer fake-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(resenaService, "usuarioExistsPath", "/usuarios/%d/exists");
        ReflectionTestUtils.setField(resenaService, "servicioExistsPath", "/servicios/%d/exists");
        ReflectionTestUtils.setField(resenaService, "compraExistePath", "/compras/usuario/%d/servicio/%d/existe");
    }

    // Helper simula CUALQUIERA de tus 3 llamadas WebClient
    @SuppressWarnings("unchecked")
    private void mockWebClientResponse(Boolean respuesta) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.justOrEmpty(respuesta));
    }


    @Test
    @DisplayName("listarTodas - retorna todas las reseñas")
    void testListarTodas() {
        Resena r1 = new Resena();
        r1.setId(1L);

        when(resenaRepository.findAll()).thenReturn(List.of(r1));

        List<Resena> resultado = resenaService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("crearResena - guarda la reseña cuando todas las validaciones pasan")
    void testCrearResena_exitoso() {
        // Arrange (preparar los datos y mocks)
        Resena nueva = new Resena();
        nueva.setIdUsuario(1L);
        nueva.setIdServicio(2L);
        nueva.setCalificacion(5);
        nueva.setComentario("Excelente atención");

        Resena guardada = new Resena();
        guardada.setId(10L);
        guardada.setIdUsuario(1L);
        guardada.setIdServicio(2L);
        guardada.setCalificacion(5);
        guardada.setComentario("Excelente atención");

        when(resenaRepository.existsByIdServicioAndIdUsuario(2L, 1L)).thenReturn(false);
        mockWebClientResponse(true);
        when(resenaRepository.save(any(Resena.class))).thenReturn(guardada);

        // Act (ejecutar el método que estamos probando)
        Resena resultado = resenaService.crearResena(nueva, token);

        // Assert (verificar que el resultado sea el esperado)
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(5, resultado.getCalificacion());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    // Test en caso de reseña duplicada
    @Test
    @DisplayName("crearResena - lanza ConflictException cuando ya existe una reseña")
    void testCrearResena_duplicada() {
        // Arrange
        Resena nueva = new Resena();
        nueva.setIdUsuario(1L);
        nueva.setIdServicio(2L);
        nueva.setCalificacion(5);
        nueva.setComentario("Excelente atención");

        when(resenaRepository.existsByIdServicioAndIdUsuario(2L, 1L)).thenReturn(true);

        // Act + Assert
        ConflictException ex = assertThrows(
            ConflictException.class,
            () -> resenaService.crearResena(nueva, token)
        );

        assertTrue(ex.getMessage().contains("Ya existe"));
        verify(resenaRepository, never()).save(any());
    }

    // test para el caso de que el usuario no exista
    @Test
    @DisplayName("crearResena - lanza ResourceNotFoundException cuando el usuario no existe")
    void testCrearResena_usuarioNoExiste() {
        // Arrange
        Resena nueva = new Resena();
        nueva.setIdUsuario(1L);
        nueva.setIdServicio(2L);
        nueva.setCalificacion(5);
        nueva.setComentario("Excelente atención");

        when(resenaRepository.existsByIdServicioAndIdUsuario(2L, 1L)).thenReturn(false);
        mockWebClientResponse(false);

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> resenaService.crearResena(nueva, token)
        );

        assertTrue(ex.getMessage().contains("usuario"));
        verify(resenaRepository, never()).save(any());
    }

    // test para que en caso de que el servicio no exista
    @Test
    @DisplayName("crearResena - lanza ResourceNotFoundException cuando el servicio no existe")
    void testCrearResena_servicioNoExiste() {
        // Arrange
        Resena nueva = new Resena();
        nueva.setIdUsuario(1L);
        nueva.setIdServicio(2L);
        nueva.setCalificacion(5);
        nueva.setComentario("Excelente atención");

        when(resenaRepository.existsByIdServicioAndIdUsuario(2L, 1L)).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class))
            .thenReturn(Mono.just(true))   // usuario existe
            .thenReturn(Mono.just(false)); // servicio NO existe

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(
            ResourceNotFoundException.class,
            () -> resenaService.crearResena(nueva, token)
        );

        assertTrue(ex.getMessage().contains("servicio"));
        verify(resenaRepository, never()).save(any());
    }

    // test en caso de que el usuario no haya comprado el servicio
    @Test
    @DisplayName("crearResena - lanza BadRequestException cuando el usuario no compró el servicio")
    void testCrearResena_compraNoExiste() {
        // Arrange
        Resena nueva = new Resena();
        nueva.setIdUsuario(1L);
        nueva.setIdServicio(2L);
        nueva.setCalificacion(5);
        nueva.setComentario("Excelente atención");

        when(resenaRepository.existsByIdServicioAndIdUsuario(2L, 1L)).thenReturn(false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class))
            .thenReturn(Mono.just(true))   // usuario existe
            .thenReturn(Mono.just(true))   // servicio existe
            .thenReturn(Mono.just(false)); // compra NO existe

        // Act + Assert
        BadRequestException ex = assertThrows(
            BadRequestException.class,
            () -> resenaService.crearResena(nueva, token)
        );

        assertTrue(ex.getMessage().contains("comprado"));
        verify(resenaRepository, never()).save(any());
    }

    // Test para obtener reseñas por servicio
    @Test
    @DisplayName("obtenerResenasPorServicio - retorna lista de reseñas del servicio")
    void testObtenerResenasPorServicio() {
        Resena r1 = new Resena();
        r1.setId(1L);
        r1.setIdServicio(2L);

        when(resenaRepository.findByIdServicio(2L)).thenReturn(List.of(r1));

        List<Resena> resultado = resenaService.obtenerResenasPorServicio(2L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(resenaRepository, times(1)).findByIdServicio(2L);
    }

    // Test para obtener reseñas por usuario
    @Test
    @DisplayName("obtenerResenasPorUsuario - retorna lista de reseñas del usuario")
    void testObtenerResenasPorUsuario() {
        Resena r1 = new Resena();
        r1.setId(1L);
        r1.setIdUsuario(5L);

        when(resenaRepository.findByIdUsuario(5L)).thenReturn(List.of(r1));

        List<Resena> resultado = resenaService.obtenerResenasPorUsuario(5L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(resenaRepository, times(1)).findByIdUsuario(5L);
    }

    // Test para obtener el promedio de calificación de un servicio
    @Test
    @DisplayName("obtenerPromedioCalificacion - retorna el promedio cuando hay reseñas")
    void testObtenerPromedio_conDatos() {
        when(resenaRepository.findPromedioCalificacionByIdServicio(2L)).thenReturn(4.5);

        Double resultado = resenaService.obtenerPromedioCalificacion(2L);

        assertEquals(4.5, resultado);
    }

    @Test
    @DisplayName("obtenerPromedioCalificacion - retorna 0.0 cuando no hay reseñas")
    void testObtenerPromedio_sinDatos() {
        when(resenaRepository.findPromedioCalificacionByIdServicio(99L)).thenReturn(null);

        Double resultado = resenaService.obtenerPromedioCalificacion(99L);

        assertEquals(0.0, resultado);
    }

    @Test
    @DisplayName("obtenerResenasPorServicio - retorna lista vacía cuando no hay reseñas")
    void testObtenerResenasPorServicio_vacio() {
        when(resenaRepository.findByIdServicio(99L)).thenReturn(List.of());

        List<Resena> resultado = resenaService.obtenerResenasPorServicio(99L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}