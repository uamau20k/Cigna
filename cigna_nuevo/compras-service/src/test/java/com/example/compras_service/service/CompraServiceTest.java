package com.example.compras_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.compras_service.exception.ResourceNotFoundException;
import com.example.compras_service.model.Compra;
import com.example.compras_service.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes"})
@ExtendWith(MockitoExtension.class)
class CompraServiceTest {
    private CompraService compraService;
    @Mock
    private CompraRepository compraRepository;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        compraService = new CompraService(compraRepository, webClient);
        Field usuarioField = CompraService.class.getDeclaredField("usuarioPath");
        usuarioField.setAccessible(true);
        usuarioField.set(compraService, "http://api/usuarios/%d/exists");

        Field servicioField = CompraService.class.getDeclaredField("servicioPath");
        servicioField.setAccessible(true);
        servicioField.set(compraService, "http://api/servicios/%d/exists");
    }

    private void mockUsuarioYServicioExistentes() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
    }

    @Test
    void testGuardar() {
        mockUsuarioYServicioExistentes();
        Compra compra = new Compra(null, 1L, 1L, new Date(), "PENDIENTE", "Consulta médica");
        when(compraRepository.save(any(Compra.class))).thenReturn(
                new Compra(1L, 1L, 1L, new Date(), "PENDIENTE", "Consulta médica"));

        Compra resultado = compraService.guardar(compra, "Bearer test-token");

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(compraRepository).save(any(Compra.class));
    }

    @Test
    void testListar() {
        when(compraRepository.findAll()).thenReturn(
                List.of(new Compra(1L, 1L, 2L, new Date(), "PAGADO", "Examen")));

        List<Compra> compras = compraService.listar();

        assertNotNull(compras);
        assertEquals(1, compras.size());
        verify(compraRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Long id = 1L;
        Compra compra = new Compra(id, 1L, 2L, new Date(), "PAGADO", "Examen");
        when(compraRepository.findById(id)).thenReturn(Optional.of(compra));

        Compra resultado = compraService.obtenerPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(compraRepository).findById(id);
    }

    @Test
    void testObtenerPorIdNoExiste() {
        Long id = 99L;
        when(compraRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> compraService.obtenerPorId(id));
    }

    @Test
    void testActualizar() {
        mockUsuarioYServicioExistentes();
        Long id = 1L;
        Compra existente = new Compra(id, 1L, 1L, new Date(), "PENDIENTE", "Original");
        when(compraRepository.findById(id)).thenReturn(Optional.of(existente));

        Compra cambios = new Compra(id, 2L, 3L, new Date(), "PAGADO", "Actualizado");
        Compra guardado = new Compra(id, 2L, 3L, cambios.getFechaCompra(), "PAGADO", "Actualizado");
        when(compraRepository.save(any(Compra.class))).thenReturn(guardado);

        Compra resultado = compraService.actualizar(id, cambios, "Bearer test-token");

        assertNotNull(resultado);
        assertEquals("PAGADO", resultado.getEstado());
        assertEquals(2L, resultado.getIdUsuario());
        verify(compraRepository).findById(id);
        verify(compraRepository).save(any(Compra.class));
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        when(compraRepository.existsById(id)).thenReturn(true);

        compraService.eliminar(id);

        verify(compraRepository).deleteById(id);
    }

    @Test
    void testEliminarNoExiste() {
        Long id = 99L;
        when(compraRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> compraService.eliminar(id));
    }

    @Test
    void testExistePorId() {
        when(compraRepository.existsById(1L)).thenReturn(true);
        when(compraRepository.existsById(99L)).thenReturn(false);

        assertTrue(compraService.existePorId(1L));
        assertFalse(compraService.existePorId(99L));
    }
}
