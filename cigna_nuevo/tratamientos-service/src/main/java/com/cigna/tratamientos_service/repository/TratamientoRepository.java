package com.example.tratamientos_service.repository;

import com.example.tratamientos_service.model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {
}
