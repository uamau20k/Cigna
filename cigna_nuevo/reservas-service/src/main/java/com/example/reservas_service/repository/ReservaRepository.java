package com.example.reservas_service.repository;

import com.example.reservas_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByIdCliente(Long idCliente);
    boolean existsByIdAndEstado(Long id, String estado);
}
