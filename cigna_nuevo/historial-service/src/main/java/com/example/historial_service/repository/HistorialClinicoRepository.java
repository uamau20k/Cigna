package com.example.historial_service.repository;

import com.example.historial_service.model.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    List<HistorialClinico> findByIdCliente(Long idCliente);
}
