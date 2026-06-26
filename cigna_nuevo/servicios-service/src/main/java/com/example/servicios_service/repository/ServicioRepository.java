package com.example.servicios_service.repository;

import com.example.servicios_service.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
