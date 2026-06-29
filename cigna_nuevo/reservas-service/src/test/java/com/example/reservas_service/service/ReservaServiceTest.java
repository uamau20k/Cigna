package com.example.reservas_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.reservas_service.exception.BadRequestException;
import com.example.reservas_service.exception.ResourceNotFoundException;
import com.example.reservas_service.model.Reserva;
import com.example.reservas_service.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes"}) // Para evitar advertencias de tipo sin usar
@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    private ReservaService reservaService;

    @Mock private ReservaRepository reservaRepository;
    @Mock private WebClient webClient;
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        reservaService = new ReservaService(reservaRepository, webClient);
        Field field = ReservaService.class.getDeclaredField("usuarioPath");
        field.setAccessible(true);
        field.set(reservaService, "http://api/usuarios/%d/exists");
        Field fieldServicio = ReservaService.class.getDeclaredField("servicioPath");
        fieldServicio.setAccessible(true);
        fieldServicio.set(reservaService, "http://api/servicios/%d/nombre");
        Field fieldTratamiento = ReservaService.class.getDeclaredField("tratamientoPath");
        fieldTratamiento.setAccessible(true);
        fieldTratamiento.set(reservaService, "http://api/tratamientos/%d/exists");
        Field fieldServicioExists = ReservaService.class.getDeclaredField("servicioExistsPath");
        fieldServicioExists.setAccessible(true);
        fieldServicioExists.set(reservaService, "http://api/servicios/%d/exists");
    }

    private void mockClienteExistente() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
    }

    private void mockServicioNombre() { // se agrego este metodo 
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Consulta General"));
    }

    // ─── CRUD Tests ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Guardar reserva con cliente valido")
    void testGuardar() {
        mockClienteExistente();
        Reserva reserva = new Reserva(null, 1L, null, null, null, "Test reserva", "PENDIENTE");
        Reserva guardada = new Reserva(1L, 1L, null, null, new Date(), "Test reserva", "PENDIENTE");
        // GIVEN

        when(reservaRepository.save(any(Reserva.class))).thenReturn(guardada);

        Reserva resultado = reservaService.guardar(reserva, "Bearer token");

        // WHEN


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Listar retorna todas las reservas")
    void testListar() {
        Reserva r = new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "PENDIENTE");
        // GIVEN

        when(reservaRepository.findAll()).thenReturn(List.of(r));

        List<Reserva> lista = reservaService.listar();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("ObtenerPorId retorna reserva existente")
    void testObtenerPorId() {
        Reserva r = new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "PENDIENTE");
        // GIVEN

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(r));

        Reserva resultado = reservaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(reservaRepository).findById(1L);
    }

    @Test
    @DisplayName("ObtenerPorId lanza excepcion cuando no existe")
    void testObtenerPorIdNoExiste() {
        // GIVEN

        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservaService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("Actualizar modifica reserva existente")
    void testActualizar() {
        mockServicioNombre();
        Reserva existente = new Reserva(1L, 1L, 1L, 1L, new Date(), "Original", "PENDIENTE");
        // GIVEN

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(existente));

        Reserva cambios = new Reserva(1L, 1L, 1L, 1L, new Date(), "Actualizada", "CONFIRMADA");
        Reserva guardada = new Reserva(1L, 1L, 1L, 1L, new Date(), "Actualizada", "CONFIRMADA");
        // GIVEN

        when(reservaRepository.save(any())).thenReturn(guardada);

        Reserva resultado = reservaService.actualizar(1L, cambios, "Bearer token");

        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Eliminar borra reserva existente")
    void testEliminar() {
        // GIVEN

        when(reservaRepository.existsById(1L)).thenReturn(true);
        reservaService.eliminar(1L);
        verify(reservaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar lanza excepcion cuando no existe")
    void testEliminarNoExiste() {
        // GIVEN

        when(reservaRepository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> reservaService.eliminar(99L));
    }

    @Test
    @DisplayName("ExistePorId retorna true cuando existe")
    void testExistePorId() {
        // GIVEN

        when(reservaRepository.existsById(1L)).thenReturn(true);
        assertTrue(reservaService.existePorId(1L));
    }

    // ─── PRUEBAS DE NEGOCIO (las 3 requeridas por la pauta) ───────────────────

    @Test
    @DisplayName("Negocio: esEstadoValido acepta PENDIENTE, CONFIRMADA y CANCELADA")
    void testNegocio_EstadosValidos() {
        // Given - estados que el sistema acepta
        // When & Then
        assertTrue(reservaService.esEstadoValido("PENDIENTE"),   "PENDIENTE debe ser valido");
        assertTrue(reservaService.esEstadoValido("CONFIRMADA"),  "CONFIRMADA debe ser valido");
        assertTrue(reservaService.esEstadoValido("CANCELADA"),   "CANCELADA debe ser valido");
        assertFalse(reservaService.esEstadoValido("RECHAZADA"),  "RECHAZADA no debe ser valido");
        assertFalse(reservaService.esEstadoValido(""),           "Vacio no debe ser valido");
        assertFalse(reservaService.esEstadoValido(null),         "Null no debe ser valido");
    }

    @Test
    @DisplayName("Negocio: solo reservas PENDIENTE pueden cancelarse")
    void testNegocio_PuedeCancelarse() {
        // Given
        Reserva pendiente  = new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "PENDIENTE");
        Reserva confirmada = new Reserva(2L, 1L, 1L, 1L, new Date(), "Test", "CONFIRMADA");
        Reserva cancelada  = new Reserva(3L, 1L, 1L, 1L, new Date(), "Test", "CANCELADA");

        // When & Then
        assertTrue(reservaService.puedeCancelarse(pendiente),
                "Reserva PENDIENTE si puede cancelarse");
        assertFalse(reservaService.puedeCancelarse(confirmada),
                "Reserva CONFIRMADA NO puede cancelarse");
        assertFalse(reservaService.puedeCancelarse(cancelada),
                "Reserva CANCELADA NO puede cancelarse de nuevo");
    }

    @Test
    @DisplayName("Negocio: calcular dias hasta la fecha de reserva")
    void testNegocio_DiasHastaReserva() {
        // Given - fecha en el futuro (+7 dias)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date fechaFutura = cal.getTime();

        // When
        long dias = reservaService.diasHastaReserva(fechaFutura);

        // Then
        assertTrue(dias >= 6 && dias <= 7,
                "Deben ser aprox 7 dias (resultado=" + dias + ")");

        // Given - fecha nula
        assertEquals(0L, reservaService.diasHastaReserva(null),
                "Fecha nula debe retornar 0");
    }

    @Test
    @DisplayName("Negocio: cancelar reserva PENDIENTE cambia estado a CANCELADA")
    void testNegocio_CancelarReservaPendiente() {
        Reserva pendiente = new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "PENDIENTE");
        // GIVEN

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(pendiente));
        // GIVEN

        when(reservaRepository.save(any())).thenReturn(
                new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "CANCELADA"));

        reservaService.cancelar(1L);

        verify(reservaRepository).save(argThat(r -> "CANCELADA".equals(r.getEstado())));
    }

    @Test
    @DisplayName("Negocio: cancelar reserva CONFIRMADA lanza excepcion")
    void testNegocio_CancelarReservaConfirmadaLanzaExcepcion() {
        Reserva confirmada = new Reserva(1L, 1L, 1L, 1L, new Date(), "Test", "CONFIRMADA");
        // GIVEN

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(confirmada));

        assertThrows(BadRequestException.class, () -> reservaService.cancelar(1L));
    }

    @Test // y el respecitivo test 
    @DisplayName("Guardar reserva con idServicio asigna descripcion desde el servicio")
    void testGuardar_ConIdServicio_AsignaDescripcion() {
        mockServicioNombre();

        Reserva reserva = new Reserva(null, 1L, 1L, 1L, null, null, "PENDIENTE");
        Reserva guardada = new Reserva(1L, 1L, 1L, 1L, new Date(), "Consulta General", "PENDIENTE");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(guardada);

        Reserva resultado = reservaService.guardar(reserva, "Bearer token");

        assertNotNull(resultado);
        assertEquals("Consulta General", resultado.getDescripcion());
        verify(reservaRepository).save(any(Reserva.class));
    }
}
