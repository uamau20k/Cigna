package com.example.resena_service.repository;

import com.example.resena_service.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    
    // Obtener todas las reseñas de un servicio específico
    List<Resena> findByIdServicio(Long idServicio);
    
    // Obtener todas las reseñas dejadas por un usuario específico
    List<Resena> findByIdUsuario(Long idUsuario);

    // Verificar si ya existe una reseña de este usuario para este servicio
    boolean existsByIdServicioAndIdUsuario(Long idServicio, Long idUsuario);

    // Calcular el promedio de calificaciones para un servicio específico
    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.idServicio = :idServicio")
    Double findPromedioCalificacionByIdServicio(@Param("idServicio") Long idServicio);

    
}